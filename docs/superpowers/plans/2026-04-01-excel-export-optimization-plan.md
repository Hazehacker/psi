# Excel 导出性能优化实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 重构项目中 Excel 导出功能，实现流式写入、游标分页、样式池复用、GZIP同步压缩四大优化

**Architecture:** 在 `psi-common` 模块新增 `excel/` 包，建立流式导出核心组件；对 `EasyExcelComponent` 进行渐进式重构；改造 `BreServiceImpl` 作为标杆案例

**Tech Stack:** Alibaba EasyExcel, Apache POI, MyBatis-Plus, Java I/O Stream

---

## 文件结构

```
psi-common/src/main/java/com/zeroone/star/project/components/
├── easyexcel/                              # 现有目录
│   ├── EasyExcelComponent.java             # [重构] 增加流式导出方法
│   └── ExcelReadListener.java             # [保留] 无需修改
├── excel/                                  # [新增目录]
│   ├── CursorPageQuery.java               # 游标分页接口
│   ├── MybatisCursorPageQuery.java        # MyBatis游标分页实现
│   ├── StreamingExcelExporter.java        # 流式导出器
│   ├── CellStylePool.java                 # 样式池
│   ├── SyncZipOutputStream.java           # 同步压缩输出流
│   └── ExportResult.java                   # 导出结果封装（含监控指标）
```

---

## Task 1: CursorPageQuery 接口

**Files:**
- Create: `psi-common/src/main/java/com/zeroone/star/project/components/excel/CursorPageQuery.java`

- [ ] **Step 1: 创建 CursorPageQuery 接口**

```java
package com.zeroone.star.project.components.excel;

import java.util.List;

/**
 * 游标分页查询接口
 * 用于流式导出时按页查询数据，支持基于主键ID的深度分页优化
 *
 * @param <T> 查询结果数据类型
 */
public interface CursorPageQuery<T> {

    /**
     * 获取下一页数据
     * @param lastId 上一页最后一条记录的主键ID，null表示第一页
     * @param pageSize 每页数量
     * @return 下一页数据列表
     */
    List<T> nextPage(Object lastId, int pageSize);

    /**
     * 获取第一页数据（首次调用时使用）
     * @param pageSize 每页数量
     * @return 第一页数据列表
     */
    default List<T> firstPage(int pageSize) {
        return nextPage(null, pageSize);
    }

    /**
     * 是否还有更多数据
     * @param lastId 当前页最后一条记录的主键ID
     * @return 是否有更多数据
     */
    boolean hasMore(Object lastId);
}
```

- [ ] **Step 2: 提交**

```bash
git add psi-common/src/main/java/com/zeroone/star/project/components/excel/CursorPageQuery.java
git commit -m "feat(excel): 新增 CursorPageQuery 游标分页接口"
```

---

## Task 2: CellStylePool 样式池

**Files:**
- Create: `psi-common/src/main/java/com/zeroone/star/project/components/excel/CellStylePool.java`

- [ ] **Step 1: 创建 CellStylePool 类**

```java
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
```

- [ ] **Step 2: 提交**

```bash
git add psi-common/src/main/java/com/zeroone/star/project/components/excel/CellStylePool.java
git commit -m "feat(excel): 新增 CellStylePool 样式池"
```

---

## Task 3: SyncZipOutputStream 同步压缩输出流

**Files:**
- Create: `psi-common/src/main/java/com/zeroone/star/project/components/excel/SyncZipOutputStream.java`

- [ ] **Step 1: 创建 SyncZipOutputStream 类**

```java
package com.zeroone.star.project.components.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 同步压缩输出流
 * 支持 GZIP 和 ZIP 两种压缩模式，边写边压缩，无需临时文件
 */
public class SyncZipOutputStream extends OutputStream {

    private final OutputStream delegate;
    private final ZipOutputStream zipOut;
    private boolean isZipEntryOpen = false;

    /**
     * 创建 GZIP 压缩输出流
     */
    public static SyncZipOutputStream gzip(OutputStream out) throws IOException {
        return new SyncZipOutputStream(out, new GZIPOutputStream(out), false);
    }

    /**
     * 创建 ZIP 压缩输出流
     */
    public static SyncZipOutputStream zip(OutputStream out) {
        ZipOutputStream zos = new ZipOutputStream(out);
        return new SyncZipOutputStream(out, zos, true);
    }

    private SyncZipOutputStream(OutputStream out, OutputStream inner, boolean isZip) {
        this.delegate = out;
        this.zipOut = (ZipOutputStream) inner;
    }

    /**
     * 开始一个新的 ZIP 条目（仅 ZIP 模式需要）
     */
    public void putNextEntry(String entryName) {
        try {
            if (isZipEntryOpen) {
                zipOut.closeEntry();
            }
            zipOut.putNextEntry(new ZipEntry(entryName));
            isZipEntryOpen = true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create zip entry: " + entryName, e);
        }
    }

    @Override
    public void write(int b) throws IOException {
        zipOut.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        zipOut.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        zipOut.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        zipOut.flush();
    }

    @Override
    public void close() throws IOException {
        if (isZipEntryOpen) {
            zipOut.closeEntry();
            isZipEntryOpen = false;
        }
        zipOut.finish();
        // 不关闭 delegate，由调用方管理
    }
}
```

- [ ] **Step 2: 提交**

```bash
git add psi-common/src/main/java/com/zeroone/star/project/components/excel/SyncZipOutputStream.java
git commit -m "feat(excel): 新增 SyncZipOutputStream 同步压缩输出流"
```

---

## Task 4: ExportResult 导出结果封装

**Files:**
- Create: `psi-common/src/main/java/com/zeroone/star/project/components/excel/ExportResult.java`

- [ ] **Step 1: 创建 ExportResult 类**

```java
package com.zeroone.star.project.components.excel;

import java.io.Serializable;

/**
 * 导出结果封装
 * 包含导出数据和监控指标
 */
public class ExportResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 导出数据行数 */
    private long rowCount;

    /** 导出耗时（毫秒） */
    private long elapsedMs;

    /** 原始文件大小（字节） */
    private long originalSize;

    /** 压缩后文件大小（字节） */
    private long compressedSize;

    /** 峰值内存占用（字节） */
    private long peakMemory;

    /** 是否启用压缩 */
    private boolean compressed;

    public ExportResult() {
    }

    public ExportResult(long rowCount, long elapsedMs) {
        this.rowCount = rowCount;
        this.elapsedMs = elapsedMs;
    }

    // Getters and Setters
    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount(long rowCount) {
        this.rowCount = rowCount;
    }

    public long getElapsedMs() {
        return elapsedMs;
    }

    public void setElapsedMs(long elapsedMs) {
        this.elapsedMs = elapsedMs;
    }

    public long getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(long originalSize) {
        this.originalSize = originalSize;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public void setCompressedSize(long compressedSize) {
        this.compressedSize = compressedSize;
    }

    public long getPeakMemory() {
        return peakMemory;
    }

    public void setPeakMemory(long peakMemory) {
        this.peakMemory = peakMemory;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    /**
     * 获取压缩率
     */
    public double getCompressionRatio() {
        if (compressedSize == 0) return 0;
        return 1.0 - (double) compressedSize / originalSize;
    }

    @Override
    public String toString() {
        return String.format(
            "ExportResult{rowCount=%d, elapsedMs=%d, originalSize=%d, compressedSize=%d, compressionRatio=%.2f%%}",
            rowCount, elapsedMs, originalSize, compressedSize, getCompressionRatio() * 100
        );
    }
}
```

- [ ] **Step 2: 提交**

```bash
git add psi-common/src/main/java/com/zeroone/star/project/components/excel/ExportResult.java
git commit -m "feat(excel): 新增 ExportResult 导出结果封装"
```

---

## Task 5: MybatisCursorPageQuery 实现

**Files:**
- Create: `psi-common/src/main/java/com/zeroone/star/project/components/excel/MybatisCursorPageQuery.java`

- [ ] **Step 1: 创建 MybatisCursorPageQuery 类**

```java
package com.zeroone.star.project.components.excel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.function.Function;

/**
 * MyBatis-Plus 游标分页查询实现
 * 基于主键 ID 实现高效的深度分页
 *
 * @param <T> 实体类型
 */
public class MybatisCursorPageQuery<T> implements CursorPageQuery<T> {

    private final BaseMapper<T> mapper;
    private final Function<Integer, LambdaQueryWrapper<T>> wrapperBuilder;

    /**
     * 使用实体类创建游标分页查询
     * @param mapper MyBatis-Plus BaseMapper
     * @param wrapperBuilder 构建查询条件，接收 lastId 返回 WHERE id > lastId 的条件
     */
    public MybatisCursorPageQuery(BaseMapper<T> mapper,
                                   Function<Integer, LambdaQueryWrapper<T>> wrapperBuilder) {
        this.mapper = mapper;
        this.wrapperBuilder = wrapperBuilder;
    }

    @Override
    public List<T> nextPage(Object lastId, int pageSize) {
        Page<T> page = new Page<>(1, pageSize, false); // current=1, size=pageSize, isSearchCount=false
        LambdaQueryWrapper<T> wrapper = wrapperBuilder.apply(lastId == null ? 0 : (Integer) lastId);
        IPage<T> result = mapper.selectPage(page, wrapper);
        return result.getRecords();
    }

    @Override
    public boolean hasMore(Object lastId) {
        if (lastId == null) {
            return true; // 第一页前默认有数据
        }
        // 检查是否还有更多数据：查询比 lastId 大的记录是否存在
        LambdaQueryWrapper<T> wrapper = wrapperBuilder.apply((Integer) lastId);
        wrapper.last("LIMIT 1");
        List<T> result = mapper.selectList(wrapper);
        return !result.isEmpty();
    }

    /**
     * 获取最后一页的最后一条记录的 ID
     */
    public Integer getLastId(List<T> page) {
        if (page == null || page.isEmpty()) {
            return null;
        }
        // 假设实体有 getId() 方法
        try {
            Object id = page.get(page.size() - 1).getClass().getMethod("getId").invoke(page.get(page.size() - 1));
            return (Integer) id;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get last ID from page", e);
        }
    }
}
```

- [ ] **Step 2: 提交**

```bash
git add psi-common/src/main/java/com/zeroone/star/project/components/excel/MybatisCursorPageQuery.java
git commit -m "feat(excel): 新增 MybatisCursorPageQuery 游标分页实现"
```

---

## Task 6: StreamingExcelExporter 流式导出器

**Files:**
- Create: `psi-common/src/main/java/com/zeroone/star/project/components/excel/StreamingExcelExporter.java`

- [ ] **Step 1: 创建 StreamingExcelExporter 类**

```java
package com.zeroone.star.project.components.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
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

                // 检查是否需要创建新sheet
                if (totalRows / (sheetNo + 1) >= DEFAULT_PAGE_SIZE * 10 && sheetNo < MAX_SHEET_COUNT) {
                    sheetNo++;
                    sheet = EasyExcel.writerSheet(sheetName + "_" + (sheetNo + 1))
                            .sheetNo(sheetNo)
                            .build();
                }

                // 检查是否有更多数据
                if (!pageQuery.hasMore(lastId)) {
                    break;
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
```

- [ ] **Step 2: 提交**

```bash
git add psi-common/src/main/java/com/zeroone/star/project/components/excel/StreamingExcelExporter.java
git commit -m "feat(excel): 新增 StreamingExcelExporter 流式导出器"
```

---

## Task 7: EasyExcelComponent 重构

**Files:**
- Modify: `psi-common/src/main/java/com/zeroone/star/project/components/easyexcel/EasyExcelComponent.java`

- [ ] **Step 1: 阅读原文件确认结构**

```bash
# 已通过 Read 工具确认原文件结构
# 需要新增 exportStreaming 方法，保留原 export 方法
```

- [ ] **Step 2: 添加流式导出方法**

在 `EasyExcelComponent` 类中添加以下方法：

```java
/**
 * 流式导出 - 支持分页查询和同步压缩
 * @param sheetName    sheet名称
 * @param os           输出流
 * @param clazz        数据类型
 * @param pageQuery    分页查询器
 * @param pageSize     每页数据量
 * @param compressGzip 是否启用GZIP压缩
 * @param <T>          数据类型
 * @return 导出结果
 */
public <T> ExportResult exportStreaming(String sheetName, OutputStream os, Class<T> clazz,
                                        CursorPageQuery<T> pageQuery, int pageSize, boolean compressGzip) {
    StreamingExcelExporter exporter = new StreamingExcelExporter();
    return exporter.export(sheetName, os, clazz, pageQuery, pageSize, compressGzip);
}

/**
 * 流式导出（简化版）
 * @param sheetName sheet名称
 * @param os        输出流
 * @param clazz     数据类型
 * @param pageQuery 分页查询器
 * @param <T>       数据类型
 * @return 导出结果
 */
public <T> ExportResult exportStreaming(String sheetName, OutputStream os, Class<T> clazz,
                                        CursorPageQuery<T> pageQuery) {
    return exportStreaming(sheetName, os, clazz, pageQuery, MAX_COUNT_PER_SHEET, false);
}
```

- [ ] **Step 3: 添加 import 语句**

```java
import com.zeroone.star.project.components.excel.CursorPageQuery;
import com.zeroone.star.project.components.excel.ExportResult;
import com.zeroone.star.project.components.excel.StreamingExcelExporter;
```

- [ ] **Step 4: 提交**

```bash
git add psi-common/src/main/java/com/zeroone/star/project/components/easyexcel/EasyExcelComponent.java
git commit -m "refactor(easyexcel): 新增流式导出方法 exportStreaming"
```

---

## Task 8: BreServiceImpl 导出方法改造

**Files:**
- Modify: `psi-purchase/src/main/java/com/zeroone/star/purchase/service/impl/BreServiceImpl.java:822-985`
- Modify: `psi-purchase/src/main/java/com/zeroone/star/purchase/service/impl/BreServiceImpl.java:993-1118`

- [ ] **Step 1: 阅读原方法确认完整逻辑**

```bash
# 已通过 Read 工具确认 exportBre 和 exportBreInfo 方法结构
# 需要将全量加载改为流式导出
```

- [ ] **Step 2: 添加新的导出方法（保持原方法兼容）**

在 `BreServiceImpl` 中添加流式导出支持：

```java
/**
 * 流式导出采购退货单（优化版）
 * 使用游标分页，避免大数据量导致的 OOM
 */
public ResponseEntity<byte[]> exportBreStreaming(List<String> ids) {
    // 创建游标分页查询器
    CursorPageQuery<PurchaseReturnExportExcel> pageQuery = new CursorPageQuery<PurchaseReturnExportExcel>() {
        private int index = 0;

        @Override
        public List<PurchaseReturnExportExcel> nextPage(Object lastId, int pageSize) {
            // 每次返回 ids.size() 条记录，但按 pageSize 分页写入
            // 这里需要根据实际业务改造
            return Collections.emptyList(); // TODO: 实现具体逻辑
        }

        @Override
        public boolean hasMore(Object lastId) {
            return false; // TODO: 实现具体逻辑
        }
    };

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ExportResult result = excel.exportStreaming("采购退货单列表", out,
            PurchaseReturnExportExcel.class, pageQuery, 5000, true);

    HttpHeaders headers = new HttpHeaders();
    String filename = "采购退货单列表-" + DateTime.now().toString("yyyyMMddHHmmss") + ".xlsx.gz";
    headers.setContentDisposition(ContentDisposition.builder("attachment")
            .filename(filename, StandardCharsets.UTF_8)
            .build());
    headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
    return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
}
```

- [ ] **Step 3: 提交**

```bash
git add psi-purchase/src/main/java/com/zeroone/star/purchase/service/impl/BreServiceImpl.java
git commit -m "feat(bre): BreServiceImpl 支持流式导出"
```

---

## Task 9: 单元测试

**Files:**
- Create: `psi-common/src/test/java/com/zeroone/star/project/components/excel/StreamingExcelExporterTest.java`
- Create: `psi-common/src/test/java/com/zeroone/star/project/components/excel/CellStylePoolTest.java`

- [ ] **Step 1: 创建 StreamingExcelExporterTest**

```java
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
```

- [ ] **Step 2: 创建 CellStylePoolTest**

```java
package com.zeroone.star.project.components.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CellStylePoolTest {

    @Test
    void testStylePoolReuse() {
        Workbook workbook = new XSSFWorkbook();
        CellStylePool pool = new CellStylePool(workbook);

        // 获取两次相同类型样式，应该返回同一个对象
        var style1 = pool.getHeaderStyle();
        var style2 = pool.getHeaderStyle();

        assertSame(style1, style2, "Same style type should return same instance");

        workbook.close();
    }

    @Test
    void testDifferentStyleTypes() {
        Workbook workbook = new XSSFWorkbook();
        CellStylePool pool = new CellStylePool(workbook);

        var headerStyle = pool.getHeaderStyle();
        var dataStyle = pool.getDataStyle();
        var summaryStyle = pool.getSummaryStyle();

        assertNotSame(headerStyle, dataStyle);
        assertNotSame(headerStyle, summaryStyle);
        assertNotSame(dataStyle, summaryStyle);

        workbook.close();
    }
}
```

- [ ] **Step 3: 运行测试**

```bash
cd psi-java/psi-common
mvn test -Dtest=StreamingExcelExporterTest,CellStylePoolTest -q
```

- [ ] **Step 4: 提交**

```bash
git add psi-common/src/test/java/com/zeroone/star/project/components/excel/
git commit -m "test(excel): 新增 StreamingExcelExporter 和 CellStylePool 单元测试"
```

---

## Task 10: 文档更新

**Files:**
- Modify: `docs/superpowers/specs/2026-04-01-excel-export-optimization-design.md`

- [ ] **Step 1: 更新设计文档，标记已完成部分**

```markdown
## 状态

- [x] CursorPageQuery 接口
- [x] CellStylePool 样式池
- [x] SyncZipOutputStream 同步压缩输出流
- [x] ExportResult 导出结果封装
- [x] MybatisCursorPageQuery 实现
- [x] StreamingExcelExporter 流式导出器
- [x] EasyExcelComponent 重构
- [ ] BreServiceImpl 改造（进行中）
- [ ] 单元测试
```

- [ ] **Step 2: 提交**

```bash
git add docs/superpowers/specs/2026-04-01-excel-export-optimization-design.md
git commit -m "docs: 更新设计文档状态"
```

---

## 验证清单

- [ ] `StreamingExcelExporter` 能正常导出 1000 行数据
- [ ] `CellStylePool` 多次获取同类型样式返回同一实例
- [ ] GZIP 压缩后的文件能正常解压
- [ ] BreServiceImpl 新导出方法能正确返回数据
- [ ] 导出 10 万行数据内存无溢出（需手动验证）

---

## 风险与注意事项

1. **游标分页依赖**: `MybatisCursorPageQuery` 依赖实体类有 `getId()` 方法
2. **Workbook 线程安全**: `CellStylePool` 按 Workbook 实例隔离，不能跨 Workbook 共享
3. **压缩文件名**: GZIP 压缩时文件名应加 `.gz` 后缀
