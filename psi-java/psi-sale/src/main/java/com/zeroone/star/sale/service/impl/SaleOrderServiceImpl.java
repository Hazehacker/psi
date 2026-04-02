package com.zeroone.star.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.sale.PurchaseOrderGenerateDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderGenerateDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.j4.sale.SaleOrderListDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderVerifyDTO;
import com.zeroone.star.project.query.j4.sale.SaleOrderQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.sale.entity.*;
import com.zeroone.star.sale.entity.SaleOrder;
import com.zeroone.star.sale.mapper.SaleOrderMapper;
import com.zeroone.star.sale.service.SaleOrderService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.excel.read.listener.PageReadListener;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.util.List;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;


@Service
public class SaleOrderServiceImpl extends ServiceImpl<SaleOrderMapper, SaleOrder> implements SaleOrderService {

    @Resource
    private SaleOrderMapper saleOrderMapper;


    // 日期格式化器
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public JsonVO<PageDTO<SaleOrderListDTO>> page(SaleOrderQuery query) {
        // 防止非法分页参数
        long pageIndex = query.getPageIndex() < 1 ? 1 : query.getPageIndex();
        long pageSize = query.getPageSize() < 1 ? 10 : query.getPageSize();

        Page<SaleOrderListDTO> mpPage = new Page<>(pageIndex, pageSize);

        Page<SaleOrderListDTO> resultPage = saleOrderMapper.list(mpPage, query);

        PageDTO<SaleOrderListDTO> pageDTO = PageDTO.create(resultPage);

        return JsonVO.success(pageDTO);
    }

    /**
     * 生成销售单数据
     */
    public JsonVO<SaleOrderGenerateDTO> generateSaleOrderData(String saleId){
        // 1. 调用 Mapper 查询数据
       SaleOrderGenerateDTO saleOrderGenerateDTO = saleOrderMapper.selectSaleById(saleId);

        // 2. 封装响应结果
        if (saleOrderGenerateDTO == null) {
            return JsonVO.create(null, 10008, "未找到该销售单");
        }
        return JsonVO.success(saleOrderGenerateDTO);
    }

    /**
     *生成采购单
     */
    @Override
    public JsonVO<PurchaseOrderGenerateDTO> generatePurchaseOrderData(String purchaseId) {
        // 1. 调用 Mapper 查询数据
        PurchaseOrderGenerateDTO purchaseOrderGenerateDTO = saleOrderMapper.selectPurchaseById(purchaseId);

        // 2. 封装响应结果
        if (purchaseOrderGenerateDTO == null) {
            return JsonVO.create(null, 10008, "未找到该采购单");
        }
        return JsonVO.success(purchaseOrderGenerateDTO);
    }



    @Override
    public Boolean saleOrderDelete(List<String> ids) {
        return remove(new QueryWrapper<SaleOrder>().in("id", ids));
    }

    @Override
    public Boolean saleOrderAdd(SaleOrderListDTO dto) {
        SaleOrder entity = new SaleOrder();
        BeanUtils.copyProperties(dto, entity);
        return save(entity);
    }

    @Override
    public Boolean saleOrderChange(SaleOrderListDTO dto) {
        SaleOrder entity = new SaleOrder();
        BeanUtils.copyProperties(dto, entity);
        return updateById(entity);
    }

    @Override
    public Boolean verifyOrder(SaleOrderVerifyDTO request) {
        UpdateWrapper<SaleOrder> wrapper = new UpdateWrapper<>();
        wrapper.in("id", request.getIds());
        wrapper.set("examine", request.getNum());
        return update(wrapper);
    }

    /**
     * Excel导入逻辑：解析→校验→转换→入库
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaleOrderImportResult importFromExcel(MultipartFile file) {
        SaleOrderImportResult result = new SaleOrderImportResult();
        List<String> successNos = new ArrayList<>();
        List<String> failNos = new ArrayList<>();
        StringBuilder failReason = new StringBuilder();

        try {
            // 1. 解析CSV为SaleOrderImport列表
            List<SaleOrderImport> importDTOList = parseExcelToDTO(file);
            if (importDTOList.isEmpty()) {
                result.setFailReason("CSV文件无有效数据");
                return result;
            }

            // 2. 校验重复单据编号
            List<String> allOrderNos = importDTOList.stream()
                    .map(SaleOrderImport::getOrderNumber)
                    .collect(Collectors.toList());
            List<String> duplicateNos = saleOrderMapper.checkDuplicateNumber(allOrderNos);
            if (!duplicateNos.isEmpty()) {
                failNos.addAll(duplicateNos);
                failReason.append("重复单据编号：").append(String.join(",", duplicateNos)).append("；");
                importDTOList.removeIf(dto -> duplicateNos.contains(dto.getOrderNumber()));
            }

            // 3. DTO转实体
            List<SaleOrder> saleOrderList = new ArrayList<>();
            List<SaleOrderInfo> saleOrderInfoList = new ArrayList<>();

            for (SaleOrderImport importDTO : importDTOList) {
                String orderNo = importDTO.getOrderNumber();
                String orderId = UUID.randomUUID().toString().replace("-", "").substring(0, 16); // 生成ID

                // 3.1 主表实体映射
                SaleOrder saleOrder = new SaleOrder();
                saleOrder.setId(orderId);
                saleOrder.setFrame(importDTO.getCustomer()); // 实际需关联组织表，这里简化
                saleOrder.setCustomer(importDTO.getCustomer());
                saleOrder.setTime(LocalDateTime.of(importDTO.getOrderDate(), LocalDateTime.MIN.toLocalTime()));
                saleOrder.setNumber(orderNo);
                saleOrder.setTotal(importDTO.getTotalAmount());
                saleOrder.setActual(importDTO.getActualAmount());
                saleOrder.setPeople(importDTO.getRelatedPerson());
                saleOrder.setArrival(LocalDateTime.of(importDTO.getArrivalDate(), LocalDateTime.MIN.toLocalTime()));
                saleOrder.setLogistics(importDTO.getLogisticsInfo());
                saleOrder.setData(importDTO.getOrderRemark());
                saleOrder.setExamine(0); // 未审核
                saleOrder.setState(0); // 未出库
                saleOrder.setUser("系统导入");
                saleOrderList.add(saleOrder);

                // 3.2 明细表实体映射
                for (SaleOrderItemImport itemDTO : importDTO.getProductList()) {
                    String infoId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
                    SaleOrderInfo info = new SaleOrderInfo();
                    info.setId(infoId);
                    info.setPid(orderId); // 关联主表ID
                    info.setGoods(itemDTO.getProductName());
                    info.setAttr(itemDTO.getAuxiliaryAttribute());
                    info.setUnit(itemDTO.getUnit());
                    info.setWarehouse(itemDTO.getWarehouse());
                    info.setPrice(itemDTO.getUnitPrice());
                    info.setNums(itemDTO.getQuantity());
                    info.setDiscount(itemDTO.getDiscountRate());
                    info.setDsc(itemDTO.getDiscountAmount());
                    info.setTotal(itemDTO.getAmount());
                    info.setTax(itemDTO.getTaxRate());
                    info.setTat(itemDTO.getTaxAmount());
                    info.setTpt(itemDTO.getTotalWithTax());
                    info.setData(itemDTO.getProductRemark());
                    info.setHandle(BigDecimal.ZERO);
                    saleOrderInfoList.add(info);
                }

                successNos.add(orderNo);
            }

            // 4. 批量入库
            if (!saleOrderList.isEmpty()) {
                saleOrderMapper.batchInsert(saleOrderList);
                saleOrderMapper.batchInfoInsert(saleOrderInfoList);
            }

            // 5. 封装结果
            result.setSuccessCount(successNos.size());
            result.setFailCount(failNos.size());
            result.setSuccessOrderNumbers(successNos);
            result.setFailOrderNumbers(failNos);
            result.setFailReason(failReason.toString());

        } catch (Exception e) {
            throw new RuntimeException("导入失败：" + e.getMessage());
        }

        return result;
    }

    /**
     * 导出简单报表
     */
    @Override
    public byte[] exportSimple(List<String> billNos) {
        List<SaleOrderExportSimple> simpleDTOList = getSimpleExportData(billNos);
        if (simpleDTOList.isEmpty()) {
            throw new RuntimeException("未查询到简洁报表数据");
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(
                     new OutputStreamWriter(out, StandardCharsets.UTF_8),
                     CSVFormat.DEFAULT.withHeader(
                             "销售订单列表",
                             "所属组织", "客户", "单据时间", "单据编号", "单据金额",
                             "实际金额", "到货日期", "关联人员", "审核状态",
                             "出库状态", "制单人", "备注信息"
                     )
             )) {

            for (SaleOrderExportSimple dto : simpleDTOList) {
                printer.printRecord(
                        dto.getOrganization(),
                        dto.getCustomer(),
                        formatDateTime(dto.getOrderTime()),
                        dto.getOrderNo(),
                        dto.getOrderAmount(),
                        dto.getActualAmount(),
                        formatDateTime(dto.getArrivalDate()),
                        dto.getRelatedPerson(),
                        dto.getAuditStatus(),
                        dto.getDeliveryStatus(),
                        dto.getCreator(),
                        dto.getRemarks()
                );
            }
            printer.flush();
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("简洁报表导出失败：" + e.getMessage());
        }
    }

    /**
     * 导出详细报表（全局只导出一次标题行和客户信息行）
     */

    @Override
    public byte[] exportDetail(List<String> billNos) {
        List<SaleOrderExportDetail> detailDTOList = getDetailExportData(billNos);
        if (detailDTOList.isEmpty()) {
            throw new RuntimeException("未查询到详细报表数据");
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(
                     new OutputStreamWriter(out, StandardCharsets.UTF_8),
                     CSVFormat.DEFAULT
             )) {

            // 第1行：销售订单标题行（全报表只出现一次）
            printer.printRecord("销售订单");

            // 第2行：客户信息行（全报表只出现一次，取第一个订单的信息）
            SaleOrderExportDetail firstDto = detailDTOList.get(0);
            printer.printRecord(
                    "客户：" + firstDto.getCustomer(),
                    "单据日期：" + formatDate(firstDto.getOrderDate()),
                    "单据编号：" + firstDto.getOrderNo()
            );

            // 第3行：商品明细表头行（全报表只出现一次）
            printer.printRecord(
                    "商品名称", "规格型号", "辅助属性", "单位", "仓库",
                    "单价", "数量", "已出库数量", "折扣率(%)", "折扣额",
                    "金额", "商品备注"
            );

            // 循环导出所有订单的商品明细
            for (SaleOrderExportDetail dto : detailDTOList) {
                List<SaleOrderExportDetailItem> items = dto.getItemList();
                if (items.isEmpty()) {
                    continue;
                }

                // 导出当前订单的所有商品明细
                for (SaleOrderExportDetailItem item : items) {
                    printer.printRecord(
                            item.getProductName(),
                            item.getSpecification(),
                            item.getAuxiliaryAttr(),
                            item.getUnit(),
                            item.getWarehouse(),
                            formatBigDecimal(item.getUnitPrice()),
                            item.getQuantity(),
                            item.getDeliveredQuantity(),
                            item.getDiscountRate(),
                            formatBigDecimal(item.getDiscountAmount()),
                            formatBigDecimal(item.getAmount()),
                            item.getItemRemarks()
                    );
                }
            }

            printer.printRecord(
                    "单据金额：" + firstDto.getTotalOrderAmount(),
                    "实际金额：" + firstDto.getTotalActualAmount(),
                    "关联人员：" + firstDto.getRelatedPerson(),
                    "到货日期：" + formatDate(firstDto.getArrivalDate()),
                    "物流信息：" + firstDto.getLogisticsInfo(),
                    "备注信息：" + firstDto.getOrderRemarks()
            );

            printer.flush();
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("详细报表导出失败：" + e.getMessage());
        }
    }


    /**
     * 查询简单报表数据
     */
    @Override
    public List<SaleOrderExportSimple> getSimpleExportData(List<String> billNos) {
        return saleOrderMapper.selectSimpleExportByNumbers(billNos);
    }

    /**
     * 查询详细报表数据
     */
    @Override
    public List<SaleOrderExportDetail> getDetailExportData(List<String> billNos) {
        // 1. 查询主表数据
        List<SaleOrderExportDetail> detailList = saleOrderMapper.selectDetailExportMain(billNos);
        if (detailList.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 提取主表ID，查询关联明细
        List<String> orderNos = detailList.stream()
                .map(SaleOrderExportDetail::getOrderNo)
                .collect(Collectors.toList());
        List<SaleOrderExportDetailItem> itemList = saleOrderMapper.selectDetailExportItems(orderNos);

        // 3. 关联明细到主表DTO
        Map<String, List<SaleOrderExportDetailItem>> itemMap = itemList.stream()
                .collect(Collectors.groupingBy(SaleOrderExportDetailItem::getOrderNo));

        detailList.forEach(detail -> detail.setItemList(itemMap.getOrDefault(detail.getOrderNo(), Collections.emptyList())));
        return detailList;
    }

    /**
     * 解析Excel为SaleOrderImport列表
     */
    @Override
    public List<SaleOrderImport> parseExcelToDTO(MultipartFile file) throws IOException {
        List<SaleOrderImport> result = new ArrayList<>();
        SaleOrderImport[] currentOrderWrapper = new SaleOrderImport[1];

        EasyExcel.read(
                        file.getInputStream(),  // 输入流
                        SaleOrderExcelRow.class, // 单行映射实体
                        new PageReadListener<SaleOrderExcelRow>(dataList -> { // 数据读取监听器
                            for (SaleOrderExcelRow excelRow : dataList) {
                                // 1. 处理主表：单据编号非空 → 新订单
                                String orderNumber = excelRow.getOrderNumber();
                                if (orderNumber != null && !orderNumber.trim().isEmpty()) {
                                    // 添加上一个订单（如果存在）
                                    if (currentOrderWrapper[0] != null) {
                                        result.add(currentOrderWrapper[0]);
                                    }
                                    // 初始化新订单
                                    SaleOrderImport newOrder = new SaleOrderImport();
                                    newOrder.setCustomer(excelRow.getCustomer());
                                    newOrder.setOrderDate(excelRow.getOrderDate());
                                    newOrder.setOrderNumber(orderNumber);
                                    newOrder.setTotalAmount(excelRow.getTotalAmount());
                                    newOrder.setActualAmount(excelRow.getActualAmount());
                                    newOrder.setRelatedPerson(excelRow.getRelatedPerson());
                                    newOrder.setArrivalDate(excelRow.getArrivalDate());
                                    newOrder.setLogisticsInfo(excelRow.getLogisticsInfo());
                                    newOrder.setOrderRemark(excelRow.getOrderRemark());
                                    newOrder.setProductList(new ArrayList<>()); // 初始化明细列表
                                    currentOrderWrapper[0] = newOrder;
                                }

                                // 2. 处理明细：当前订单已初始化 + 商品名称非空
                                if (currentOrderWrapper[0] != null) {
                                    String productName = excelRow.getProductName();
                                    if (productName != null && !productName.trim().isEmpty()) {
                                        SaleOrderItemImport item = new SaleOrderItemImport();
                                        // 赋值明细字段（省略，保持原有逻辑）
                                        item.setProductName(productName);
                                        item.setAuxiliaryAttribute(excelRow.getAuxiliaryAttribute());
                                        item.setUnit(excelRow.getUnit());
                                        item.setWarehouse(excelRow.getWarehouse());
                                        item.setUnitPrice(excelRow.getUnitPrice());
                                        item.setQuantity(excelRow.getQuantity());
                                        item.setDiscountRate(excelRow.getDiscountRate());
                                        item.setDiscountAmount(excelRow.getDiscountAmount());
                                        item.setAmount(excelRow.getAmount());
                                        item.setTaxRate(excelRow.getTaxRate());
                                        item.setTaxAmount(excelRow.getTaxAmount());
                                        item.setTotalWithTax(excelRow.getTotalWithTax());
                                        item.setProductRemark(excelRow.getProductRemark());
                                        // 添加到当前订单明细
                                        currentOrderWrapper[0].getProductList().add(item);
                                    }
                                }
                            }
                            // 3. 添加最后一个订单
                            if (currentOrderWrapper[0] != null && !result.contains(currentOrderWrapper[0])) {
                                result.add(currentOrderWrapper[0]);
                            }
                        })
                )
                .sheet() // 第一步：获取要读取的Sheet（默认第1个）
                .headRowNumber(2) // 第二步：配置跳过第2行表头（必须跟在sheet()之后）
                .doRead(); // 第三步：执行读取

        return result;
    }
    /**
     * 格式化LocalDateTime
     */
    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : "";
    }

    /**
     * 格式化LocalDate
     */
    private String formatDate(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
    }

    // 金额格式化工具方法
    private String formatBigDecimal(BigDecimal number) {
        return number != null ? number.setScale(2).toPlainString() : "0.00";
    }

}
