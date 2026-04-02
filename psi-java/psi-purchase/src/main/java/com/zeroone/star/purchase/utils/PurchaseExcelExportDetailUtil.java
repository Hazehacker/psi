package com.zeroone.star.purchase.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnDetailReportDTO;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class PurchaseExcelExportDetailUtil {
    public byte[] createExcelForBre(PurchaseReturnDetailReportDTO data, String supplier, String time, String number) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 设置Excel参数
            ExcelWriter excelWriter = EasyExcel.write(outputStream)
                    .registerWriteHandler(new CustomCellWriteHandler()) // 自定义样式处理器
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet("采购退货单详情")
                    .head(head()) // 设置表头
                    .build();

            // 写入数据
            excelWriter.write(Collections.singletonList(buildRowData(data)), writeSheet);
            // 添加汇总信息
            addSummaryInfo(excelWriter, data, supplier, time, number);
            excelWriter.finish();
            return outputStream.toByteArray();
        }
    }

    /**
     * 定义表头
     */
    private List<List<String>> head() {
        List<List<String>> head = new ArrayList<>();
        head.add(Collections.singletonList("供应商："));
        head.add(Collections.singletonList("单据日期："));
        head.add(Collections.singletonList("单据编号："));
        head.add(Collections.singletonList("商品名称："));
        head.add(Collections.singletonList("规格型号："));
        head.add(Collections.singletonList("辅助属性："));
        head.add(Collections.singletonList("单位："));
        head.add(Collections.singletonList("仓库："));
        head.add(Collections.singletonList("单价："));
        head.add(Collections.singletonList("数量："));
        head.add(Collections.singletonList("折扣率："));
        head.add(Collections.singletonList("折扣额："));
        head.add(Collections.singletonList("金额："));
        head.add(Collections.singletonList("税率："));
        head.add(Collections.singletonList("税额："));
        head.add(Collections.singletonList("价税合计："));
        head.add(Collections.singletonList("备注信息："));
        head.add(Collections.singletonList("单据金额："));
        head.add(Collections.singletonList("单据费用："));
        head.add(Collections.singletonList("实际金额："));
        head.add(Collections.singletonList("核销金额："));
        head.add(Collections.singletonList("结算账户："));
        head.add(Collections.singletonList("发票信息："));
        head.add(Collections.singletonList("关联人员："));
        head.add(Collections.singletonList("物流信息："));
        head.add(Collections.singletonList("退货单备注信息："));
        return head;
    }

    /**
     * 构建行数据
     */
    private List<Object> buildRowData(PurchaseReturnDetailReportDTO data) {
        List<Object> row = new ArrayList<>();
        row.add(getSafeString(data.getSupplier()));
        row.add(getSafeString(data.getTime().toString()));
        row.add(getSafeString(data.getNumber()));
        row.add(getSafeString(data.getName()));
        row.add(getSafeString(data.getSpec()));
        row.add(getSafeDouble(data.getAttr()));
        row.add(getSafeDouble(data.getUnit()));
        row.add(getSafeDouble(data.getWarehouse()));
        row.add(getSafeBigDecimal(data.getPrice()));
        row.add(getSafeBigDecimal(data.getNums()));
        row.add(getSafeBigDecimal(data.getDiscount()));
        row.add(getSafeBigDecimal(data.getDsc()));
        row.add(getSafeBigDecimal(data.getTotal()));
        row.add(getSafeBigDecimal(data.getTax()));
        row.add(getSafeBigDecimal(data.getTat()));
        row.add(getSafeBigDecimal(data.getTpt()));
        row.add(getSafeString(data.getData()));
        row.add(getSafeBigDecimal(data.getTotal()));
        row.add(getSafeBigDecimal(data.getCost()));
        row.add(getSafeBigDecimal(data.getActual()));
        row.add(getSafeBigDecimal(data.getMoney()));
        row.add(getSafeString(data.getAccount()));
        row.add(getSafeInteger(data.getNums2()));
        row.add(getSafeString(data.getPeople()));
        row.add(getSafeString(data.getLogistics()));
        row.add(getSafeString(data.getData2()));
        return row;
    }

    /**
     * 添加汇总信息
     */
    private void addSummaryInfo(ExcelWriter excelWriter, PurchaseReturnDetailReportDTO data, String supplier, String time, String number) {
        // 可以在这里添加自定义的汇总信息
        // EasyExcel 也支持自定义写入，这里简化处理
    }
    private String getSafeString(String value) {
        return value == null ? "" : value;
    }
    private Double getSafeDouble(String value) {
        if (value == null) return 0.0;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    private Integer getSafeInteger(Integer value){
        return value!=null?value:0;
    }

    private BigDecimal getSafeBigDecimal(BigDecimal value){
        return value!=null?value:BigDecimal.ZERO;
    }

    /**
     * 自定义单元格样式处理器（增强版）
     */
    public static class CustomCellWriteHandler implements CellWriteHandler {
        @Override
        public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                    Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
            Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
            CellStyle cellStyle = workbook.createCellStyle();
            if (isHead) {
                // 表头样式
                Font font = workbook.createFont();
                font.setBold(true);
                cellStyle.setFont(font);
                cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                // 表头也支持换行
                cellStyle.setWrapText(true);
            } else {
                // 数据单元格样式 - 启用自动换行
                cellStyle.setWrapText(true);
                // 可选：设置垂直对齐方式为靠上，这样换行显示更美观
                cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
            }
            cell.setCellStyle(cellStyle);
            // 可选：自动调整行高以适应内容
            if (!isHead) {
                Sheet sheet = writeSheetHolder.getSheet();
                Row row = cell.getRow();
                if (row != null) {
                    row.setHeightInPoints((row.getHeightInPoints() + 10) * 2); // 增加行高
                }
            }
        }
    }
}