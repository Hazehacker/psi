package com.zeroone.star.project.components.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.metadata.Head;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 流式导出器
 * 支持分页查询 + 流式写入 + GZIP同步压缩 + 样式池复用
 */
public class StreamingExcelExporter {

    /** 默认每页数据量 */
    private static final int DEFAULT_PAGE_SIZE = 5000;

    /** 默认最大sheet数量 */
    private static final int MAX_SHEET_COUNT = 50;

    /** 每sheet最大行数（10倍页大小） */
    private static final int MAX_ROWS_PER_SHEET = DEFAULT_PAGE_SIZE * 10;

    private final CellStylePool cellStylePool;

    public StreamingExcelExporter() {
        this.cellStylePool = null;
    }

    public StreamingExcelExporter(CellStylePool cellStylePool) {
        this.cellStylePool = cellStylePool;
    }

    /**
     * 流式导出
     *
     * @param sheetName    sheet名称
     * @param outputStream 输出流
     * @param clazz        数据类型
     * @param pageQuery    分页查询器
     * @param pageSize     每页数据量，默认5000
     * @param compressGzip 是否启用GZIP压缩
     * @return 导出结果
     */
    public <T> ExportResult export(String sheetName, OutputStream outputStream,
                                    Class<T> clazz, CursorPageQuery<T> pageQuery,
                                    int pageSize, boolean compressGzip) {
        long startTime = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();
        long peakMemory = startMemory;

        int sheetNo = 0;
        Object lastId = null;
        int totalRows = 0;

        try {
            OutputStream exportStream = outputStream;
            if (compressGzip) {
                exportStream = SyncZipOutputStream.gzip(outputStream);
            }

            ExcelWriterBuilder builder = EasyExcel.write(exportStream, clazz);
            if (cellStylePool != null) {
                builder.registerWriteHandler(new PooledCellWriteHandler(cellStylePool));
            }
            ExcelWriter writer = builder.build();

            WriteSheet sheet = EasyExcel.writerSheet(sheetName)
                    .sheetNo(sheetNo)
                    .build();

            while (true) {
                // 分页查询
                List<T> page = pageQuery.nextPage(lastId, pageSize);
                if (page.isEmpty()) {
                    break;
                }

                // 写入数据
                writer.write(page, sheet);
                totalRows += page.size();

                // 更新游标
                if (pageQuery instanceof MybatisCursorPageQuery) {
                    lastId = ((MybatisCursorPageQuery<?>) pageQuery).getLastId(page);
                } else {
                    // 通用方式：通过反射获取最后一页的最后ID
                    lastId = getLastId(page);
                }

                // 更新峰值内存
                long currentMemory = runtime.totalMemory() - runtime.freeMemory();
                peakMemory = Math.max(peakMemory, currentMemory);

                // 检查是否有更多数据，再决定是否需要创建新sheet
                if (!pageQuery.hasMore(lastId)) {
                    break;
                }

                // 检查是否需要创建新sheet（在确认还有更多数据之后）
                if (totalRows / (sheetNo + 1) >= MAX_ROWS_PER_SHEET && sheetNo < MAX_SHEET_COUNT) {
                    sheetNo++;
                    sheet = EasyExcel.writerSheet(sheetName + "_" + (sheetNo + 1))
                            .sheetNo(sheetNo)
                            .build();
                }
            }

            writer.finish();

            if (compressGzip) {
                exportStream.close();
            }

            // 构建结果
            ExportResult result = new ExportResult(totalRows, System.currentTimeMillis() - startTime);
            result.setPeakMemory(peakMemory - startMemory);
            result.setCompressed(compressGzip);

            return result;

        } catch (IOException e) {
            throw new RuntimeException("Export failed", e);
        }
    }

    /**
     * 简化版流式导出（使用默认参数）
     */
    public <T> ExportResult export(String sheetName, OutputStream outputStream,
                                    Class<T> clazz, CursorPageQuery<T> pageQuery) {
        return export(sheetName, outputStream, clazz, pageQuery, DEFAULT_PAGE_SIZE, false);
    }

    private <T> Object getLastId(List<T> page) {
        if (page == null || page.isEmpty()) {
            return null;
        }
        try {
            Object lastItem = page.get(page.size() - 1);
            Object id = lastItem.getClass().getMethod("getId").invoke(lastItem);
            return id;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get last ID from page", e);
        }
    }

    /**
     * 使用样式池的单元格写入处理器
     */
    private static class PooledCellWriteHandler implements CellWriteHandler {
        private final CellStylePool stylePool;

        public PooledCellWriteHandler(CellStylePool stylePool) {
            this.stylePool = stylePool;
        }

        @Override
        public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                    Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
            Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
            CellStyle style = isHead ? stylePool.getHeaderStyle() : stylePool.getDataStyle();
            cell.setCellStyle(style);
        }
    }
}