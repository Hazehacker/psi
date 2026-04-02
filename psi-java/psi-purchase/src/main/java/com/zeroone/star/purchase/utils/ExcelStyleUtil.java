package com.zeroone.star.purchase.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public final class ExcelStyleUtil {
    private ExcelStyleUtil() {}

    /**
     * 创建标题样式（大字体、加粗、居中、无边框）
     */
    public static CellStyle createTitleStyle(Workbook wb) {
        Font font = createFont(wb, (short) 12, true);
        return createBaseCellStyle(wb, HorizontalAlignment.CENTER, false, font, null, false);
    }

    /**
     * 创建表头样式（中字体、加粗、居中、有边框）
     */
    public static CellStyle createHeaderStyle(Workbook wb) {
        Font font = createFont(wb, (short) 11, true);
        return createBaseCellStyle(wb, HorizontalAlignment.CENTER, true, font, null, false);
    }

    /**
     * 创建浅灰色样式（中字体、加粗、居中、有边框、浅灰背景）
     */
    public static XSSFCellStyle createLightGrayStyle(Workbook wb) {
        Font font = createFont(wb, (short) 11, true);
        XSSFColor lightGray = new XSSFColor(new java.awt.Color(242, 242, 242), new DefaultIndexedColorMap());
        CellStyle baseStyle = createBaseCellStyle(wb, HorizontalAlignment.CENTER, true, font, lightGray, false);
        return (XSSFCellStyle) baseStyle;
    }

    /**
     * 创建数据样式（中字体、不加粗、左对齐、有边框、自动换行）
     */
    public static CellStyle createDataStyle(Workbook wb) {
        Font font = createFont(wb, (short) 11, false);
        return createBaseCellStyle(wb, HorizontalAlignment.LEFT, true, font, null, true);
    }

    /**
     * 封装字体创建逻辑
     * @param wb 工作簿
     * @param fontHeight 字体大小
     * @param isBold 是否加粗
     * @return 字体对象
     */
    private static Font createFont(Workbook wb, short fontHeight, boolean isBold) {
        Font font = wb.createFont();
        font.setFontHeightInPoints(fontHeight);
        font.setBold(isBold);
        return font;
    }

    /**
     * 封装单元格样式的基础配置（提取共性逻辑）
     * @param wb 工作簿
     * @param horizontalAlignment 水平对齐方式
     * @param hasBorder 是否需要边框
     * @param font 字体
     * @param fillColor 填充色（仅XSSF支持）
     * @param wrapText 是否自动换行
     * @return 基础单元格样式
     */
    private static CellStyle createBaseCellStyle(Workbook wb, HorizontalAlignment horizontalAlignment,
                                                 boolean hasBorder, Font font, XSSFColor fillColor,
                                                 boolean wrapText) {
        CellStyle style = wb.createCellStyle();
        // 字体设置
        style.setFont(font);
        // 垂直对齐（所有样式均为居中）
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 水平对齐
        style.setAlignment(horizontalAlignment);
        // 边框设置
        if (hasBorder) {
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
        }
        // 填充色设置（仅XSSF样式支持）
        if (fillColor != null && style instanceof XSSFCellStyle) {
            XSSFCellStyle xssfStyle = (XSSFCellStyle) style;
            xssfStyle.setFillForegroundColor(fillColor);
            xssfStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        // 自动换行
        style.setWrapText(wrapText);
        return style;
    }
}