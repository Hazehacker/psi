package com.zeroone.star.project.components.excel;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StreamingExcelExporterTest {

    @Test
    void testExportWithSmallData() {
        StreamingExcelExporter exporter = new StreamingExcelExporter();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        CursorPageQuery<TestData> pageQuery = new CursorPageQuery<TestData>() {
            private int called = 0;

            @Override
            public List<TestData> nextPage(Object lastId, int pageSize) {
                if (called > 0) return new ArrayList<>();
                called++;
                List<TestData> data = new ArrayList<>();
                data.add(new TestData(1, "Item1"));
                data.add(new TestData(2, "Item2"));
                return data;
            }

            @Override
            public boolean hasMore(Object lastId) {
                return false;
            }
        };

        ExportResult result = exporter.export("Test", out, TestData.class, pageQuery, 5000, false);

        assertEquals(2, result.getRowCount());
        assertTrue(result.getElapsedMs() >= 0);
    }

    @Test
    void testExportWithGzip() {
        StreamingExcelExporter exporter = new StreamingExcelExporter();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        CursorPageQuery<TestData> pageQuery = new CursorPageQuery<TestData>() {
            @Override
            public List<TestData> nextPage(Object lastId, int pageSize) {
                List<TestData> data = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    data.add(new TestData(i, "Item" + i));
                }
                return data;
            }

            @Override
            public boolean hasMore(Object lastId) {
                return false;
            }
        };

        ExportResult result = exporter.export("Test", out, TestData.class, pageQuery, 5000, true);

        assertEquals(100, result.getRowCount());
        assertTrue(result.isCompressed());
    }

    // 测试用实体类
    public static class TestData {
        private Integer id;
        private String name;

        public TestData() {}
        public TestData(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}