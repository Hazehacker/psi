package com.zeroone.star.report.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.common.constant.CacheConstants;
import com.zeroone.star.common.utils.CacheUtil;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.report.wss_KazamataNeri.StockSummaryReportDTO;
import com.zeroone.star.project.query.j8.report.StockSummaryReportQuery;
import com.zeroone.star.report.mapper.StockSummaryReport.StockSummaryReportMapper;
import com.zeroone.star.report.service.StockSummaryReportService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StockSummaryReportServiceImpl implements StockSummaryReportService {
    @Resource
    private StockSummaryReportMapper stockSummaryReportMapper;

    @Resource
    private EasyExcelComponent excel;

    @Resource
    private CacheUtil cacheUtil;

    @Override
    public PageDTO<StockSummaryReportDTO> listGoodsReceiptAndDispatchSummary(StockSummaryReportQuery q) {
        // 构建缓存key：前缀 + 查询条件MD5 + 分页
        String cacheKey = CacheConstants.REPORT_STOCK_SUMMARY + ":" +
                JSONUtil.toJsonStr(q).hashCode() + ":" +
                q.getPageIndex() + ":" + q.getPageSize();

        try {
            return cacheUtil.queryWithMutex(
                    cacheKey,
                    PageDTO.class,
                    () -> {
                        Page<StockSummaryReportDTO> page = Page.of(q.getPageIndex(), q.getPageSize());
                        Page<StockSummaryReportDTO> result = stockSummaryReportMapper.findGoods(page, q);
                        return PageDTO.create(result);
                    },
                    CacheConstants.DEFAULT_TTL,
                    TimeUnit.SECONDS
            );
        } catch (InterruptedException e) {
            // 降级：直接查数据库
            log.warn("缓存获取失败，降级查数据库", e);
            Thread.currentThread().interrupt();
        }

        // 降级查询
        Page<StockSummaryReportDTO> page = Page.of(q.getPageIndex(), q.getPageSize());
        Page<StockSummaryReportDTO> result = stockSummaryReportMapper.findGoods(page, q);
        return PageDTO.create(result);
    }


    @Override
    @SneakyThrows
    public ResponseEntity<byte[]> exportWssExcel(StockSummaryReportQuery q) {
        List<StockSummaryReportDTO> rows = stockSummaryReportMapper.findGoodsAll(q);
        if (rows == null) {
            rows = java.util.Collections.emptyList();
        }

        // ====== 在表尾增加“总条数”一行（显示在 A 列）======
        StockSummaryReportDTO totalRow = new StockSummaryReportDTO();
        totalRow.setName("总条数：" + rows.size());  // A 列显示
        totalRow.setWarehouse("");                   // 其它列留空即可
        totalRow.setNumber("");
        totalRow.setSpec("");
        totalRow.setUnit("");
        rows.add(totalRow);
        // ============================================

        byte[] bytes;
        try (java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
            excel.export("商品收发汇总表", out, StockSummaryReportDTO.class, rows);
            bytes = out.toByteArray();
        }

        String fileName = "商品收发汇总表-" +
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + ".xlsx";
        String encoded = java.net.URLEncoder.encode(fileName, java.nio.charset.StandardCharsets.UTF_8.name())
                .replaceAll("\\+", "%20");

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(bytes.length);
        headers.add("Access-Control-Expose-Headers", "Content-Disposition,Content-Length,Content-Type");

        return new org.springframework.http.ResponseEntity<>(bytes, headers, org.springframework.http.HttpStatus.OK);
    }

}
