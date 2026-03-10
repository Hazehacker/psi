# reportButtonTable 组件说明

一个基于 Element Plus 的报表表格组件，内置选择列、操作列（详情/删除/日志下拉）、分页、页脚汇总与自定义日志下拉展示能力。支持受控分页与外部汇总数据渲染。

## 基本使用

```vue
<template>
  <ReportTable
    title="销售单据"
    :columns="columns"
    :data="rows"
    :total="total"
    :loading="loading"
    :summary-data="summaryData"
    :row-logs="getRowLogs"
    :current-page="currentPage"
    :page-size="pageSize"
    @page-change="onPageChange"
    @view="onView"
    @delete="onDelete"
    @selection-change="onSelectionChange"
  />
</template>
```

## Props

- `title?: string` 表格标题，默认“报表”。
- `columns: Column[]` 列定义。
  - `prop: string` 字段键
  - `label: string` 标题
  - `width?: string`/`minWidth?: string`
  - `sortable?: boolean`
  - `align?: 'left'|'center'|'right'`
  - `slot?: string` 使用具名插槽自定义单元格
- `data: any[]` 原始数据数组。
- `total: number` 数据总条数。
- `loading?: boolean` 加载状态。
- `showSelection?: boolean` 是否显示选择列，默认 `true`。
- `showOperations?: boolean` 是否显示操作列，默认 `true`。
- `currentPage?: number` 当前页码（受控），默认 `1`。
- `pageSize?: number` 每页条数（受控），默认 `30`。
- `summaryData?: { label: string; value: string|number }[]` 页脚右侧汇总数据，按“label:value | …”渲染。
- `rowLogs?: (row:any)=>{time:string;user:string;action:string}[]` 行级日志数据提供者，用于下拉面板展示。

## Emits

- `view(row)` 点击“详情”。
- `delete(rows)` 点击“删除”，若选中多行则批量删除。
- `edit(row)` 预留的编辑事件。
- `search(keyword, dateRange)`/`reset`/`export`/`refresh` 预留。
- `sort-change(sort)` 列排序变化。
- `selection-change(selection)` 选择变化。
- `page-change(page, size)` 分页变化（当 `pageSize` 改变时会把页码重置为 1）。

## 插槽 Slots

- `footer-right` 页脚右侧自定义区域（默认渲染 `summaryData`）。
- 动态列 `slot`：当列的 `slot` 被设置时，可通过相同名称的具名插槽自定义单元格内容。

## 分页说明

组件内部对传入的 `data` 做前端 `slice` 分页，页码与页大小受 `currentPage`、`pageSize` 控制。当页大小变化时，组件会发出 `page-change(1, size)` 并把内部页码重置为 1。

如需后端分页：在父组件监听 `page-change`，请求新页数据并更新 `data`/`total` 即可。

## 汇总与总条数

- 左侧：`<` 分页 `>`、页码跳转、每页条数、`[共 X 条]`。
- 右侧：`summaryData` 以 `label:value | ...` 的行内形式展示。

## 操作列（按钮样式）

操作列提供“详情/删除/下拉”分段按钮样式，默认白底、蓝/红文字，悬浮有轻微背景色变化。可在 `reportButtonTable.vue` 的样式段内根据设计规范调整配色与尺寸。

## 行日志下拉

通过传入 `rowLogs(row)` 返回形如 `{ time,user,action }[]` 的数组，点击下拉按钮后展示该行的操作日志。结合全局状态（例如 Pinia）可实现跨页面实时更新日志的效果。

## 自定义样式

组件已对表头、边框、滚动条、分页与按钮做了基础样式优化。若需覆盖，请在父级使用 `:deep()` 或自定义类名进行拓展。

## 常见问题

- 在模板标签属性中不要写行内注释（例如 `title="销售单据" <!-- 注释 -->`），会被解析为非法属性从而引起类型错误。应将注释放在行上方或行尾外部。

## 示例见

- `src/views/sample/reportButtonTable/reportButtonTable.vue`
