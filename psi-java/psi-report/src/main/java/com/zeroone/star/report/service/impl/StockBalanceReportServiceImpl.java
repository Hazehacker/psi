package com.zeroone.star.report.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.common.constant.CacheConstants;
import com.zeroone.star.common.utils.CacheUtil;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.report.ProductStockBalanceDTO;
import com.zeroone.star.project.dto.j8.report.WarehouseCellDTO;
import com.zeroone.star.project.query.j8.report.ProductStockBalanceQuery;
import com.zeroone.star.report.entity.Balance.ProductStockBalanceDetailDO;
import com.zeroone.star.report.entity.Balance.ProductStockBalanceHeadDO;
import com.zeroone.star.report.mapper.ProductStockBalanceMapper;
import com.zeroone.star.report.service.StockBalanceReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StockBalanceReportServiceImpl implements StockBalanceReportService {

    @Resource
    ProductStockBalanceMapper mapper;

    @Resource
    CacheUtil cacheUtil;

    @Override
    public PageDTO<ProductStockBalanceDTO> getProductStocksByCondition(ProductStockBalanceQuery q) {
        // 构建缓存key：前缀 + 查询条件MD5 + 分页
        String cacheKey = CacheConstants.REPORT_STOCK_BALANCE + ":" +
                JSONUtil.toJsonStr(q).hashCode() + ":" +
                q.getPageIndex() + ":" + q.getPageSize();

        try {
            return cacheUtil.queryWithMutex(
                    cacheKey,
                    PageDTO.class,
                    () -> queryData(q),
                    CacheConstants.DEFAULT_TTL,
                    TimeUnit.SECONDS
            );
        } catch (InterruptedException e) {
            log.warn("缓存获取失败，降级查数据库", e);
            Thread.currentThread().interrupt();
        }

        // 降级查询
        return queryData(q);
    }

    /**
     * 查询库存余额数据
     */
    private PageDTO<ProductStockBalanceDTO> queryData(ProductStockBalanceQuery q) {
        // 1) 分页查”商品层（汇总）”
        Page<ProductStockBalanceHeadDO> page = new Page<>(q.getPageIndex(), q.getPageSize());
        IPage<ProductStockBalanceHeadDO> headPage = mapper.pageHeads(page, q);

        // 2) 取本页 goodsId
        List<String> goodsIds = headPage.getRecords().stream()
                .map(ProductStockBalanceHeadDO::getGoodsId)
                .collect(Collectors.toList());

        // 3) 查”仓库层（多）”
        List<ProductStockBalanceDetailDO> detailList = goodsIds.isEmpty()
                ? Collections.<ProductStockBalanceDetailDO>emptyList()
                : mapper.listDetailsByGoodsIds(q, goodsIds);

        // 4) 明细按 goodsId 分组
        final Map<String, List<ProductStockBalanceDetailDO>> byGoods =
                detailList.stream().collect(Collectors.groupingBy(ProductStockBalanceDetailDO::getGoodsId));

        // 5) 组装成 DTO 列表（rows）
        List<ProductStockBalanceDTO> rows = new ArrayList<>(headPage.getRecords().size());
        for (ProductStockBalanceHeadDO h : headPage.getRecords()) {
            ProductStockBalanceDTO dto = new ProductStockBalanceDTO();
            dto.setGoodsId(h.getGoodsId());
            dto.setProductName(h.getProductName());
            dto.setProductCode(h.getProductCode());
            dto.setSpecification(h.getSpecification());
            dto.setUnit(h.getUnit());
            dto.setSumQty(h.getTotalQuantity());
            dto.setSumAmount(h.getTotalAmount());
            dto.setAvgCost(h.getAvgCost());
            // 需要 cells 就填；不要可不填
            List<ProductStockBalanceDetailDO> ds = byGoods.get(h.getGoodsId());
            if (ds != null && !ds.isEmpty()) {
                List<WarehouseCellDTO> cells = new ArrayList<>(ds.size());
                for (ProductStockBalanceDetailDO d : ds) {
                    WarehouseCellDTO c = new WarehouseCellDTO();
                    c.setWarehouseId(d.getWarehouseId());
                    c.setWarehouseName(d.getWarehouseName());
                    c.setQty(d.getQuantity());
                    c.setAmount(d.getAmount());
                    c.setCost(d.getCost());
                    cells.add(c);
                }
                dto.setCells(cells);
            }

            rows.add(dto);
        }

        // 6) 组装成一个完整的 Page<T> 再交给 PageDTO
        Page<ProductStockBalanceDTO> dtoPage =
                new Page<>(headPage.getCurrent(), headPage.getSize());
        dtoPage.setTotal(headPage.getTotal());
        dtoPage.setRecords(rows);

        return PageDTO.create(dtoPage);
    }

    @Override
    public ResponseEntity<byte[]> export(ProductStockBalanceQuery query) {
        // 1) 全部商品层
        List<ProductStockBalanceHeadDO> heads = mapper.listHeads(query);
        if (heads == null || heads.isEmpty()) {
            return buildEmptyExcel("商品库存余额表.xlsx");
        }

        // 2) 全部明细层
        List<String> goodsIds = heads.stream()
                .map(ProductStockBalanceHeadDO::getGoodsId)
                .collect(Collectors.toList());
        List<ProductStockBalanceDetailDO> details = mapper.listDetailsByGoodsIds(query, goodsIds);

        // 3) 本次涉及的仓库顺序（按名称）
        Map<String, String> whNameMap = details.stream()
                .collect(Collectors.toMap(ProductStockBalanceDetailDO::getWarehouseId,
                        ProductStockBalanceDetailDO::getWarehouseName, (a, b) -> a));
        List<String> whOrder = whNameMap.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 4) 明细分组
        Map<String, List<ProductStockBalanceDetailDO>> byGoods =
                details.stream().collect(Collectors.groupingBy(ProductStockBalanceDetailDO::getGoodsId));

        // 5) 组装 DTO 列表
        List<ProductStockBalanceDTO> dtoRows = new ArrayList<>(heads.size());
        for (ProductStockBalanceHeadDO h : heads) {
            ProductStockBalanceDTO dto = toDTO(h, byGoods.get(h.getGoodsId()), query);
            dtoRows.add(dto);
        }

        // 6) 表头（基础4列 + 每仓3列 + 汇总3列）
        List<List<String>> head = new ArrayList<>();
        head.add(Collections.singletonList("商品名称"));
        head.add(Collections.singletonList("商品编号"));
        head.add(Collections.singletonList("规格型号"));
        head.add(Collections.singletonList("单位"));
        for (String wid : whOrder) {
            String wname = whNameMap.get(wid);
            head.add(Collections.singletonList(wname + " 成本"));
            head.add(Collections.singletonList(wname + " 数量"));
            head.add(Collections.singletonList(wname + " 总成本"));
        }
        head.add(Collections.singletonList("总数量"));
        head.add(Collections.singletonList("总金额"));
        head.add(Collections.singletonList("平均成本"));

        // 7) 数据
        List<List<Object>> data = new ArrayList<>(dtoRows.size());
        for (ProductStockBalanceDTO row : dtoRows) {
            List<Object> line = new ArrayList<>();
            line.add(row.getProductName());
            line.add(row.getProductCode());
            line.add(row.getSpecification());
            line.add(row.getUnit());

            Map<String, WarehouseCellDTO> cellByWh = new HashMap<>();
            if (row.getCells() != null) {
                for (WarehouseCellDTO c : row.getCells()) {
                    cellByWh.put(c.getWarehouseId(), c);
                }
            }
            for (String wid : whOrder) {
                WarehouseCellDTO c = cellByWh.get(wid);
                if (c == null) {
                    line.add(BigDecimal.ZERO);
                    line.add(BigDecimal.ZERO);
                    line.add(BigDecimal.ZERO);
                } else {
                    line.add(scale2(c.getCost()));
                    line.add(scale2(c.getQty()));
                    line.add(scale2(c.getAmount()));
                }
            }
            line.add(scale2(row.getSumQty()));
            line.add(scale2(row.getSumAmount()));
            line.add(scale2(row.getAvgCost()));
            data.add(line);
        }

        // 8) 写 Excel
        byte[] bytes;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            EasyExcel.write(out).head(head).sheet("商品库存余额表").doWrite(data);
            bytes = out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("导出库存余额失败", e);
        }

        String fileName = "商品库存余额表" + System.currentTimeMillis() + ".xlsx";
        // JDK8：第二参是 String
        String encoded = null;
        try {
            encoded = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }

    private static ProductStockBalanceDTO toDTO(ProductStockBalanceHeadDO h,
                                                List<ProductStockBalanceDetailDO> details,
                                                ProductStockBalanceQuery query) {
        ProductStockBalanceDTO dto = new ProductStockBalanceDTO();
        dto.setGoodsId(h.getGoodsId());
        dto.setProductName(h.getProductName());
        dto.setProductCode(h.getProductCode());
        dto.setSpecification(h.getSpecification());
        dto.setUnit(h.getUnit());
        dto.setSumQty(h.getTotalQuantity());
        dto.setSumAmount(h.getTotalAmount());
        dto.setAvgCost(h.getAvgCost());
        // 如果是 java.util.Date，请改成：
        // if (query.getQueryDate() != null) {
        //     LocalDate d = query.getQueryDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //     dto.setQueryDate(d);
        // }

        if (details != null && !details.isEmpty()) {
            List<WarehouseCellDTO> cells = new ArrayList<>(details.size());
            for (ProductStockBalanceDetailDO d : details) {
                WarehouseCellDTO c = new WarehouseCellDTO();
                c.setWarehouseId(d.getWarehouseId());
                c.setWarehouseName(d.getWarehouseName());
                c.setQty(d.getQuantity());
                c.setAmount(d.getAmount());
                c.setCost(d.getCost());
                cells.add(c);
            }
            dto.setCells(cells);
        }
        return dto;
    }

    private static BigDecimal scale2(BigDecimal v) {
        if (v == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return v.setScale(2, RoundingMode.HALF_UP);
    }

    private static ResponseEntity<byte[]> buildEmptyExcel(String fileName) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            List<List<String>> head = Arrays.asList(
                    Collections.singletonList("商品名称"),
                    Collections.singletonList("商品编号"),
                    Collections.singletonList("规格型号"),
                    Collections.singletonList("单位"),
                    Collections.singletonList("总数量"),
                    Collections.singletonList("总金额"),
                    Collections.singletonList("平均成本")
            );
            EasyExcel.write(out).head(head).sheet("商品库存余额表").doWrite(Collections.emptyList());
            String encoded = URLEncoder.encode(fileName, "UTF-8");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("导出库存余额失败", e);
        }
    }
}
