package com.zeroone.star.purchase.service.impl;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.project.dto.j3.purchase.PurchaseOrderAddDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseOrderInfoDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseOrderUpdateDTO;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.purchase.DO.BorDO;
import com.zeroone.star.purchase.DO.BorInfoDO;
import com.zeroone.star.purchase.mapper.BorMapper;
import com.zeroone.star.purchase.service.IBorInfoService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j3.purchase.*;
import com.zeroone.star.project.query.j3.purchase.PurchaseOrderQuery;
import com.zeroone.star.purchase.DO.BorDO;
import com.zeroone.star.purchase.DO.BorInfoDO;
import com.zeroone.star.purchase.DO.BuyDO;
import com.zeroone.star.purchase.DO.GoodsDO;
import com.zeroone.star.purchase.mapper.BorInfoMapper;
import com.zeroone.star.purchase.mapper.BorMapper;
import com.zeroone.star.purchase.mapper.BuyMapper;
import cn.hutool.core.date.DateTime;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.project.dto.j3.purchase.GoodsDataDTO;
import com.zeroone.star.purchase.DO.BorDO;
import com.zeroone.star.purchase.DO.BorInfoDO;
import com.zeroone.star.purchase.DO.GoodsDO;
import com.zeroone.star.purchase.mapper.BorInfoMapper;
import com.zeroone.star.purchase.mapper.BorMapper;
import com.zeroone.star.purchase.mapper.GoodsMapper;
import com.zeroone.star.purchase.service.IBorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 采购订单 服务实现类
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
@Service
public class BorServiceImpl extends ServiceImpl<BorMapper, BorDO> implements IBorService {
    @Resource
    private IBorInfoService borInfoService;
    @Resource
    private UserHolder userHolder;

    /**
     * 新增采购订单
     * @param purchaseOrderAddDTO
     * @return
     */
    @Transactional
    public JsonVO<String> addPurchaseOrder(PurchaseOrderAddDTO purchaseOrderAddDTO) {
        BorDO bor = BeanUtil.copyProperties(purchaseOrderAddDTO, BorDO.class);

        List<PurchaseOrderInfoDTO> borInfoDTOList = purchaseOrderAddDTO.getPurchaseOrderInfoDTOList();

        // 1、将bor插入数据库中，并返回生成的id
        bor.setExamine(0);      // 设置审核状态为未审核
        bor.setState(0);        // 设置入库状态为未入库
        try {
            bor.setUser(userHolder.getCurrentUser().getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        save(bor);

        String id = bor.getId();

        // 2、将borInfo插入到数据库中
        List<BorInfoDO> borInfoList = new ArrayList<>();
        for (PurchaseOrderInfoDTO borInfoDTO : borInfoDTOList) {
            BorInfoDO borInfo = BeanUtil.copyProperties(borInfoDTO, BorInfoDO.class);
            borInfo.setPid(id);    // 设置所属id
            borInfoList.add(borInfo);
        }
        borInfoService.saveBatch(borInfoList);

        return JsonVO.success(id);
    }

    /**
     * 修改采购订单
     * @param purchaseOrderUpdateDTO
     * @return
     */
    @Override
    @Transactional
    public JsonVO<String> updatePurchaseOrder(PurchaseOrderUpdateDTO purchaseOrderUpdateDTO) {

        String id = purchaseOrderUpdateDTO.getId();

        BorDO bor = BeanUtil.copyProperties(purchaseOrderUpdateDTO, BorDO.class);
        List<PurchaseOrderInfoDTO> borInfoDTOList = purchaseOrderUpdateDTO.getPurchaseOrderInfoDTOList();

        try {
            // 设置操作人 id
            bor.setUser(userHolder.getCurrentUser().getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 修改采购订单
        updateById(bor);

        // 修改采购订单详情
        // 1、删除原始的采购订单详情信息
        QueryWrapper<BorInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", id);
        borInfoService.remove(queryWrapper);
        // 2、插入新的采购订单详情信息
        ArrayList<BorInfoDO> borInfoList = new ArrayList<>();
        for (PurchaseOrderInfoDTO borInfoDTO : borInfoDTOList) {
            BorInfoDO borInfo = BeanUtil.copyProperties(borInfoDTO, BorInfoDO.class);
            borInfo.setPid(id);    // 设置所属id
            borInfoList.add(borInfo);
        }
        borInfoService.saveBatch(borInfoList);

        return JsonVO.success(id);
    }

    /**
     * 删除采购订单
     * @param ids
     * @return
     */
    @Transactional
    public JsonVO<String> deletePurchaseOrder(List<String> ids) {

        // 删除采购订单详情信息
        QueryWrapper<BorInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("pid", ids);
        borInfoService.remove(queryWrapper);

        // 删除采购订单
        removeByIds(ids);

        return JsonVO.success("删除采购订单成功！！");
    }

    /**
     * 获取采购订单列表(条件+分页)
     * @Author 待兼唐怀瑟
     * @param query
     * @return
     */
    @Override
    public PageDTO<PurchaseOrderListDTO> listPurchaseOrders(PurchaseOrderQuery query) {
        // 分页插件对象
        IPage<BorDO> ipage = new Page<>(query.getPageIndex(), query.getPageSize());

        // 条件
        LambdaQueryWrapper<BorDO> w = new LambdaQueryWrapper<>();
        w.likeRight(StringUtils.isNotBlank(query.getNumber()), BorDO::getNumber, query.getNumber())
                .eq(StringUtils.isNotBlank(query.getSupplier()), BorDO::getSupplier, query.getSupplier())
                .eq(StringUtils.isNotBlank(query.getPeople()), BorDO::getPeople, query.getPeople())
                .eq(StringUtils.isNotBlank(query.getUser()), BorDO::getUser, query.getUser())
                .eq(StringUtils.isNotBlank(query.getExamine()), BorDO::getExamine, query.getExamine())
                .eq(StringUtils.isNotBlank(query.getState()), BorDO::getState, query.getState());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (StringUtils.isNotBlank(query.getDocumentStartTime())) {
            LocalDateTime start = LocalDate.parse(query.getDocumentStartTime(), fmt).atStartOfDay();
            w.ge(BorDO::getTime, start);
        }
        if (StringUtils.isNotBlank(query.getDocumentEndTime())) {
            LocalDateTime end = LocalDate.parse(query.getDocumentEndTime(), fmt).atTime(23, 59, 59);
            w.le(BorDO::getTime, end);
        }
        if (StringUtils.isNotBlank(query.getArrivalStartTime())) {
            LocalDateTime start = LocalDate.parse(query.getArrivalStartTime(), fmt).atStartOfDay();
            w.ge(BorDO::getArrival, start);
        }
        if (StringUtils.isNotBlank(query.getArrivalEndTime())) {
            LocalDateTime end = LocalDate.parse(query.getArrivalEndTime(), fmt).atTime(23, 59, 59);
            w.le(BorDO::getArrival, end);
        }
        w.orderByDesc(BorDO::getTime);

        // 执行分页
        ipage = this.page(ipage, w);

        return PageDTO.create((Page<BorDO>)ipage, PurchaseOrderListDTO.class);
    }

    @Resource
    private BorInfoMapper borInfoMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private BuyMapper buyMapper;

    /**
     * 获取指定采购订单详情
     * @Author 待兼唐怀瑟
     * @param id
     * @return
     */
    @Override
    public PurchaseOrderDetailDTO getPurchaseOrderDetail(String id) {
        BorDO bor = this.getById(id);
        if (bor == null) throw new RuntimeException("单据不存在");

        PurchaseOrderDetailDTO dto = new PurchaseOrderDetailDTO();

        // 采购订单详情
        List<BorInfoDO> infoList = borInfoMapper.selectList(
                new LambdaQueryWrapper<BorInfoDO>().eq(BorInfoDO::getPid, id)
                        .last("LIMIT 1"));
        if (!infoList.isEmpty()) {
            BorInfoDO info = infoList.get(0);
            GoodsDO g = goodsMapper.selectById(info.getGoods());

            dto.setGoods(info.getGoods());      // 商品编号
            dto.setName(g == null ? "" : g.getName()); // 商品名称
            dto.setSpec(g == null ? "" : g.getSpec()); // 规格型号
            dto.setAttr(info.getAttr());        // 辅助属性
            dto.setWarehouse(info.getWarehouse()); // 仓库
            dto.setUnit(info.getUnit());        // 单位
            dto.setPrice(info.getPrice().toPlainString()); // 单价
            dto.setNums(info.getNums().toPlainString());   // 数量
            dto.setHandle(info.getHandle().toPlainString());// 入库数量
            dto.setAmount(info.getTotal().toPlainString()); // 金额
            dto.setTax(info.getTax().toPlainString());      // 税率(%)
            dto.setTat(info.getTat().toPlainString());      // 税额
            dto.setTpt(info.getTpt().toPlainString());      // 价税合计
            dto.setData(info.getData());        // 备注信息（明细）
        }

        //抬头字段
        PurchaseOrderOtherDetailDTO header = new PurchaseOrderOtherDetailDTO();
        header.setSupplier(bor.getSupplier());   // 供应商
        header.setTime(bor.getTime().toLocalDate().toString()); // 单据日期
        header.setNumber(bor.getNumber());       // 单据编号
        header.setTotal(bor.getTotal().toPlainString()); // 单据金额
        header.setActual(bor.getActual().toPlainString()); // 实际金额
        header.setPeople(bor.getPeople());       // 关联人员
        header.setArrivalTime(bor.getArrival() == null ? "" : bor.getArrival().toLocalDate().toString()); // 到货日期
        header.setLogistics(bor.getLogistics()); // 物流信息
        header.setFile(bor.getFile());           // 单据附件
        header.setData(bor.getData());           // 备注信息（主单）

        dto.setPurchaseOrderOtherDetailDTO(header);
        return dto;
    }

    /**
     * 审核/反审核(支持批量)
     * @Author 待兼唐怀瑟
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> batchApprovePurchaseOrders(AuditRequestDTO dto) {
        List<String> idList = dto.getIds()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.toList());

        // 反审核锁:bor.number在buy.source中存在且 number非空禁止反审核
        if (dto.getType() == 0) {   // 反审核
            for (String id : idList) {
                // 用 bor.id → 拿到 bor 抬头
                BorDO bor = this.getById(id);
                if (bor == null) continue;

                // 锁:buy.source = bor.number且number非空禁止反审核
                BuyDO buy = buyMapper.selectOne(
                        new LambdaQueryWrapper<BuyDO>()
                                .eq(BuyDO::getSource, bor.getNumber())
                                .isNotNull(BuyDO::getNumber)
                                .last("LIMIT 1")
                );
                if (buy != null && buy.getNumber() != null && !buy.getNumber().isEmpty()) {
                    throw new RuntimeException("单据已生成采购单，禁止反审核：" + buy.getNumber());
                }
            }
        }

        // 批量更新 bor 审核状态
        LambdaUpdateWrapper<BorDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(BorDO::getExamine, dto.getType() == 1)
                .in(BorDO::getId, idList);

        this.update(wrapper);
        // 返回受影响 ID
        return idList;

    }
    @Resource
    EasyExcelComponent excel;

    @Resource
    BorMapper borMapper;

    /**
     * 导出简单报表
     * @author 小王
     * @param ids 采购订单ID列表
     * @return
     */
    @Override
    public ResponseEntity<byte[]> exportBor(List<String> ids) {
        try {
            // 1. 准备数据
            List<BorDO> borDOS = borMapper.selectBatchIds(ids);
            if (borDOS == null || borDOS.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("未找到对应的采购订单数据".getBytes());
            }

            // 2. 定义表头
            List<List<String>> headList = Arrays.asList(
                    Collections.singletonList("所属组织"),
                    Collections.singletonList("供应商"),
                    Collections.singletonList("单据时间"),
                    Collections.singletonList("单据编号"),
                    Collections.singletonList("单据金额"),
                    Collections.singletonList("实际金额"),
                    Collections.singletonList("到货日期"),
                    Collections.singletonList("审核状态"),
                    Collections.singletonList("入库状态"),
                    Collections.singletonList("关联人员"),
                    Collections.singletonList("制单人"),
                    Collections.singletonList("备注信息")
            );

            // 3. 创建日期格式化器
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // 4. 转换为二维行数据
            List<List<Object>> rowList = new ArrayList<>();
            for (BorDO bor : borDOS) {
                // 入库状态转换（Integer类型：0-3）
                Integer state = bor.getState();
                String stateDesc = "";
                if (state != null) {
                    switch (state) {
                        case 0: stateDesc = "未入库"; break;
                        case 1: stateDesc = "部分入库"; break;
                        case 2: stateDesc = "已入库"; break;
                        case 3: stateDesc = "关闭"; break;
                        default: stateDesc = "未知";
                    }
                }
                
                // 审核状态转换
                String examineDesc = "";
                if (bor.getExamine() != null) {
                    examineDesc = bor.getExamine()==1? "已审核" : "未审核";
                }
                
                rowList.add(Arrays.asList(
                        bor.getFrame() != null ? bor.getFrame() : "",
                        bor.getSupplier() != null ? bor.getSupplier() : "",
                        bor.getTime() != null ? bor.getTime().format(dateTimeFormatter) : "",
                        bor.getNumber() != null ? bor.getNumber() : "",
                        bor.getTotal() != null ? bor.getTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() : "",
                        bor.getActual() != null ? bor.getActual().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() : "",
                        bor.getArrival() != null ? bor.getArrival().format(dateTimeFormatter) : "",
                        examineDesc,
                        stateDesc,
                        bor.getPeople() != null ? bor.getPeople() : "",
                        bor.getUser() != null ? bor.getUser() : "",
                        bor.getData() != null ? bor.getData() : ""
                ));
            }

            // 5. 计算统计信息
            int totalCount = borDOS.size();
            BigDecimal totalAmount = borDOS.stream()
                    .map(BorDO::getTotal)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalActual = borDOS.stream()
                    .map(BorDO::getActual)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 6. 添加统计行（不合并列）
            List<Object> statsRow = new ArrayList<>();
            statsRow.add("总数:" + totalCount);
            statsRow.add("总单据金额:" + totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            statsRow.add("总实际金额:" + totalActual.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            // 第4-12列：空字符串
            for (int i = 3; i < headList.size(); i++) {
                statsRow.add("");
            }
            rowList.add(statsRow);

            // 7. 导出为 Excel
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                EasyExcel.write(out)
                        .head(headList)
                        .sheet("采购订单列表")
                        .doWrite(rowList);

                byte[] bytes = out.toByteArray();

                String filename = "采购订单列表-" + DateTime.now().toString("yyyyMMddHHmmss") + ".xlsx";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentDisposition(ContentDisposition.builder("attachment")
                        .filename(filename, StandardCharsets.UTF_8)
                        .build());
                headers.setContentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                headers.setContentLength(bytes.length);
                headers.setCacheControl("no-cache, no-store, must-revalidate");

                return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(("导出失败：" + e.getMessage()).getBytes());
        }
    }

    /**
     * 导入数据
     * @author 小王
     * @param file Excel文件
     * @return 导入结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importBor(MultipartFile file) {
        try {
            // 1. 读取Excel文件（跳过前2行表头，从第3行开始读取）
            List<Map<Integer, String>> allRows = EasyExcel.read(file.getInputStream())
                    .sheet(0)
                    .doReadSync();

            // 调试：打印所有行的信息
            System.out.println("总行数：" + allRows.size());
            for (int i = 0; i < Math.min(allRows.size(), 5); i++) {
                System.out.println("第" + (i + 1) + "行（索引" + i + "）：" + allRows.get(i));
            }

            // 跳过前2行表头，从第3行开始（索引2）
            // 注意：如果EasyExcel跳过了第一行表头（合并单元格），那么：
            // allRows.get(0) = 第2行表头（列名）
            // allRows.get(1) = 第3行数据（第一行数据）
            // allRows.get(2) = 第4行数据（第二行数据）
            // 所以应该使用 subList(1, ...) 而不是 subList(2, ...)
            if (allRows.size() < 2) {
                throw new RuntimeException("导入数据为空，请至少填写一行数据");
            }

            // 根据调试信息，如果第一行表头（合并单元格）被跳过，则从索引1开始
            // 如果第一行表头被读取，则从索引2开始
            // 先尝试从索引1开始（跳过第一行表头）
            List<Map<Integer, String>> dataRows = allRows.subList(1, allRows.size());
            
            // 检查第一行数据是否包含单据资料（供应商、单据编号等）
            if (!dataRows.isEmpty()) {
                Map<Integer, String> firstRow = dataRows.get(0);
                String firstSupplier = getCellValue(firstRow, 0);
                String firstOrderNo = getCellValue(firstRow, 2);
                // 如果第一行数据的单据资料为空，说明可能跳过了第一行表头，需要再跳过一行
                if (!org.springframework.util.StringUtils.hasText(firstSupplier) && !org.springframework.util.StringUtils.hasText(firstOrderNo)) {
                    // 第一行数据没有单据资料，说明这是第二行表头，需要再跳过一行
                    if (dataRows.size() > 1) {
                        dataRows = dataRows.subList(1, dataRows.size());
                        System.out.println("检测到第一行是表头，已跳过，实际数据从索引2开始");
                    }
                }
            }

            // 2. 获取当前登录用户ID
            String currentUserId;
            // try {
            //     currentUserId = userHolder.getCurrentUser().getId();
            // } catch (Exception e) {
            //     throw new RuntimeException("获取当前用户信息失败");
            // }
            currentUserId = "10"; // 临时使用固定值，用于测试

            // 3. 数据结构和验证错误收集
            // Map<单据编号, BorDO数据>
            Map<String, BorDO> borMap = new LinkedHashMap<>();
            // Map<单据编号, List<BorInfoDO数据>>
            Map<String, List<BorInfoDO>> borInfoMap = new LinkedHashMap<>();
            // 验证错误信息列表
            List<String> errorMessages = new ArrayList<>();

            // 4. 解析Excel数据并验证
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            int rowIndex = 3; // 从第3行开始（Excel行号从1开始，所以是3）
            String currentOrderNo = null; // 当前处理的单据编号

            for (Map<Integer, String> row : dataRows) {
                // 4.1 读取单据资料字段（列0-8）
                // 注意：如果Excel有合并单元格，列索引可能会错位，需要根据实际情况调整
                String supplier = getCellValue(row, 0); // 供应商（必填）
                String orderDate = getCellValue(row, 1); // 单据日期（必填）
                String orderNo = getCellValue(row, 2); // 单据编号（必填）
                String orderAmount = getCellValue(row, 3); // 单据金额
                String actualAmount = getCellValue(row, 4); // 实际金额（必填）
                String relatedUser = getCellValue(row, 5); // 关联人员
                String arrivalDate = getCellValue(row, 6); // 到货日期
                String logistics = getCellValue(row, 7); // 物流信息
                String remark = getCellValue(row, 8); // 备注信息

                // 4.2 读取商品资料字段（列9-21）
                String productName = getCellValue(row, 9); // 商品名称（必填）
                String attr = getCellValue(row, 10); // 辅助属性
                String unit = getCellValue(row, 11); // 单位
                String warehouse = getCellValue(row, 12); // 仓库（必填）
                String price = getCellValue(row, 13); // 单价（必填）
                String quantity = getCellValue(row, 14); // 数量（必填）
                String discountRate = getCellValue(row, 15); // 折扣率(%)（必填）
                String discountAmount = getCellValue(row, 16); // 折扣额
                String amount = getCellValue(row, 17); // 金额
                String taxRate = getCellValue(row, 18); // 税率(%)（必填）
                String taxAmount = getCellValue(row, 19); // 税额
                String totalAmount = getCellValue(row, 20); // 价税合计
                String productRemark = getCellValue(row, 21); // 备注信息
                
                // 调试：打印第一行数据的所有列，帮助定位问题
                if (rowIndex == 3) {
                    System.out.println("第一行数据所有列：" + row);
                }

                // 4.3 判断是否为同一单据的商品行
                // 如果单据编号为空，且供应商、单据日期、实际金额都为空，说明这是同一个单据的商品行
                boolean isSameOrderRow = !org.springframework.util.StringUtils.hasText(orderNo)
                        && !org.springframework.util.StringUtils.hasText(supplier)
                        && !org.springframework.util.StringUtils.hasText(orderDate)
                        && !org.springframework.util.StringUtils.hasText(actualAmount);

                if (isSameOrderRow) {
                    // 使用上一个有效的单据编号
                    if (currentOrderNo == null) {
                        // 如果是第一行数据（rowIndex == 3），说明第一行数据的所有单据资料字段都为空
                        // 这种情况下，应该提示第一行数据必须有完整的单据资料
                        if (rowIndex == 3) {
                            errorMessages.add("第" + rowIndex + "行：第一行数据必须填写完整的单据资料（供应商、单据日期、单据编号、实际金额）");
                        } else {
                            errorMessages.add("第" + rowIndex + "行：单据资料为空，但找不到上一个有效的单据编号");
                        }
                        rowIndex++;
                        continue;
                    }
                    orderNo = currentOrderNo;
                }

                // 4.4 收集该行的验证错误
                List<String> rowErrors = new ArrayList<>();

                // 验证单据资料必填字段（仅对新单据行验证）
                if (!isSameOrderRow) {
                    if (!org.springframework.util.StringUtils.hasText(supplier)) {
                        rowErrors.add("供应商不能为空");
                    }
                    if (!org.springframework.util.StringUtils.hasText(orderDate)) {
                        rowErrors.add("单据日期不能为空");
                    }
                    if (!org.springframework.util.StringUtils.hasText(orderNo)) {
                        rowErrors.add("单据编号不能为空");
                    }
                    if (!org.springframework.util.StringUtils.hasText(actualAmount)) {
                        rowErrors.add("实际金额不能为空");
                    }
                    
                    // 关键修复：如果单据编号有效（即使其他字段有错误），也先设置currentOrderNo
                    // 这样即使当前行处理失败，后续同一单据的商品行也能找到有效的单据编号
                    if (org.springframework.util.StringUtils.hasText(orderNo) && currentOrderNo == null) {
                        currentOrderNo = orderNo;
                    }
                }

                // 验证商品资料必填字段（所有行都需要验证）
                if (!org.springframework.util.StringUtils.hasText(productName)) {
                    rowErrors.add("商品名称不能为空");
                }
                if (!org.springframework.util.StringUtils.hasText(warehouse)) {
                    rowErrors.add("仓库不能为空");
                }
                if (!org.springframework.util.StringUtils.hasText(price)) {
                    rowErrors.add("单价不能为空");
                }
                if (!org.springframework.util.StringUtils.hasText(quantity)) {
                    rowErrors.add("数量不能为空");
                }
                if (!org.springframework.util.StringUtils.hasText(discountRate)) {
                    rowErrors.add("折扣率(%)不能为空");
                }
                if (!org.springframework.util.StringUtils.hasText(taxRate)) {
                    rowErrors.add("税率(%)不能为空");
                }

                // 如果有必填字段错误，记录并跳过该行
                if (!rowErrors.isEmpty()) {
                    for (String error : rowErrors) {
                        errorMessages.add("第" + rowIndex + "行：" + error);
                    }
                    rowIndex++;
                    continue;
                }

                // 确保单据编号不为空（如果还是为空，说明没有上一个有效单据）
                if (!org.springframework.util.StringUtils.hasText(orderNo)) {
                    errorMessages.add("第" + rowIndex + "行：单据编号不能为空");
                    rowIndex++;
                    continue;
                }

                // 如果是新单据行，在验证通过后立即更新currentOrderNo，以便后续同一单据的商品行可以使用
                // 注意：这里必须在验证通过后立即设置，即使后续处理出错，currentOrderNo也已经设置了
                if (!isSameOrderRow && org.springframework.util.StringUtils.hasText(orderNo)) {
                    currentOrderNo = orderNo;
                }

                // 4.5 数据类型转换（先解析商品资料字段，用于创建商品）
                BigDecimal priceDecimal = parseBigDecimal(price);
                if (priceDecimal == null) {
                    errorMessages.add("第" + rowIndex + "行：单价不能为空");
                    rowIndex++;
                    continue;
                }

                BigDecimal nums = parseBigDecimal(quantity);
                if (nums == null) {
                    errorMessages.add("第" + rowIndex + "行：数量不能为空");
                    rowIndex++;
                    continue;
                }

                BigDecimal discount = parseBigDecimal(discountRate);
                if (discount == null) {
                    discount = BigDecimal.ZERO;
                }

                BigDecimal tax = parseBigDecimal(taxRate);
                if (tax == null) {
                    tax = BigDecimal.ZERO;
                }

                // 4.6 数据类型转换（仅对新单据行转换，同一单据的商品行使用已存在的数据）
                LocalDateTime time = null;
                BigDecimal total = null;
                BigDecimal actual = null;
                LocalDateTime arrival = null;

                if (!isSameOrderRow) {
                    // 新单据行，需要转换数据
                    if (org.springframework.util.StringUtils.hasText(orderDate)) {
                        try {
                            LocalDate date = LocalDate.parse(orderDate, dateFormatter);
                            time = date.atStartOfDay();
                        } catch (Exception e) {
                            errorMessages.add("第" + rowIndex + "行：单据日期格式错误，应为yyyy-MM-dd");
                            rowIndex++;
                            continue;
                        }
                    }

                    if (org.springframework.util.StringUtils.hasText(arrivalDate)) {
                        try {
                            LocalDate date = LocalDate.parse(arrivalDate, dateFormatter);
                            arrival = date.atStartOfDay();
                        } catch (Exception e) {
                            // 到货日期格式错误不影响，设为null
                        }
                    }

                    total = parseBigDecimal(orderAmount);
                    actual = parseBigDecimal(actualAmount);
                    if (actual == null) {
                        errorMessages.add("第" + rowIndex + "行：实际金额不能为空");
                        rowIndex++;
                        continue;
                    }
                } else {
                    // 同一单据的商品行，从已存在的BorDO中获取数据
                    BorDO existingBor = borMap.get(orderNo);
                    if (existingBor != null) {
                        time = existingBor.getTime();
                        total = existingBor.getTotal();
                        actual = existingBor.getActual();
                        arrival = existingBor.getArrival();
                    } else {
                        // 如果找不到已存在的BorDO，说明数据有问题
                        errorMessages.add("第" + rowIndex + "行：找不到对应的单据信息");
                        rowIndex++;
                        continue;
                    }
                }

                BigDecimal dsc = parseBigDecimal(discountAmount);
                BigDecimal totalAmountDecimal = parseBigDecimal(amount);
                BigDecimal tat = parseBigDecimal(taxAmount);
                BigDecimal tpt = parseBigDecimal(totalAmount);

                // 4.7 验证商品名称是否存在，不存在则自动创建
                LambdaQueryWrapper<GoodsDO> goodsWrapper = new LambdaQueryWrapper<>();
                goodsWrapper.eq(GoodsDO::getName, productName);
                GoodsDO goodsDO = goodsMapper.selectOne(goodsWrapper);
                
                if (goodsDO == null) {
                    // 商品不存在，自动创建新商品
                    goodsDO = new GoodsDO();
                    goodsDO.setId(UUID.randomUUID().toString().replace("-", ""));
                    goodsDO.setName(productName);
                    goodsDO.setPy(productName); // 拼音信息，暂时使用商品名称
                    goodsDO.setNumber("GOODS" + System.currentTimeMillis() + (int)(Math.random() * 1000)); // 自动生成商品编号
                    goodsDO.setCategory("默认类别"); // 默认商品类别
                    goodsDO.setUnit(org.springframework.util.StringUtils.hasText(unit) ? unit : "个"); // 单位，如果为空使用Excel中的单位，再为空则默认"个"
                    goodsDO.setBuy(priceDecimal); // 采购价格，使用Excel中的单价
                    goodsDO.setSell(priceDecimal.multiply(new BigDecimal("1.2"))); // 销售价格，默认是采购价格的1.2倍
                    goodsDO.setStock(BigDecimal.ZERO); // 库存阈值，默认0
                    goodsDO.setType(0); // 产品类型，默认0（常规商品）
                    goodsDO.setMore("{}"); // 扩展信息，默认空JSON
                    goodsMapper.insert(goodsDO);
                }

                // 4.8 构建BorDO（主表数据，按单据编号去重，仅对新单据行创建）
                if (!borMap.containsKey(orderNo) && !isSameOrderRow) {
                    BorDO borDO = new BorDO();
                    borDO.setId(UUID.randomUUID().toString().replace("-", ""));
                    borDO.setSupplier(supplier);
                    borDO.setTime(time);
                    borDO.setNumber(orderNo);
                    borDO.setTotal(total);
                    borDO.setActual(actual);
                    borDO.setPeople(relatedUser);
                    borDO.setArrival(arrival);
                    borDO.setLogistics(logistics);
                    borDO.setData(remark);
                    borDO.setFrame("0"); // 默认所属组织
                    borDO.setExamine(0); // 默认未审核
                    borDO.setState(0); // 默认未入库
                    borDO.setUser(currentUserId); // 制单人
                    borMap.put(orderNo, borDO);
                    borInfoMap.put(orderNo, new ArrayList<>());
                    // 成功创建BorDO后，更新当前单据编号
                    currentOrderNo = orderNo;
                }

                // 4.9 构建BorInfoDO（子表数据）
                BorInfoDO borInfoDO = new BorInfoDO();
                borInfoDO.setId(UUID.randomUUID().toString().replace("-", ""));
                borInfoDO.setPid(borMap.get(orderNo).getId()); // 关联主表ID
                borInfoDO.setGoods(goodsDO.getId()); // 商品ID
                borInfoDO.setAttr(attr);
                // unit字段数据库NOT NULL，如果为空设置默认值
                borInfoDO.setUnit(org.springframework.util.StringUtils.hasText(unit) ? unit : "个");
                borInfoDO.setWarehouse(warehouse);
                borInfoDO.setPrice(priceDecimal);
                borInfoDO.setNums(nums);
                borInfoDO.setDiscount(discount);
                borInfoDO.setDsc(dsc);
                borInfoDO.setTotal(totalAmountDecimal);
                borInfoDO.setTax(tax);
                borInfoDO.setTat(tat);
                borInfoDO.setTpt(tpt);
                borInfoDO.setData(productRemark);
                borInfoDO.setHandle(BigDecimal.ZERO); // 默认入库数量为0

                borInfoMap.get(orderNo).add(borInfoDO);
                rowIndex++;
            }

            // 5. 如果有验证错误，抛出异常
            if (!errorMessages.isEmpty()) {
                String detailedError = String.join("；", errorMessages);
                throw new RuntimeException("信息不全，请补全必填字段：" + detailedError);
            }

            // 6. 处理主表和子表数据（插入或更新）
            for (Map.Entry<String, BorDO> entry : borMap.entrySet()) {
                String orderNo = entry.getKey();
                BorDO borDO = entry.getValue();

                // 6.1 查询单据编号是否已存在
                LambdaQueryWrapper<BorDO> borWrapper = new LambdaQueryWrapper<>();
                borWrapper.eq(BorDO::getNumber, orderNo);
                BorDO existingBor = borMapper.selectOne(borWrapper);

                if (existingBor != null) {
                    // 6.2 更新主表
                    borDO.setId(existingBor.getId());
                    borMapper.updateById(borDO);

                    // 6.3 删除旧的子表记录
                    LambdaQueryWrapper<BorInfoDO> borInfoWrapper = new LambdaQueryWrapper<>();
                    borInfoWrapper.eq(BorInfoDO::getPid, existingBor.getId());
                    borInfoMapper.delete(borInfoWrapper);

                    // 6.4 插入新的子表记录
                    List<BorInfoDO> borInfoList = borInfoMap.get(orderNo);
                    for (BorInfoDO borInfo : borInfoList) {
                        borInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                        borInfo.setPid(existingBor.getId());
                        borInfoMapper.insert(borInfo);
                    }
                } else {
                    // 6.5 插入主表
                    borMapper.insert(borDO);

                    // 6.6 插入子表
                    List<BorInfoDO> borInfoList = borInfoMap.get(orderNo);
                    for (BorInfoDO borInfo : borInfoList) {
                        borInfo.setPid(borDO.getId());
                        borInfoMapper.insert(borInfo);
                    }
                }
            }

            return true;

        } catch (RuntimeException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("读取Excel文件失败：" + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("导入数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取Excel单元格值
     */
    private String getCellValue(Map<Integer, String> row, int columnIndex) {
        String value = row.get(columnIndex);
        return value != null ? value.trim() : "";
    }

    /**
     * 解析BigDecimal
     */
    private BigDecimal parseBigDecimal(String value) {
        if (!org.springframework.util.StringUtils.hasText(value)) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (Exception e) {
            return null;
        }
    }
}
