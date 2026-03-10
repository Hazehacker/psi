<template>
  <div class="sales-book-form">
    <!-- 顶部操作栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <!-- 左上角搜索组件 -->
        <GoodSearchForm
          :model-value="searchModel"
          @update:model-value="(val) => Object.assign(searchModel, val)"
          class="mr-8"
          @search="onGoodSearch"
          ref="goodSearchRef"
        />
      </div>
      <div class="toolbar-right">
        <div class="btn-join">
          <!-- 批量操作按钮组：当有选中项时显示 -->
          <template v-if="selected.length > 0">
            <el-popover
              class="btnGroupPopover"
              placement="bottom"
              popper-class="blockPopover"
              trigger="click"
            >
              <template #reference>
                <el-button type="info">操作</el-button>
              </template>
              <ul>
                <li @click="onBatchExamine(1)">审核</li>
                <li @click="onBatchExamine(0)">反审核</li>
              </ul>
            </el-popover>
            <el-button @click="onBatchDelete" type="info">删除</el-button>
          </template>

          <!-- 自定义批量按钮 -->
          <el-button type="warning" @click="openBatchDialog">批量</el-button>

          <el-button :loading="refreshing" type="primary" @click="onRefresh">刷新</el-button>
        </div>
      </div>
    </div>

    <!-- 批量操作弹窗 -->
    <el-dialog title="批量操作" v-model="batchDialogVisible" width="600px" :show-close="true">
      <div class="batch-content">
        <el-tabs v-model="activeTab" class="batch-tabs">
          <el-tab-pane label="导入数据" name="import">
            <div class="tab-content">
              <div class="tips-list">
                <div class="tip-item">
                  <span class="tip-number">1.</span>
                  <span class="tip-text">该功能适用于批量导入数据。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">2.</span>
                  <span class="tip-text">您需要下载数据模板后使用Excel录入数据。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">3.</span>
                  <span class="tip-text">录入数据时，请勿修改首行数据标题以及排序。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">4.</span>
                  <span class="tip-text">请查阅使用文档获取字段格式内容以及相关导入须知。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">5.</span>
                  <span class="tip-text">点击下方上传模板，选择您编辑好的模板文件即可。</span>
                </div>
              </div>
              <div class="button-group">
                <el-button class="download-btn" @click="downloadTemplate">下载模板</el-button>
                <el-upload
                  ref="uploadRef"
                  :auto-upload="false"
                  :show-file-list="false"
                  accept=".xlsx,.xls"
                  :on-change="handleFileChange"
                  :disabled="uploading"
                  style="display: inline-block"
                >
                  <el-button type="primary" class="upload-btn" :loading="uploading">
                    {{ uploading ? '上传中...' : '上传模板' }}
                  </el-button>
                </el-upload>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="导出数据" name="export">
            <div class="tab-content">
              <div class="export-content">
                <el-button type="primary" class="export-btn" @click="exportSimpleReport"
                  >导出简单报表</el-button
                >
                <el-button type="success" class="export-btn" @click="exportDetailReport"
                  >导出详细报表</el-button
                >
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-dialog>

    <!-- 表格 + 固定横向滚动条 + 状态栏 -->
    <div class="table-section">
      <!-- 表格滚动容器 -->
      <div ref="tableWrapper" class="table-wrapper">
        <el-table
          ref="tableRef"
          :data="pagedData"
          border
          style="width: 100%"
          :header-cell-style="{ background: '#fafafa' }"
          @selection-change="handleSelectionChange"
          v-loading="loading"
        >
          <!-- 表格列定义保持不变 -->
          <el-table-column fixed="left" type="selection" width="48" align="center" />

          <!-- 动态列 -->
          <el-table-column
            v-for="col in columns"
            :key="col.prop"
            :prop="col.prop"
            :label="col.label"
            :width="col.width"
            align="center"
            header-align="center"
            :show-overflow-tooltip="true"
          >
            <template #header="{ column }">
              <div class="header-sortable">
                <span class="col-title">{{ column.label }}</span>
                <div v-if="col.sortable" class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === col.prop && sortOrder === 'asc' }"
                    title="升序"
                    @click.stop="sortTable(col.prop, 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === col.prop && sortOrder === 'desc' }"
                    title="降序"
                    @click.stop="sortTable(col.prop, 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
          </el-table-column>

          <!-- 操作列 -->
          <el-table-column fixed="right" label="相关操作" width="170" align="center">
            <template #default="scope">
              <div class="op-row">
                <el-button size="small" class="op-btn op-gray" @click="openDetail(scope.row)"
                  >详情</el-button
                >
                <el-button size="small" class="op-btn op-gray" @click="onDelete(scope.row)"
                  >删除</el-button
                >
                <el-dropdown trigger="click">
                  <el-button size="small" class="op-btn op-gray op-more">
                    <el-icon><ArrowDown /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu class="info-dropdown">
                      <el-dropdown-item>单据号：{{ scope.row.number }}</el-dropdown-item>
                      <el-dropdown-item>客户：{{ scope.row.customer }}</el-dropdown-item>
                      <el-dropdown-item
                        >金额：{{ scope.row.total?.toLocaleString() }}</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 固定横向拖动条 -->
      <div ref="xScroll" class="x-scroll scrollbar">
        <div ref="xInner" class="x-scroll-inner"></div>
      </div>

      <!-- 底部状态栏 -->
      <div class="status-bar">
        <div class="status-left">
          <el-pagination
            background
            layout="prev, pager, next, sizes, jumper, total"
            :total="totalCount"
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 30, 50, 100]"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
        <div class="status-right">
          <span>总单据金额：{{ totalAmount.toLocaleString() }}</span>
          <span class="ml-24">总实际金额：{{ totalRealAmount.toLocaleString() }}</span>
        </div>
      </div>
    </div>

    <SalesBookDetailDialog
      v-model:visible="detailVisible"
      :record="currentRow"
      @save="handleDetailSave"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import SalesBookDetailDialog from './SalesBookDetailDialog.vue'
import GoodSearchForm from '@/components/goodSearchConpoent/GoodSearchForm.vue'

// 导入API
import salesBookApi, {
  type SaleOrderData,
  type SaleOrderQuery,
  type SaleOrderVerifyRequest
} from '@/apis/sales/booking'

const detailVisible = ref(false)
const currentRow = ref<SaleOrderData | null>(null)

// 加载状态
const loading = ref(false)
const refreshing = ref(false)

// 批量弹窗相关
const batchDialogVisible = ref(false)
const activeTab = ref<'import' | 'export'>('import')
const uploading = ref(false)
const uploadRef = ref()

const openDetail = (row: SaleOrderData) => {
  currentRow.value = row
  detailVisible.value = true
}

const handleDetailSave = (payload: any) => {
  ElMessage.success('详情已保存')
  detailVisible.value = false
  loadTableData()
}

// 表格数据
const tableData = ref<SaleOrderData[]>([])

// 分页数据
const totalCount = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const columns = [
  { prop: 'frame', label: '所属组织', width: 120 },
  { prop: 'customer', label: '客户', width: 120 },
  { prop: 'time', label: '单据时间', width: 130, sortable: true },
  { prop: 'number', label: '单据编号', width: 200, sortable: true },
  { prop: 'total', label: '单据金额', width: 130, sortable: true },
  { prop: 'actual', label: '实际金额', width: 130, sortable: true },
  { prop: 'arrival', label: '到货日期', width: 130 },
  { prop: 'people', label: '关联人员', width: 120 },
  { prop: 'examine', label: '审核状态', width: 100 },
  { prop: 'state', label: '出库状态', width: 100 },
  { prop: 'user', label: '制单人', width: 100 },
  { prop: 'data', label: '备注信息', width: 180 }
]

// 审核状态映射
const examineMap: Record<number, string> = {
  0: '未审核',
  1: '已审核'
}

// 出库状态映射
const stateMap: Record<number, string> = {
  0: '未出库',
  1: '部分出库',
  2: '已出库',
  3: '关闭'
}

/* 选择 / 排序 */
const selected = ref<SaleOrderData[]>([])
const handleSelectionChange = (rows: SaleOrderData[]) => {
  selected.value = rows
}

const sortKey = ref<string>('')
const sortOrder = ref<'asc' | 'desc' | ''>('')
const sortTable = (key: string, order: 'asc' | 'desc') => {
  sortKey.value = key
  sortOrder.value = order
  tableData.value.sort((a, b) => {
    const va = a[key as keyof SaleOrderData] as any
    const vb = b[key as keyof SaleOrderData] as any
    if (order === 'asc') return va > vb ? 1 : va < vb ? -1 : 0
    return va < vb ? 1 : va > vb ? -1 : 0
  })
}

/* 合计 */
const totalAmount = computed(() => tableData.value.reduce((s, r) => s + (r.total || 0), 0))
const totalRealAmount = computed(() => tableData.value.reduce((s, r) => s + (r.actual || 0), 0))

// 分页数据
const pagedData = computed(() => {
  const s = (currentPage.value - 1) * pageSize.value
  return tableData.value.slice(s, s + pageSize.value)
})

/** ---------------- 搜索组件对接 ---------------- */
type SearchFormData = {
  goods?: string
  number?: string
  supplier?: any
  people?: any
  user?: any
  startTime?: string
  endTime?: string
  startArrival?: string
  endArrival?: string
  examine?: number | string
  state?: number | string
  data?: string
  [k: string]: any
}

const searchModel = reactive<SearchFormData>({})
const goodSearchRef = ref()

// 加载表格数据
const loadTableData = async (queryParams?: SaleOrderQuery) => {
  try {
    loading.value = true
    const params: SaleOrderQuery = {
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      ...queryParams
    }

    const response = await salesBookApi.getSaleOrdersList(params)
    if (response.code === 10000 && response.data) {
      tableData.value = response.data.rows || []
      totalCount.value = response.data.total || 0

      // 处理状态显示
      tableData.value.forEach((item) => {
        if (item.examine !== undefined) {
          ;(item as any).examineText = examineMap[item.examine] || '未知'
        }
        if (item.state !== undefined) {
          ;(item as any).stateText = stateMap[item.state] || '未知'
        }
      })
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const onGoodSearch = (payload: SearchFormData) => {
  const queryParams: SaleOrderQuery = {
    pageIndex: 1,
    pageSize: pageSize.value
  }

  if (payload.number) queryParams.number = payload.number
  if (payload.customer) queryParams.customer = payload.customer
  if (payload.startTime) queryParams.startTime = payload.startTime
  if (payload.endTime) queryParams.endTime = payload.endTime
  if (payload.examine) queryParams.examine = Number(payload.examine)
  if (payload.state) queryParams.state = Number(payload.state)
  if (payload.user) queryParams.user = payload.user
  if (payload.data) queryParams.data = payload.data

  currentPage.value = 1
  loadTableData(queryParams)
}

/** ---------------- 批量操作 ---------------- */
const openBatchDialog = () => {
  batchDialogVisible.value = true
  activeTab.value = 'import'
}

// 下载模板
const downloadTemplate = () => {
  ElMessage.info('模板下载功能开发中')
  // 这里可以调用后端下载模板接口或生成前端模板
}

// 处理文件上传
const handleFileChange = async (file: any) => {
  if (!file.raw) return

  try {
    uploading.value = true
    const response = await salesBookApi.importSaleOrders(file.raw)
    if (response.code === 10000) {
      ElMessage.success('导入成功')
      batchDialogVisible.value = false
      loadTableData()
    } else {
      ElMessage.error(response.message || '导入失败')
    }
  } catch (error) {
    console.error('导入失败:', error)
    ElMessage.error('导入失败')
  } finally {
    uploading.value = false
  }
}

// 导出简单报表
const exportSimpleReport = async () => {
  try {
    // 获取选中的订单编号
    const billNos = selected.value.map((row) => row.number).filter(Boolean) as string[]

    if (billNos.length === 0) {
      ElMessage.warning('请先选择要导出的订单')
      return
    }

    const response = await salesBookApi.exportSimpleReport(billNos)

    // 创建下载链接
    const blob = new Blob([response.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `销售订单简单报表_${new Date().getTime()}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)

    ElMessage.success('导出成功')
    batchDialogVisible.value = false
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}

// 导出详细报表
const exportDetailReport = async () => {
  try {
    // 获取选中的订单编号
    const billNos = selected.value.map((row) => row.number).filter(Boolean) as string[]

    if (billNos.length === 0) {
      ElMessage.warning('请先选择要导出的订单')
      return
    }

    const response = await salesBookApi.exportDetailReport(billNos)

    // 创建下载链接
    const blob = new Blob([response.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `销售订单详细报表_${new Date().getTime()}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)

    ElMessage.success('导出成功')
    batchDialogVisible.value = false
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}

const onRefresh = () => {
  if (refreshing.value) return
  refreshing.value = true
  loadTableData()
    .then(() => {
      ElMessage.success('已刷新')
    })
    .catch(() => {
      // 错误处理
    })
    .finally(() => {
      refreshing.value = false
    })
}

/* 删除 */
const onDelete = (row: SaleOrderData) => {
  ElMessageBox.confirm(`确定删除单据【${row.number}】吗？`, '提示', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消'
  })
    .then(async () => {
      if (!row.id) {
        ElMessage.error('单据ID不存在')
        return
      }
      try {
        const response = await salesBookApi.deleteSaleOrders([row.id])
        if (response.code === 10000) {
          ElMessage.success('删除成功')
          loadTableData()
        } else {
          ElMessage.error(response.message || '删除失败')
        }
      } catch (error) {
        console.error('删除失败:', error)
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

/* 批量审核/反审核 */
const onBatchExamine = async (type: 1 | 0) => {
  if (selected.value.length === 0) {
    ElMessage.warning('请选择要操作的单据')
    return
  }

  const validRows = selected.value.filter((row) => row.id)
  if (validRows.length === 0) {
    ElMessage.warning('选中的单据没有有效的ID')
    return
  }

  const ids = validRows.map((row) => row.id!)

  try {
    const request: SaleOrderVerifyRequest = {
      ids,
      num: type
    }
    const response = await salesBookApi.verifySaleOrders(request)
    if (response.code === 10000) {
      ElMessage.success(`操作成功！共处理 ${validRows.length} 条单据`)
      selected.value = []
      if (tableRef.value) {
        tableRef.value.clearSelection()
      }
      loadTableData()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

/* 批量删除 */
const onBatchDelete = () => {
  if (selected.value.length === 0) {
    ElMessage.warning('未选择要删除的数据！')
    return
  }

  const validRows = selected.value.filter((row) => row.id)
  if (validRows.length === 0) {
    ElMessage.warning('选中的单据没有有效的ID')
    return
  }

  const ids = validRows.map((row) => row.id!)

  ElMessageBox.confirm(`确定删除选中的 ${validRows.length} 条单据吗？`, '提示', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消'
  })
    .then(async () => {
      try {
        const response = await salesBookApi.deleteSaleOrders(ids)
        if (response.code === 10000) {
          ElMessage.success(`删除成功！共删除 ${validRows.length} 条单据`)
          selected.value = []
          if (tableRef.value) {
            tableRef.value.clearSelection()
          }
          loadTableData()
        } else {
          ElMessage.error(response.message || '删除失败')
        }
      } catch (error) {
        console.error('删除失败:', error)
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

// 分页处理
const handleSizeChange = (newSize: number) => {
  pageSize.value = newSize
  currentPage.value = 1
  loadTableData()
}

const handleCurrentChange = (newPage: number) => {
  currentPage.value = newPage
  loadTableData()
}

// 表格滚动条相关代码保持不变...
const tableRef = ref()
const tableWrapper = ref<HTMLElement | null>(null)
const bodyWrapper = ref<HTMLElement | null>(null)
const headerWrapper = ref<HTMLElement | null>(null)
const xScroll = ref<HTMLElement | null>(null)
const xInner = ref<HTMLElement | null>(null)
let ro: ResizeObserver | null = null

const setWidths = () => {
  if (!bodyWrapper.value || !xInner.value) return
  xInner.value.style.width = bodyWrapper.value.scrollWidth + 'px'
}

const bodyScroll = () => {
  if (!xScroll.value || !bodyWrapper.value) return
  if (xScroll.value.scrollLeft !== bodyWrapper.value.scrollLeft) {
    xScroll.value.scrollLeft = bodyWrapper.value.scrollLeft
  }
  if (headerWrapper.value) headerWrapper.value.scrollLeft = bodyWrapper.value.scrollLeft
}

const xScrollScroll = () => {
  if (!xScroll.value || !bodyWrapper.value) return
  if (bodyWrapper.value.scrollLeft !== xScroll.value.scrollLeft) {
    bodyWrapper.value.scrollLeft = xScroll.value.scrollLeft
    if (headerWrapper.value) headerWrapper.value.scrollLeft = xScroll.value.scrollLeft
  }
}

onMounted(async () => {
  loadTableData()

  await nextTick()
  const tableEl = (tableRef.value?.$el ?? null) as HTMLElement | null
  bodyWrapper.value = tableEl?.querySelector('.el-table__body-wrapper') as HTMLElement | null
  headerWrapper.value = tableEl?.querySelector('.el-table__header-wrapper') as HTMLElement | null

  if (bodyWrapper.value) {
    bodyWrapper.value.addEventListener('scroll', bodyScroll, { passive: true })
  }
  if (xScroll.value) {
    xScroll.value.addEventListener('scroll', xScrollScroll, { passive: true })
  }
  setWidths()
  if (bodyWrapper.value) {
    ro = new ResizeObserver(() => setWidths())
    ro.observe(bodyWrapper.value)
  }
})

onBeforeUnmount(() => {
  if (bodyWrapper.value) bodyWrapper.value.removeEventListener('scroll', bodyScroll)
  if (xScroll.value) xScroll.value.removeEventListener('scroll', xScrollScroll)
  if (ro && bodyWrapper.value) ro.unobserve(bodyWrapper.value)
  ro = null
})
</script>

<style scoped>
/* 批量按钮样式 */
.btn-join {
  display: inline-flex;
  align-items: center;
}

.btn-join > * + * {
  margin-left: -1px;
}

.btn-join :deep(.el-button) {
  border-radius: 0;
}
.btn-join :deep(.el-button:first-child) {
  border-top-left-radius: 4px;
  border-bottom-left-radius: 4px;
}
.btn-join :deep(.el-button:last-child) {
  border-top-right-radius: 4px;
  border-bottom-right-radius: 4px;
}

:root {
  --status-h: 20px;
}

.sales-book-form {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: #fff;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
}

.table-section {
  flex: 1 1 auto;
  min-height: 0;
  display: flex;
  flex-direction: column;
  position: relative;
}
.table-wrapper {
  flex: 1 1 auto;
  min-height: 0;
  overflow-y: auto;
  overflow-x: hidden;
  padding-bottom: 14px;
  border-bottom: 1px solid #ebeef5;
}

.table-wrapper :deep(.el-table__body-wrapper) {
  overflow-x: hidden !important;
}
.table-wrapper :deep(.el-table__header-wrapper) {
  overflow: hidden !important;
}

.x-scroll {
  position: absolute;
  left: 0;
  right: 0;
  bottom: var(--status-h);
  z-index: 6;
  height: 12px;
  overflow-x: auto;
  overflow-y: hidden;
  background: #f0f2f5;
  border-top: 1px solid #ebeef5;
}
.x-scroll-inner {
  height: 1px;
}

.scrollbar::-webkit-scrollbar,
.table-wrapper::-webkit-scrollbar {
  height: 12px;
  width: 12px;
}
.scrollbar::-webkit-scrollbar-track,
.table-wrapper::-webkit-scrollbar-track {
  background: #f0f2f5;
  border-radius: 6px;
}
.scrollbar::-webkit-scrollbar-thumb,
.table-wrapper::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 6px;
  border: 2px solid #f0f2f5;
}
.scrollbar::-webkit-scrollbar-thumb:hover,
.table-wrapper::-webkit-scrollbar-thumb:hover {
  background: #a8abb2;
}

.header-sortable {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
.col-title {
  white-space: nowrap;
}
.sort-vertical {
  display: inline-flex;
  flex-direction: column;
  margin-left: 4px;
  line-height: 10px;
}
.sort-vertical .el-icon {
  font-size: 14px;
  color: #c0c4cc;
  cursor: pointer;
}
.sort-vertical .el-icon.active {
  color: #409eff;
}

.op-row {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.op-btn {
  height: 28px;
  line-height: 26px;
  background: #fff;
  color: #606266;
  border: 1px solid #dcdfe6;
  border-radius: 0;
  padding: 0 10px;
  transition: all 0.15s ease;
}

.op-row > * + * {
  margin-left: -1px;
}
.op-btn + .op-btn {
  margin-left: -1px;
}

.op-row > :first-child .op-btn,
.op-row > :first-child.op-btn {
  border-top-left-radius: 4px;
  border-bottom-left-radius: 4px;
}

:deep(.op-row .el-dropdown .el-button) {
  border-radius: 0 !important;
}

:deep(.op-row .el-dropdown:last-child .el-button) {
  border-top-right-radius: 4px !important;
  border-bottom-right-radius: 4px !important;
}

.op-btn:hover,
.op-btn:focus {
  border-color: #409eff;
  color: #409eff;
}
:deep(.op-row .el-dropdown .el-button:hover),
:deep(.op-row .el-dropdown .el-button:focus) {
  border-color: #409eff;
  color: #409eff;
}

.info-dropdown {
  pointer-events: none;
}

.status-bar {
  position: sticky;
  bottom: 0;
  z-index: 5;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #f7f8fa;
  border-top: 1px solid #ebeef5;
  height: var(--status-h);
  padding: 4px 10px;
}
.status-right {
  font-size: 13px;
  color: #606266;
}
.ml-24 {
  margin-left: 24px;
}

.mr-8 {
  margin-right: 8px;
}

/* 批量弹窗样式 */
.batch-content {
  padding: 0;
}

.batch-tabs {
  margin-top: 0;
}

.tab-content {
  padding: 20px 0;
}

.tips-list {
  margin-bottom: 30px;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
  line-height: 1.5;
}

.tip-number {
  color: #409eff;
  font-weight: 600;
  margin-right: 8px;
  min-width: 20px;
}

.tip-text {
  color: #333;
  font-size: 14px;
  flex: 1;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 20px;
}

.download-btn {
  background-color: #f5f5f5;
  border-color: #d9d9d9;
  color: #333;
  min-width: 100px;
}

.upload-btn {
  min-width: 100px;
}

.export-content {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  min-height: 200px;
}

.export-btn {
  min-width: 120px;
  height: 40px;
  font-size: 16px;
}

:deep(.el-tabs__header) {
  margin: 0 0 20px 0;
}

:deep(.el-tabs__nav-wrap::after) {
  background-color: #e4e7ed;
}

:deep(.el-tabs__active-bar) {
  background-color: #409eff;
}

:deep(.el-tabs__item.is-active) {
  color: #409eff;
}

:deep(.el-tabs__item) {
  font-size: 14px;
  font-weight: 500;
}
</style>
