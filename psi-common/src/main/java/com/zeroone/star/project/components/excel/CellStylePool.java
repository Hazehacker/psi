package com.zeroone.star.project.components.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CellStyle 样式池
 * 复用 CellStyle 对象，避免为每个单元格创建新样式导致内存溢出
 */
public class CellStylePool {

    private final Workbook workbook;
    private final Map<String, CellStyle> styleCache = new ConcurrentHashMap<>();

    public CellStylePool(Workbook workbook) {
        this.workbook = workbook;
    }

    /**
     * 获取表头样式（粗体 + 灰色背景 + 自动换行）
     */
    public CellStyle getHeaderStyle() {
        return getOrCreate("header", () -> {
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setWrapText(true);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            return style;
        });
    }

    /**
     * 获取数据单元格样式（自动换行 + 垂直靠上）
     */
    public CellStyle getDataStyle() {
        return getOrCreate("data", () -> {
            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);
            style.setVerticalAlignment(VerticalAlignment.TOP);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            return style;
        });
    }

    /**
     * 获取汇总行样式（粗体）
     */
    public CellStyle getSummaryStyle() {
        return getOrCreate("summary", () -> {
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            return style;
        });
    }

    /**
     * 获取或创建样式
     */
    private CellStyle getOrCreate(String key, StyleCreator creator) {
        return styleCache.computeIfAbsent(key, k -> creator.create());
    }

    @FunctionalInterface
    private interface StyleCreator {
        CellStyle create();
    }
}