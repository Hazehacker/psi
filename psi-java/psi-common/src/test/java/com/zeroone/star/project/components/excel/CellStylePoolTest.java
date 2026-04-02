package com.zeroone.star.project.components.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CellStylePoolTest {

    @Test
    void testStylePoolReuse() {
        try (Workbook workbook = new XSSFWorkbook()) {
            CellStylePool pool = new CellStylePool(workbook);

            // 获取两次相同类型样式，应该返回同一个对象
            var style1 = pool.getHeaderStyle();
            var style2 = pool.getHeaderStyle();

            assertSame(style1, style2, "Same style type should return same instance");
        }
    }

    @Test
    void testDifferentStyleTypes() {
        try (Workbook workbook = new XSSFWorkbook()) {
            CellStylePool pool = new CellStylePool(workbook);

            var headerStyle = pool.getHeaderStyle();
            var dataStyle = pool.getDataStyle();
            var summaryStyle = pool.getSummaryStyle();

            assertNotSame(headerStyle, dataStyle);
            assertNotSame(headerStyle, summaryStyle);
            assertNotSame(dataStyle, summaryStyle);
        }
    }
}