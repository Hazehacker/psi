# Excel 导出性能优化设计方案

## 1. Context

项目中大量使用 EasyExcel 进行数据导出，当前实现存在以下问题：
- 大数据量导出时频繁 Full GC / OOM
- `CustomCellWriteHandler` 为每个单元格创建新 CellStyle，内存开销大
- 数据全量加载到内存后再写入，无法支持超大数据集
- 无异步导出、游标分页、压缩等优化手段

本文档旨在建立一套完整的 Excel 导出优化方案，覆盖内存优化、速度优化、可观测性三个维度。

## 2. 核心组件设计

### 2.1 StreamingExcelExporter（流式导出器）

**位置**: `psi-common/src/main/java/com/zeroone/star/project/components/excel/StreamingExcelExporter.java`

**职责**: 提供真正的流式导出能力，按页查询数据并即时写入输出流，内存中只保留一页数据。

```java
public class StreamingExcelExporter {

    /**
     * 流式导出 - 支持分页查询和同步压缩
     * @param sheetName      sheet名称
     * @param outputStream   输出流
     * @param clazz          数据类型
     * @param pageQuery      分页查询器（负责按页查询数据）
     * @param pageSize       每页数据量，默认5000
     * @param compressGzip   是否启用GZIP压缩
     */
    public <T> void export(String sheetName, OutputStream outputStream,
                          Class<T> clazz, CursorPageQuery<T> pageQuery,
                          int pageSize, boolean compressGzip);
}
```

**流式写入流程**:
```
for (每页数据) {
    List<T> page = pageQuery.nextPage();
    if (page.isEmpty()) break;
    writer.write(page, sheet);
}
```

### 2.2 CursorPageQuery（游标分页接口）

**位置**: `psi-common/src/main/java/com/zeroone/star/project/components/excel/CursorPageQuery.java`

**职责**: 定义游标分页查询契约，支持基于主键 ID 的深度分页优化。

```java
public interface CursorPageQuery<T> {
    /**
     * 获取下一页数据
     * @param lastId 上一页最后一条记录的主键ID，null表示第一页
     * @param pageSize 每页数量
     * @return 下一页数据
     */
    List<T> nextPage(Object lastId, int pageSize);

    /**
     * 是否还有更多数据
     */
    boolean hasMore(Object lastId);
}
```

### 2.3 MybatisCursorPageQuery（MyBatis 实现）

**位置**: `psi-common/src/main/java/com/zeroone/star/project/components/excel/MybatisCursorPageQuery.java`

**职责**: 提供基于 MyBatis-Plus 的游标分页查询实现。

```java
public class MybatisCursorPageQuery<T> implements CursorPageQuery<T> {
    private final Class<T> entityClass;
    private final IPage<T> query(Object lastId, int pageSize);

    @Override
    public List<T> nextPage(Object lastId, int pageSize) {
        return query(lastId, pageSize);
    }
}
```

### 2.4 CellStylePool（样式池）

**位置**: `psi-common/src/main/java/com/zeroone/star/project/components/excel/CellStylePool.java`

**职责**: 复用 CellStyle 对象，避免为每个单元格创建新样式。

**复用策略**:
- 预创建常用样式（表头、数据、汇总行）
- 按 (fontBold, fillColor, wrapText) 组合作为 key 缓存
- Workbook 销毁时一并清理

### 2.5 SyncZipOutputStream（同步压缩输出流）

**位置**: `psi-common/src/main/java/com/zeroone/star/project/components/excel/SyncZipOutputStream.java`

**职责**: 包装输出流，实现边写边压缩，无需临时文件。

```java
public class SyncZipOutputStream extends OutputStream {
    public SyncZipOutputStream(OutputStream out, boolean gzip);
    // 写入时自动压缩，关闭时自动刷新
}
```

## 3. EasyExcelComponent 重构

**改动点**:

1. **新增方法** `exportStreaming()` - 流式导出入口
2. **保留原有方法** `export()` - 兼容小数据量场景
3. **集成 CellStylePool** - 替换现有的每单元格创建样式逻辑
4. **集成 SyncZipOutputStream** - 可选的 GZIP 压缩支持

## 4. 现有导出服务改造

### 4.1 BreServiceImpl 改造

**改动点**:
- `exportBre()`: 改为使用 `StreamingExcelExporter` + 游标分页
- `exportBreInfo()`: 集成压缩输出流，ZIP 内每个 Excel 单独压缩

### 4.2 其他导出点

后续逐步改造以下模块的导出功能：
- `psi-reportmanagement` 下各 Controller
- `psi-finance` 下各 Controller
- `psi-fund` 下各 Controller

## 5. 性能监控埋点

**新增监控指标**:
- 导出耗时
- 导出数据量（行数）
- 峰值内存占用（通过 `Runtime.memory()`）
- 压缩率（原始大小 vs 压缩后）

**实现方式**: 在 `StreamingExcelExporter` 内部记录，可通过日志输出或封装为 `ExportResult` 返回。

## 6. 文件结构

```
psi-common/src/main/java/com/zeroone/star/project/components/
├── easyexcel/                          # 现有目录
│   ├── EasyExcelComponent.java         # [重构] 增加流式导出
│   ├── CellStylePool.java              # [新增] 样式池
│   ├── CursorPageQuery.java            # [新增] 游标分页接口
│   ├── MybatisCursorPageQuery.java     # [新增] MyBatis游标分页实现
│   ├── StreamingExcelExporter.java      # [新增] 流式导出器
│   └── SyncZipOutputStream.java        # [新增] 同步压缩输出流
```

## 7. 验证方案

1. **单元测试**: 为 `StreamingExcelExporter`、`CellStylePool` 编写 JUnit 测试
2. **大数据量测试**: 导出 50万+ 行数据，监控内存无溢出
3. **对比测试**: 优化前后导出耗时、内存峰值对比
4. **压缩验证**: 确认压缩后的 ZIP 文件可正常解压

## 8. 风险与限制

- 游标分页依赖自增主键 ID，不适用于无主键表
- GZIP 压缩会略微增加 CPU 开销，建议仅对超大文件启用
- 样式池需确保线程安全（Workbook 级）

## 状态

- [x] CursorPageQuery 接口
- [x] CellStylePool 样式池
- [x] SyncZipOutputStream 同步压缩输出流
- [x] ExportResult 导出结果封装
- [x] MybatisCursorPageQuery 实现
- [x] StreamingExcelExporter 流式导出器
- [x] EasyExcelComponent 重构
- [x] BreServiceImpl 导出方法改造
- [x] 单元测试
