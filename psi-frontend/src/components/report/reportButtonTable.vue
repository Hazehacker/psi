<!--
  ReportButtonTable 组件
  作用：
  - 可配置列的报表表格，带选择、排序、固定操作列与分页
  - 右侧插槽可扩展汇总/自定义内容；行操作日志通过 rowLogs 提供

  关键 Props：
  - columns: 列配置（prop/label/width/minWidth/sortable/align/slot/formatter）
  - data: 原始数据（组件内做前端分页）
  - total/currentPage/pageSize: 分页控制
  - showSelection/showOperations: 是否显示选择列/操作列
  - summaryData: 页脚汇总展示
  - rowLogs(row): 返回某行操作日志数组

  插槽：
  - 列插槽：在列配置中指定 column.slot，对应 <template #slotName>，参数 { row }
  - footer-right：页脚右侧自定义区域（默认展示 summaryData）

  事件：
  - search(keyword, dateRange) / reset() / export() / refresh()
  - view(row) / delete(rows) / edit(row)
  - sort-change(sort) / page-change(page, size) / selection-change(selection)
-->
<template>
  <div class="report-table-container">
    <!-- 表格主体 -->
    <div class="table-wrapper">
      <el-table
        :data="tableData"
        :border="true"
        :stripe="false"
        :highlight-current-row="false"
        v-loading="loading"
        @sort-change="handleSortChange"
        @selection-change="handleSelectionChange"
        style="width: 100%"
        :header-cell-style="{ backgroundColor: '#f5f7fa', color: '#606266' }"
        :max-height="600"
        :scrollbar-always-on="true"
      >
        <!-- 选择列：支持多选功能 -->
        <el-table-column v-if="showSelection" type="selection" width="55" align="center" />

        <!-- 数据列：动态渲染所有配置的列 -->
        <el-table-column
          v-for="column in columns"
          :key="column.prop"
          :prop="column.prop"
          :label="column.label"
          :width="column.width"
          :min-width="column.minWidth"
          :sortable="column.sortable"
          :align="column.align || 'left'"
          show-overflow-tooltip
        >
          <template #default="scope">
            <!-- 如果有自定义列插槽，使用插槽 -->
            <slot v-if="column.slot" :name="column.slot" :row="scope.row"></slot>
            <!-- 如果有格式化函数，使用格式化函数 -->
            <span v-else-if="column.formatter">
              {{ column.formatter(scope.row, column, scope.row[column.prop], scope.$index) }}
            </span>
            <!-- 默认显示 -->
            <span v-else>{{ scope.row[column.prop] }}</span>
          </template>
        </el-table-column>

        <!-- 操作列：固定的操作按钮区域 -->
        <el-table-column
          v-if="showOperations"
          label="相关操作"
          width="150"
          fixed="right"
          align="center"
        >
          <template #default="scope">
            <div class="operation-buttons">
              <div class="button-group">
                <!-- 详情按钮 -->
                <button class="detail-btn" @click="handleView(scope.row)">详情</button>
                <!-- 删除按钮 -->
                <button class="delete-btn" @click="handleDelete(scope.row)">删除</button>
                <!-- 操作日志下拉菜单 -->
                <el-dropdown trigger="click" class="dropdown-wrapper">
                  <button class="dropdown-btn">
                    <el-icon><arrow-down /></el-icon>
                  </button>
                  <template #dropdown>
                    <div class="logs-dropdown" @click.stop>
                      <div class="logs-list">
                        <!-- 渲染操作日志列表 -->
                        <div
                          v-for="(log, idx) in resolveRowLogs(scope.row)"
                          :key="idx"
                          class="log-item"
                        >
                          <div class="log-line">
                            <span class="time">{{ log.time }}</span>
                            <span class="sep"> - </span>
                            <span class="user">{{ log.user }}</span>
                            <span class="sep"> - </span>
                            <span class="action">{{ log.action }}</span>
                          </div>
                          <!-- 日志项之间的分隔线 -->
                          <div
                            class="divider"
                            v-if="idx < resolveRowLogs(scope.row).length - 1"
                          ></div>
                        </div>
                        <!-- 空状态提示 -->
                        <div v-if="resolveRowLogs(scope.row).length === 0" class="log-empty">
                          暂无记录
                        </div>
                      </div>
                    </div>
                  </template>
                </el-dropdown>
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 页脚/分页区域 -->
    <div class="report-footer">
      <div class="footer-left">
        <!-- 分页控件 -->
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 30, 50, 100]"
          :total="total"
          layout="prev, pager, next, jumper, sizes"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
        <!-- 总数据条数显示 -->
        <div class="total-info-box">
          <span class="total-text">共 {{ total }} 条</span>
        </div>
      </div>
      <div class="footer-right">
        <!-- 右侧插槽：用于放置汇总信息等 -->
        <slot name="footer-right">
          <!-- 汇总信息显示 -->
          <div class="summary-box" v-if="summaryData && summaryData.length">
            <div class="summary-content">
              <template v-for="(summary, index) in summaryData" :key="index">
                <span class="summary-item">
                  <span class="summary-label">{{ summary.label }}:</span>
                  <span class="summary-value">{{ summary.value }}</span>
                </span>
                <!-- 汇总项之间的分隔符 -->
                <span v-if="index < summaryData.length - 1" class="summary-divider"> | </span>
              </template>
            </div>
          </div>
        </slot>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
/**
 * ReportButtonTable：可配置列的报表表格，内置操作区与分页。
 * 主要 props：columns、data、total、currentPage、pageSize、rowLogs 等。
 * 主要事件：view/delete/edit/search/reset/export/refresh/sort-change/page-change/selection-change。
 */

// ========== 导入区域 ==========
import { ref, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'

// ========== 类型定义 ==========

/**
 * 表格列配置接口
 */
interface Column {
  prop: string // 字段名
  label: string // 显示标签
  width?: string // 列宽度
  minWidth?: string // 最小宽度
  sortable?: boolean // 是否可排序
  align?: 'left' | 'center' | 'right' // 对齐方式
  slot?: string // 自定义插槽名称
  formatter?: (row: any, column: any, cellValue: any, index: number) => string // 格式化函数
}

/**
 * 汇总信息项接口
 */
interface SummaryItem {
  label: string // 汇总项标签
  value: string | number // 汇总值
}

/**
 * 操作日志项接口
 */
interface LogItem {
  time: string // 操作时间
  user: string // 操作用户
  action: string // 操作内容
}

/**
 * 组件属性接口
 */
interface Props {
  title?: string // 表格标题
  columns: Column[] // 列配置数组
  data: any[] // 表格数据
  total: number // 数据总数
  loading?: boolean // 加载状态
  showSelection?: boolean // 是否显示选择列
  showOperations?: boolean // 是否显示操作列
  currentPage?: number // 当前页码
  pageSize?: number // 每页大小
  summaryData?: SummaryItem[] // 汇总数据
  rowLogs?: (row: any) => LogItem[] // 行日志数据获取函数
}

// ========== 属性定义和默认值 ==========
const props = withDefaults(defineProps<Props>(), {
  title: '报表',
  loading: false,
  showSelection: true, // 默认显示选择列
  showOperations: true, // 默认显示操作列
  currentPage: 1, // 默认第一页
  pageSize: 30, // 默认每页30条
  summaryData: () => [], // 默认空汇总数据
  rowLogs: () => [] // 默认空日志函数
})

// ========== 事件定义 ==========
const emit = defineEmits<{
  search: [keyword: string, dateRange: any] // 搜索事件
  reset: [] // 重置事件
  export: [] // 导出事件
  refresh: [] // 刷新事件
  view: [row: any] // 查看详情事件
  delete: [rows: any[]] // 删除事件（支持批量）
  edit: [row: any] // 编辑事件
  'sort-change': [sort: any] // 排序变化事件
  'page-change': [page: number, size: number] // 分页变化事件
  'selection-change': [selection: any[]] // 选择变化事件
}>()

// ========== 响应式数据 ==========
const searchKeyword = ref('') // 搜索关键词
const dateRange = ref([]) // 日期范围
const currentPage = ref(props.currentPage) // 当前页码
const pageSize = ref(props.pageSize) // 每页大小
const selectedRows = ref<any[]>([]) // 选中的行数据

// ========== 计算属性 ==========

/**
 * 表格数据计算属性
 * 实现前端分页功能
 */
const tableData = computed(() => {
  // 空数据检查
  if (!props.data || props.data.length === 0) return []

  // 计算分页数据
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return props.data.slice(start, end)
})

// ========== 方法定义 ==========

/**
 * 搜索处理
 */
const handleSearch = () => {
  emit('search', searchKeyword.value, dateRange.value)
}

/**
 * 重置搜索条件
 */
const handleReset = () => {
  searchKeyword.value = ''
  dateRange.value = []
  emit('reset')
}

/**
 * 导出处理
 */
const handleExport = () => {
  emit('export')
}

/**
 * 刷新数据
 */
const handleRefresh = () => {
  emit('refresh')
}

/**
 * 查看详情
 * @param row 行数据
 */
const handleView = (row: any) => {
  emit('view', row)
}

/**
 * 删除处理（支持单条和批量删除）
 * @param row 行数据（单条删除时使用）
 */
const handleDelete = (row: any) => {
  // 判断是单条删除还是批量删除
  const toDelete = selectedRows.value.length > 0 ? selectedRows.value : [row]

  ElMessageBox.confirm('您确定要删除选中数据吗?', '提示', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  })
    .then(() => {
      // 确认删除，触发删除事件
      emit('delete', toDelete)
    })
    .catch(() => {
      // 用户取消删除
    })
}

/**
 * 编辑处理
 * @param row 行数据
 */
const handleEdit = (row: any) => {
  emit('edit', row)
}

/**
 * 下拉菜单命令处理
 * @param command 命令名称
 * @param row 行数据
 */
const handleDropdownCommand = (command: string, row: any) => {
  if (command === 'delete') {
    handleDelete(row)
  } else if (command === 'edit') {
    handleEdit(row)
  }
}

/**
 * 解析行操作日志
 * @param row 行数据
 * @returns 日志数组
 */
const resolveRowLogs = (row: any) => {
  return props.rowLogs ? props.rowLogs(row) : []
}

/**
 * 选择变化处理
 * @param selection 选中的行数组
 */
const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection
  emit('selection-change', selection)
}

/**
 * 排序变化处理
 * @param sort 排序信息
 */
const handleSortChange = (sort: any) => {
  emit('sort-change', sort)
}

/**
 * 每页大小变化处理
 * @param size 新的每页大小
 */
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1 // 重置到第一页
  emit('page-change', 1, size)
}

/**
 * 当前页码变化处理
 * @param page 新的页码
 */
const handleCurrentChange = (page: number) => {
  currentPage.value = page
  emit('page-change', page, pageSize.value)
}

/**
 * 日期范围变化处理
 */
const handleDateChange = () => {
  handleSearch()
}

// ========== 监听器 ==========

// 监听当前页码属性变化
watch(
  () => props.currentPage,
  (newVal) => {
    currentPage.value = newVal
  }
)

// 监听每页大小属性变化
watch(
  () => props.pageSize,
  (newVal) => {
    pageSize.value = newVal
  }
)
</script>

<style scoped>
/* ========== 容器样式 ========== */
.report-table-container {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.table-wrapper {
  padding: 0;
  overflow: hidden;
  border-radius: 4px;
}

/* ========== 页脚样式 ========== */
.report-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 16px;
  border-top: 1px solid #ebeef5;
  background: #fafafa;
  border-radius: 0 0 4px 4px;
  min-height: 36px;
}

.footer-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.footer-right {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

/* ========== 总数据条数框样式 ========== */
.total-info-box {
  background: #f5f7fa;
  border: 1px solid #dcdfe6;
  border-radius: 3px;
  padding: 0 8px;
  font-size: 12px;
  color: #606266;
  white-space: nowrap;
  height: 20px;
  display: flex;
  align-items: center;
  line-height: 20px;
}

.total-text {
  font-weight: 500;
  font-size: 12px;
  line-height: 20px;
}

/* ========== 汇总信息框样式 ========== */
.summary-box {
  background: #f5f7fa;
  border: 1px solid #dcdfe6;
  border-radius: 3px;
  padding: 0 8px;
  font-size: 12px;
  height: 20px;
  display: flex;
  align-items: center;
  line-height: 20px;
}

.summary-content {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #606266;
  white-space: nowrap;
  gap: 0;
  line-height: 20px;
}

.summary-item {
  display: inline-flex;
  align-items: center;
  line-height: 20px;
}

.summary-label {
  color: #606266;
  font-weight: normal;
  margin-right: 2px;
  font-size: 12px;
  line-height: 20px;
}

.summary-value {
  color: #303133;
  font-weight: 500;
  font-size: 12px;
  line-height: 20px;
}

.summary-divider {
  margin: 0 4px;
  color: #dcdfe6;
  font-weight: normal;
  font-size: 12px;
  line-height: 20px;
}

/* ========== 表格样式优化 ========== */
:deep(.el-table) {
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

:deep(.el-table th) {
  background-color: #f5f7fa !important;
  color: #606266 !important;
  font-weight: 500;
}

:deep(.el-table td) {
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-table--border th) {
  border-right: 1px solid #ebeef5;
}

:deep(.el-table--border td) {
  border-right: 1px solid #ebeef5;
}

/* ========== 滚动条样式 ========== */
:deep(.el-table__body-wrapper) {
  scrollbar-width: thin;
  scrollbar-color: #c1c1c1 #f1f1f1;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-corner {
  background: #f1f1f1;
}

/* ========== 操作按钮样式 ========== */
.operation-buttons {
  display: flex;
  align-items: center;
  justify-content: center;
}

.button-group {
  display: flex;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
}

/* 详情按钮样式 */
.detail-btn {
  width: 50px;
  height: 28px;
  padding: 0;
  font-size: 12px;
  background-color: #fff;
  color: #409eff;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s;
  position: relative;
}

.detail-btn::after {
  content: '';
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  width: 1px;
  background-color: #dcdfe6;
}

.detail-btn:hover {
  background-color: #f5f7fa;
}

/* 删除按钮样式 */
.delete-btn {
  width: 50px;
  height: 28px;
  padding: 0;
  font-size: 12px;
  background-color: #fff;
  color: #f56c6c;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s;
  position: relative;
}

.delete-btn::after {
  content: '';
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  width: 1px;
  background-color: #dcdfe6;
}

.delete-btn:hover {
  background-color: #fef0f0;
}

/* 下拉按钮样式 */
.dropdown-wrapper {
  display: flex;
}

.dropdown-btn {
  width: 28px;
  height: 28px;
  padding: 0;
  font-size: 12px;
  background-color: #fff;
  color: #409eff;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s;
}

.dropdown-btn:hover {
  background-color: #f5f7fa;
}

:deep(.el-dropdown) {
  margin: 0;
}

:deep(.el-icon) {
  font-size: 12px;
}

/* ========== 日志下拉样式 ========== */
.logs-dropdown {
  width: 420px;
  max-height: 260px;
  padding: 12px;
  box-sizing: border-box;
}

.logs-list {
  max-height: 200px;
  overflow: auto;
}

.log-item {
  padding: 8px 4px;
}

.log-line {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #303133;
}

.log-line .time {
  width: 150px;
  color: #606266;
}

.log-line .user,
.log-line .action {
  color: #303133;
}

.log-line .sep {
  margin: 0 6px;
  color: #909399;
}

.divider {
  height: 1px;
  background-color: #ebeef5;
  margin-top: 8px;
}

.log-empty {
  text-align: center;
  color: #909399;
  padding: 16px 0;
}

/* ========== 分页样式优化 ========== */
:deep(.el-pagination) {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}

:deep(.el-pagination .el-pager) {
  display: flex;
  align-items: center;
  gap: 4px;
}

:deep(.el-pagination .el-pager li) {
  min-width: 20px;
  height: 20px;
  line-height: 20px;
  border-radius: 3px;
  margin: 0;
  font-size: 12px;
}

:deep(.el-pagination .btn-prev),
:deep(.el-pagination .btn-next) {
  width: 20px;
  height: 20px;
  line-height: 20px;
  border-radius: 3px;
  margin: 0;
  font-size: 12px;
}

:deep(.el-pagination .el-pagination__jump) {
  margin: 0;
  font-size: 12px;
}

:deep(.el-pagination .el-pagination__jump .el-input) {
  font-size: 12px;
}

:deep(.el-pagination .el-pagination__jump .el-input__inner) {
  font-size: 12px;
  height: 20px;
  line-height: 20px;
  padding: 0 4px;
}

:deep(.el-pagination .el-pagination__sizes) {
  margin: 0;
  font-size: 12px;
}

:deep(.el-pagination .el-pagination__sizes .el-select) {
  font-size: 12px;
}

:deep(.el-pagination .el-pagination__sizes .el-select .el-input__inner) {
  font-size: 12px;
  height: 20px;
  line-height: 20px;
  padding: 0 4px;
}

:deep(.el-pagination .el-pager li.is-active) {
  background-color: #409eff;
  color: #fff;
}

:deep(.el-pagination .el-pager li:hover) {
  color: #409eff;
}

:deep(.el-pagination .btn-prev:hover),
:deep(.el-pagination .btn-next:hover) {
  color: #409eff;
}

:deep(.el-pagination .el-pagination__sizes) {
  margin-right: 16px;
}

:deep(.el-pagination .el-pagination__jump) {
  margin-left: 16px;
}

/* ========== 响应式设计 ========== */
@media (max-width: 768px) {
  .report-footer {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .footer-right {
    justify-content: center;
  }

  .summary-content {
    flex-wrap: wrap;
    justify-content: center;
    gap: 4px;
  }
}
</style>
