package com.zeroone.star.purchase.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

/**
 * Common small helpers for manipulating sheets and rows.
 */
public final class ExcelHelper {
    private ExcelHelper() {}

    // 常量定义：Excel列宽单位（1字符≈256单位）、最大列宽（255字符）
    private static final int COLUMN_WIDTH_UNIT = 256;
    private static final int MAX_COLUMN_WIDTH = 255 * COLUMN_WIDTH_UNIT;

    /** 把整行 0..lastCellNum-1 的单元格都设置为 style（包含空单元格）。 */
    public static void applyStyleToRow(Row row, CellStyle style, int lastCellNum) {
        if (row == null) return;
        for (int colIndex = 0; colIndex < lastCellNum; colIndex++) {
            // 利用策略直接获取非空单元格，无需额外null判断
            Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(style);
        }
    }

    /** 在 sheet 第一行写标题并合并到最后一列 */
    public static void writeTitle(Sheet sheet, String title, int lastCellNum, CellStyle titleStyle) {
        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(24);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(titleStyle);
        // 计算合并结束列（确保至少合并1列）
        int mergeEndCol = Math.max(0, lastCellNum - 1);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, mergeEndCol));
    }

    /** 在数据后写 summary，texts 前几个单元格写文本，其余设为空但上样式 */
    public static void writeSummaryRow(Sheet sheet, int lastCellNum, List<String> texts, CellStyle style) {
        int rowIndex = sheet.getLastRowNum() + 1;
        Row summaryRow = sheet.createRow(rowIndex);
        for (int colIndex = 0; colIndex < lastCellNum; colIndex++) {
            Cell cell = summaryRow.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            // 按索引设置值，超出texts长度则为空
            String cellValue = (colIndex < texts.size()) ? texts.get(colIndex) : "";
            cell.setCellValue(cellValue);
            cell.setCellStyle(style);
        }
    }

    /** 放大列宽（multiplier）并处理原宽为 0 的列，defaultChars 为默认字符宽（例如 16） */
    public static void scaleColumnWidths(Sheet sheet, int lastCellNum, int multiplier, int defaultChars) {
        for (int colIndex = 0; colIndex < lastCellNum; colIndex++) {
            int currentWidth = sheet.getColumnWidth(colIndex);
            int newWidth;
            if (currentWidth <= 0) {
                // 原宽为0时，用默认字符宽计算
                newWidth = defaultChars * COLUMN_WIDTH_UNIT * multiplier;
            } else {
                // 按比例放大，不超过最大列宽
                long scaledWidth = (long) currentWidth * multiplier;
                newWidth = (int) Math.min(scaledWidth, MAX_COLUMN_WIDTH);
            }
            sheet.setColumnWidth(colIndex, newWidth);
        }
    }

    /** 把 EasyExcel 生成的 bytes 转为 POI Workbook，交给 poiProcessor 处理后返回最终 bytes */
    public static byte[] processEasyExcelBytes(byte[] inBytes, Consumer<Workbook> poiProcessor) throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(inBytes);
             Workbook workbook = new XSSFWorkbook(inputStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            poiProcessor.accept(workbook);
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * 将详情对象按 pid 分组。
     * 优先尝试 detail.getPid()（通过反射）；若没有 getPid() 方法，则使用索引对齐（best-effort）。
     */
    public static <D, E> Map<String, List<E>> groupDetailsByPid(List<D> buyInfoDOS, List<E> detailList) {
        Map<String, List<E>> result = new LinkedHashMap<>();
        if (detailList == null || detailList.isEmpty()) return result;

        // 尝试获取详情对象的getPid()方法
        Method getPidMethod = getGetPidMethod(detailList.get(0).getClass());
        if (getPidMethod != null) {
            // 有getPid()方法时，直接通过方法分组
            for (E detail : detailList) {
                String pid = getPidValue(detail, getPidMethod);
                result.computeIfAbsent(pid, k -> new ArrayList<>()).add(detail);
            }
            return result;
        }

        // 无getPid()方法时，按索引与主数据对齐
        Map<Integer, String> indexToPid = buildIndexToPidMap(buyInfoDOS);
        for (int i = 0; i < detailList.size(); i++) {
            E detail = detailList.get(i);
            String pid = indexToPid.getOrDefault(i, "");
            result.computeIfAbsent(pid, k -> new ArrayList<>()).add(detail);
        }
        return result;
    }

    /** 反射获取类的getPid()方法（私有工具方法） */
    private static Method getGetPidMethod(Class<?> clazz) {
        try {
            return clazz.getMethod("getPid");
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    /** 调用getPid()方法获取pid值（处理异常，私有工具方法） */
    private static String getPidValue(Object obj, Method getPidMethod) {
        try {
            Object pidObj = getPidMethod.invoke(obj);
            return pidObj == null ? "" : pidObj.toString();
        } catch (Exception e) {
            return ""; // 异常时默认归到空字符串组
        }
    }

    /** 构建主数据索引到pid的映射（私有工具方法） */
    private static <D> Map<Integer, String> buildIndexToPidMap(List<D> mainList) {
        Map<Integer, String> indexToPid = new HashMap<>();
        if (mainList == null) return indexToPid;

        for (int i = 0; i < mainList.size(); i++) {
            D mainObj = mainList.get(i);
            Method getPidMethod = getGetPidMethod(mainObj.getClass());
            String pid = (getPidMethod != null) ? getPidValue(mainObj, getPidMethod) : "";
            indexToPid.put(i, pid);
        }
        return indexToPid;
    }
}