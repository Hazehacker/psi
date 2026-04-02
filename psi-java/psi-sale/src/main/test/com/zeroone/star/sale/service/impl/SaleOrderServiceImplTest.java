package com.zeroone.star.sale.service.impl;

import com.zeroone.star.sale.entity.SaleOrderExportDetail;
import com.zeroone.star.sale.entity.SaleOrderExportDetailItem;
import com.zeroone.star.sale.entity.SaleOrderExportSimple;
import com.zeroone.star.sale.entity.SaleOrderImport;
import com.zeroone.star.sale.entity.SaleOrderImportResult;
import com.zeroone.star.sale.service.SaleOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SaleOrderServiceImplTest {

    @Autowired
    private SaleOrderService saleOrderService;

    // 测试用订单编号（需与Excel中数据一致）
    private final String testOrderNo = "BH001";
    // 测试用Excel路径
    private final String testExcelPath = "rrrtttpp.xlsx";
    // 日期格式化器
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");



    // 1. 测试Excel解析为DTO
    @Test
    void testParseExcelToDTO() throws IOException {
        File excelFile = new File(testExcelPath);
        try (FileInputStream fis = new FileInputStream(excelFile)) {
            MultipartFile multipartFile = new MockMultipartFile(
                    "excelFile",
                    excelFile.getName(),
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    fis
            );

            List<SaleOrderImport> result = saleOrderService.parseExcelToDTO(multipartFile);

            // 基础非空验证
            assertNotNull(result, "解析结果不能为null");
            assertFalse(result.isEmpty(), "解析结果不能为空列表");

            // 验证订单主信息
            SaleOrderImport firstOrder = result.get(0);
            assertEquals(testOrderNo, firstOrder.getOrderNumber(), "订单编号解析错误");
            assertEquals("点可云", firstOrder.getCustomer(), "客户名称解析错误");
            assertNotNull(firstOrder.getOrderDate(), "订单日期不能为null");

            // 验证商品明细
            assertFalse(firstOrder.getProductList().isEmpty(), "订单应包含商品明细");
            assertEquals("笔记本", firstOrder.getProductList().get(0).getProductName(), "商品名称解析错误");
        }
    }

    // 2. 测试Excel导入数据库
    @Test
    void testImportFromExcel() throws IOException {
        File excelFile = new File(testExcelPath);
        try (FileInputStream fis = new FileInputStream(excelFile)) {
            MultipartFile multipartFile = new MockMultipartFile(
                    "excelFile",
                    excelFile.getName(),
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    fis
            );

            SaleOrderImportResult importResult = saleOrderService.importFromExcel(multipartFile);

            // 验证导入结果
            assertEquals(0, importResult.getFailCount(), "导入失败：" + importResult.getFailReason());
            assertTrue(importResult.getSuccessCount() > 0, "未导入任何订单");
            assertTrue(importResult.getSuccessOrderNumbers().contains(testOrderNo), "测试订单未成功导入");
        }
    }

    // 3. 测试简洁报表数据查询
    @Test
    void testGetSimpleExportData() {
        List<String> billNos = Arrays.asList(testOrderNo);
        List<SaleOrderExportSimple> simpleList = saleOrderService.getSimpleExportData(billNos);

        // 基础验证
        assertNotNull(simpleList, "查询结果不能为null");
        assertFalse(simpleList.isEmpty(), "未查询到订单数据");
        assertEquals(1, simpleList.size(), "查询结果数量错误");

        // 字段验证（根据SaleOrderExportSimple实体）
        SaleOrderExportSimple simple = simpleList.get(0);
        assertEquals(testOrderNo, simple.getOrderNo(), "单据编号不匹配");
        assertEquals("点可云", simple.getCustomer(), "客户名称不匹配");
        assertNotNull(simple.getOrderTime(), "单据时间不能为null");

        // 金额验证（根据实际Excel数据调整预期值）
        assertNotNull(simple.getOrderAmount(), "单据金额不能为null");
        assertTrue(simple.getOrderAmount().compareTo(BigDecimal.ZERO) > 0, "单据金额应大于0");
    }

    // 4. 测试详细报表数据查询
    @Test
    void testGetDetailExportData() {
        List<String> billNos = Arrays.asList(testOrderNo);
        List<SaleOrderExportDetail> detailList = saleOrderService.getDetailExportData(billNos);

        // 基础验证
        assertNotNull(detailList, "查询结果不能为null");
        assertFalse(detailList.isEmpty(), "未查询到订单数据");
        assertEquals(1, detailList.size(), "查询结果数量错误");

        // 订单表头验证（根据SaleOrderExportDetail实体）
        SaleOrderExportDetail detail = detailList.get(0);
        assertEquals(testOrderNo, detail.getOrderNo(), "单据编号不匹配");
        assertEquals("点可云", detail.getCustomer(), "客户名称不匹配");
        assertEquals(LocalDate.parse("2020-01-01", DATE_FORMATTER), detail.getOrderDate(), "单据日期不匹配");
        assertEquals(new BigDecimal("154.4200"), detail.getTotalOrderAmount(), "总金额不匹配");

        // 商品明细验证（根据SaleOrderExportDetailItem实体）
        List<SaleOrderExportDetailItem> itemList = detail.getItemList();
        assertNotNull(itemList, "商品明细不能为null");
        assertEquals(3, itemList.size(), "商品明细数量错误");

    }

    // 5. 测试简洁报表导出
    @Test
    void testExportSimple() {
        List<String> billNos = Arrays.asList(testOrderNo);
        byte[] exportData = saleOrderService.exportSimple(billNos);

        // 基础验证
        assertNotNull(exportData, "导出字节流不能为null");
        assertTrue(exportData.length > 0, "导出内容不能为空");

        // CSV内容验证
        String csvContent = new String(exportData);
        assertTrue(csvContent.contains("所属组织,客户,单据时间,单据编号,单据金额"), "CSV表头格式错误");
        assertTrue(csvContent.contains(testOrderNo), "导出内容不包含测试订单");
        assertTrue(csvContent.contains("154.42"), "导出内容不包含订单金额");
    }

    // 6. 测试详细报表导出
    @Test
    void testExportDetail() {
        List<String> billNos = Arrays.asList(testOrderNo);
        byte[] exportData = saleOrderService.exportDetail(billNos);

        // 基础验证
        assertNotNull(exportData, "导出字节流不能为null");
        assertTrue(exportData.length > 0, "导出内容不能为空");

        // CSV内容验证
        String csvContent = new String(exportData);
        assertTrue(csvContent.contains("笔记本"), "导出内容不包含商品明细");
        assertTrue(csvContent.contains("鼠标"), "导出内容不包含商品明细");
        assertTrue(csvContent.contains("12.00"), "导出内容不包含商品单价");
    }
}