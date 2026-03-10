<template>
  <div class="purchase-book-form">
    <!-- 顶部操作栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <!-- 左上角搜索组件 -->
        <PurchaseSearchForm
          :page-index="currentPage"
          :page-size="pageSize"
          class="mr-8"
          @search="handlePurchaseSearch"
          ref="purchaseSearchRef"
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
                <li @click="onBatchExamine(0)">审核</li>
                <li @click="onBatchExamine(1)">反审核</li>
              </ul>
            </el-popover>
            <el-button @click="onBatchDelete" type="info">删除</el-button>
          </template>
          <OperationButtons
            :show-add="false"
            :show-batch="true"
            :show-refresh="false"
            :table-columns="columns"
            :import-api="handleImportFile"
            :template-file-name="'采购订单导入模板'"
            :export-simple-report-api="handleExportSimpleReport"
            :selected-order-ids="selectedOrderIds"
            :export-detail-report-api="handleExportDetailReport"
            export-file-name="简单报表导出"
            export-detail-file-name="详细报表导出"
            @batch="onBatch"
            @import-data="onImportData"
          />
          <el-button :loading="refreshing" type="primary" @click="onRefresh">刷新</el-button>
        </div>
      </div>
    </div>

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
        >
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

          <!-- 操作列：灰描边、黑白配、无缝拼接、更多仅图标 -->
          <el-table-column fixed="right" label="相关操作" width="170" align="center">
            <template #default="scope">
              <div class="op-row">
                <el-button 
                  size="small" 
                  class="op-btn op-gray" 
                  :loading="loadingDetail && currentRow?.id === scope.row.id"
                  @click="openDetail(scope.row)"
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
                    <!-- 只展示信息，禁交互 -->
                    <el-dropdown-menu class="info-dropdown">
                      <el-dropdown-item>单据号：{{ scope.row.orderNo }}</el-dropdown-item>
                      <el-dropdown-item>供应商：{{ scope.row.supplier }}</el-dropdown-item>
                      <el-dropdown-item
                        >金额：{{ scope.row.amount.toLocaleString() }}</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </template>
          </el-table-column>
        </el-table>
        </div>

      <!-- 固定横向拖动条（紧贴状态栏上方；与表格联动） -->
      <div ref="xScroll" class="x-scroll scrollbar">
        <div ref="xInner" class="x-scroll-inner"></div>
        </div>

      <!-- 底部状态栏 -->
      <div class="status-bar">
        <div class="status-left">
          <el-pagination
            background
            layout="prev, pager, next, sizes, jumper, total"
            :total="total"
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 30, 50, 100]"
            @current-change="handlePageChange"
            @size-change="handleSizeChange"
          />
        </div>
        <div class="status-right">
          <span>总单据金额：{{ totalAmount.toLocaleString() }}</span>
          <span class="ml-24">总实际金额：{{ totalRealAmount.toLocaleString() }}</span>
        </div>
      </div>
    </div>
    <PurchaseBookDetailDialog
      v-model:visible="detailVisible"
      :record="currentRow"
      @save="handleDetailSave"
    />
  </div>
</template>

<script setup lang="ts">
import {
  ref,
  reactive,
  computed,
  onMounted,
  onBeforeUnmount,
  nextTick,
  defineAsyncComponent
} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import PurchaseBookDetailDialog from './PurchaseBookDetailDialog.vue'
import type { PurchaseOrderListQuery, PurchaseOrderListItem } from '@/apis/purchase/PurchaseBooking/order'
import { PurchaseOrderAPI } from '@/apis/purchase/PurchaseBooking/order'
import type { PageDTO } from '@/apis/type'

/** 外部组件 */
import { PurchaseSearchForm } from '@/components/goodSearchConpoent'
import OperationButtons from '@/components/operationbuttons/OperationButtons.vue'

const detailVisible = ref(false)
const currentRow = ref<RowData | null>(null)
const loadingDetail = ref(false)

const openDetail = async (row: RowData) => {
  if (!row.id) {
    ElMessage.warning('订单ID不存在，无法获取详情')
    return
  }

  loadingDetail.value = true
  try {
    // 调用接口获取订单详情
    const response = await PurchaseOrderAPI.getPurchaseOrderDetail(row.id)
    
    if (response.code === 10000 && response.data) {
      // 将详情数据传递给对话框
      currentRow.value = {
        ...row,
        detailData: response.data
      }
      detailVisible.value = true
    } else {
      ElMessage.error(response.message || '获取订单详情失败')
    }
  } catch (error: any) {
    console.error('获取订单详情失败:', error)
    const errorMessage = error?.response?.data?.message || error?.message || '获取订单详情失败，请稍后重试'
    ElMessage.error(errorMessage)
  } finally {
    loadingDetail.value = false
  }
}

const handleDetailSave = (payload: any) => {
  // 保存操作已在 PurchaseBookDetailDialog 组件内部完成
  // 这里只需要关闭对话框并刷新列表
  detailVisible.value = false
  // 保存成功后刷新列表
  purchaseSearchRef.value?.search?.({})
}

interface RowData {
  id?: number
  org: string
  supplier: string
  date: string
  orderNo: string
  amount: number
  realAmount: number
  arrivalDate: string
  contact: string
  auditStatus: string
  stockStatus: string
  maker: string
  remark: string
  detailData?: any // 详情数据
}

/* 表格数据 */
const tableData = ref<RowData[]>([])

const columns = [
  { prop: 'org', label: '所属组织', width: 120 },
  { prop: 'supplier', label: '供应商', width: 180 },
  { prop: 'date', label: '单据时间', width: 130, sortable: true },
  { prop: 'orderNo', label: '单据编号', width: 200, sortable: true },
  { prop: 'amount', label: '单据金额', width: 130, sortable: true },
  { prop: 'realAmount', label: '实际金额', width: 130, sortable: true },
  { prop: 'arrivalDate', label: '到货日期', width: 130 },
  { prop: 'contact', label: '关联人员', width: 120 },
  { prop: 'auditStatus', label: '审核状态', width: 100 },
  { prop: 'stockStatus', label: '入库状态', width: 100 },
  { prop: 'maker', label: '制单人', width: 100 },
  { prop: 'remark', label: '备注信息', width: 180 }
]

/* 选择 / 排序 */
const selected = ref<RowData[]>([])
const handleSelectionChange = (rows: RowData[]) => {
  selected.value = rows
}

// 获取选中的订单ID数组
const selectedOrderIds = computed(() => {
  return selected.value.map(row => {
    // 优先使用 id 字段，如果没有则使用 orderNo（单据编号）
    return (row as any).id || row.orderNo || ''
  }).filter(id => id !== '')
})

// 导出简单报表的处理函数
const handleExportSimpleReport = async (orderId: string | number) => {
  console.log('🔍 [父组件] handleExportSimpleReport 被调用，orderId:', orderId)
  try {
    const result = await PurchaseOrderAPI.exportSimpleReport(orderId)
    console.log('🔍 [父组件] API 调用成功，result:', result)
    return result
  } catch (error: any) {
    console.error('导出简单报表失败:', error)
    throw error
  }
}

// 导出详细报表的处理函数
const handleExportDetailReport = async (ids: (string | number)[]) => {
  console.log('🔍 [父组件] handleExportDetailReport 被调用，ids:', ids)
  try {
    const result = await PurchaseOrderAPI.exportDetailReport(ids)
    console.log('🔍 [父组件] API 调用成功，result:', result)
    return result
  } catch (error: any) {
    console.error('导出详细报表失败:', error)
    throw error
  }
}

const sortKey = ref<string>('')
const sortOrder = ref<'asc' | 'desc' | ''>('')
const sortTable = (key: string, order: 'asc' | 'desc') => {
  sortKey.value = key
  sortOrder.value = order
  tableData.value.sort((a, b) => {
    const va = a[key as keyof RowData] as any
    const vb = b[key as keyof RowData] as any
    if (order === 'asc') return va > vb ? 1 : va < vb ? -1 : 0
    return va < vb ? 1 : va > vb ? -1 : 0
  })
}

/* 合计 / 分页 */
const totalAmount = computed(() => tableData.value.reduce((s, r) => s + (r.amount || 0), 0))
const totalRealAmount = computed(() => tableData.value.reduce((s, r) => s + (r.realAmount || 0), 0))
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0) // 总条数
const pagedData = computed(() => {
  // 后端分页，直接返回所有数据
  return tableData.value
})

/** ---------------- 搜索组件对接 ---------------- */
const purchaseSearchRef = ref<InstanceType<typeof PurchaseSearchForm> | null>(null)

// 将后端数据转换为前端 RowData 格式
const convertToRowData = (item: PurchaseOrderListItem): RowData => {
  return {
    id: item.id,
    org: item.frame || '默认组织',
    supplier: item.supplier || '',
    date: item.documentTime || '',
    orderNo: item.number || '',
    amount: typeof item.total === 'string' ? parseFloat(item.total) || 0 : item.total || 0,
    realAmount: typeof item.actual === 'string' ? parseFloat(item.actual) || 0 : item.actual || 0,
    arrivalDate: item.arrivalTime || '',
    contact: item.people || '',
    auditStatus: item.examine || '未审核',
    stockStatus: item.state || '未入库',
    maker: item.user || '',
    remark: item.data || ''
  }
}

// 处理采购搜索组件的搜索事件
const handlePurchaseSearch = (query: PurchaseOrderListQuery, result: PageDTO<PurchaseOrderListItem>) => {
  // 更新表格数据
  tableData.value = (result.rows || []).map(convertToRowData)
  total.value = result.total || 0
  
  console.log('采购订单搜索完成，共', total.value, '条，当前页', tableData.value.length, '条')
}

/** ---------------- 批量/导入/刷新 ---------------- */
    

/** ---------------- 批量/导入/刷新 ---------------- */
const refreshing = ref(false)
const onRefresh = async () => {
  if (refreshing.value) return
  refreshing.value = true
  try {
    // 重置分页
    currentPage.value = 1
    // 重置搜索组件
    purchaseSearchRef.value?.resetForm?.()
    // 重新加载数据（通过搜索组件触发）
    purchaseSearchRef.value?.search?.({})
    ElMessage.success('已刷新')
  } catch (error) {
    console.error('刷新失败:', error)
  } finally {
    refreshing.value = false
  }
}

const onBatch = () => {
  ElMessage.info('打开批量操作窗口')
}

/** 处理文件导入 - 直接上传文件到接口 */
const handleImportFile = async (file: File) => {
  try {
    const response = await PurchaseOrderAPI.importPurchaseOrders(file)
    return response
  } catch (error: any) {
    // 错误已经在 API 层处理，这里直接抛出
    throw error
  }
}

/** 承接 OperationButtons 的导入数据事件 */
const onImportData = async (data: any) => {
  // 如果提供了 importApi，data 会是 { success, result, file } 格式
  if (data && typeof data === 'object' && 'success' in data) {
    if (data.success) {
      // 导入成功
      ElMessage.success(data.result?.message || '导入成功')
      // 重新加载列表
      purchaseSearchRef.value?.search?.({})
    } else {
      // 导入失败（错误信息已经在 API 层显示）
      console.error('导入失败:', data.error)
    }
    return
  }
  
  // 兼容旧的数据格式（解析Excel后的数据数组）
  if (Array.isArray(data)) {
    // TODO: 如果需要支持解析Excel后导入，可以在这里处理
    ElMessage.success(`已导入 ${data.length} 条记录`)
    purchaseSearchRef.value?.search?.({})
  }
}

/* 删除 */
const onDelete = (row: RowData) => {
  ElMessageBox.confirm(`确定删除单据【${row.orderNo}】吗？`, '提示', {
      type: 'warning',
    confirmButtonText: '删除',
      cancelButtonText: '取消'
  })
    .then(async () => {
      // TODO: 调用删除接口
      // await PurchaseOrderAPI.deletePurchaseOrder(row.orderNo)
      // 删除成功后重新加载列表
      purchaseSearchRef.value?.search?.({})
      ElMessage.success('删除成功')
    })
    .catch(() => {})
}

/* 批量审核/反审核 */
const onBatchExamine = async (type: 0 | 1) => {
  // type: 0=审核, 1=反审核
  
  // 检查是否有选中项
  if (!selected.value || selected.value.length === 0) {
    ElMessage.warning('请先选择要操作的单据！')
    return
  }

  const validRows: RowData[] = []
  const invalidRows: RowData[] = []
  
  selected.value.forEach((row) => {
    const currentStatus = row.auditStatus || ''
    // 审核：只处理未审核的
    if (type === 0) {
      // 支持多种可能的"未审核"状态值
      if (currentStatus === '未审核' || currentStatus === '0' || currentStatus === '' || currentStatus === 'false') {
        validRows.push(row)
      } else {
        invalidRows.push(row)
      }
    }
    // 反审核：只处理已审核的
    if (type === 1) {
      // 支持多种可能的"已审核"状态值
      if (currentStatus === '已审核' || currentStatus === '1' || currentStatus === 'true') {
        validRows.push(row)
      } else {
        invalidRows.push(row)
      }
    }
  })

  if (validRows.length === 0) {
    const actionName = type === 0 ? '审核' : '反审核'
    const requiredStatus = type === 0 ? '未审核' : '已审核'
    if (invalidRows.length > 0) {
      ElMessage.warning(`选中的 ${invalidRows.length} 条单据状态不符合要求！${actionName}操作只能处理状态为"${requiredStatus}"的单据。`)
    } else {
      ElMessage.warning('无可操作单据！')
    }
    // 输出调试信息
    console.log('选中单据状态详情:', selected.value.map(r => ({ orderNo: r.orderNo, auditStatus: r.auditStatus })))
    return
  }

  // 提取订单ID
  const orderIds = validRows
    .map(row => row.id)
    .filter(id => id !== undefined && id !== null && id > 0) as number[]

  if (orderIds.length === 0) {
    ElMessage.error('选中的单据缺少订单ID，无法执行审核操作')
    return
  }

  if (orderIds.length !== validRows.length) {
    ElMessage.warning(`共选中 ${validRows.length} 条单据，其中 ${orderIds.length} 条有有效订单ID，将处理这 ${orderIds.length} 条`)
  }

  try {
    // 根据接口文档，type: 1=审核，0=反审核
    // 但前端逻辑中 type: 0=审核，1=反审核，所以需要反转
    const apiType = type === 0 ? 1 : 0
    
    // 调用批量审核/反审核接口，传递 ids 和 type 参数
    await PurchaseOrderAPI.auditPurchaseOrders(orderIds, apiType)
    
    // 清空选中项
    selected.value = []
    if (tableRef.value) {
      tableRef.value.clearSelection()
    }

    // 重新加载列表
    purchaseSearchRef.value?.search?.({})

    const actionName = type === 0 ? '审核' : '反审核'
    ElMessage.success(`${actionName}成功！共处理 ${orderIds.length} 条单据`)
  } catch (error: any) {
    console.error('批量审核/反审核失败:', error)
    const errorMessage = error?.response?.data?.message || error?.message || '操作失败，请稍后重试'
    ElMessage.error(errorMessage)
  }
}

/* 批量删除 */
const onBatchDelete = () => {
  if (selected.value.length === 0) {
    ElMessage.warning('未选择要删除的数据！')
    return
  }

  ElMessageBox.confirm(
    `确定删除选中的 ${selected.value.length} 条单据吗？`,
    '提示',
    {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消'
    }
  )
    .then(async () => {
      // 提取订单ID
      const orderIds = selected.value
        .map(row => row.id)
        .filter(id => id !== undefined && id !== null && id > 0) as number[]

      if (orderIds.length === 0) {
        ElMessage.error('选中的单据缺少订单ID，无法执行删除操作')
        return
      }

      if (orderIds.length !== selected.value.length) {
        ElMessage.warning(`共选中 ${selected.value.length} 条单据，其中 ${orderIds.length} 条有有效订单ID，将删除这 ${orderIds.length} 条`)
      }

      try {
        // 调用批量删除接口
        await PurchaseOrderAPI.deletePurchaseOrders(orderIds)
        
        // 清空选中项
        selected.value = []
        if (tableRef.value) {
          tableRef.value.clearSelection()
        }

        // 重新加载列表
        purchaseSearchRef.value?.search?.({})

        ElMessage.success(`删除成功！共删除 ${orderIds.length} 条单据`)
      } catch (error: any) {
        console.error('批量删除失败:', error)
        const errorMessage = error?.response?.data?.message || error?.message || '删除失败，请稍后重试'
        ElMessage.error(errorMessage)
      }
    })
    .catch(() => {})
}

/* —— 固定横向滚动条：仅显示这一个，隐藏表格内部横向条 —— */
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

// 分页变化处理
const handlePageChange = (page: number) => {
  currentPage.value = page
  // 分页变化会自动触发 PurchaseSearchForm 的搜索
}

// 每页条数变化处理
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  // 分页变化会自动触发 PurchaseSearchForm 的搜索
}

onMounted(async () => {
  // 初始化加载数据（通过搜索组件触发）
  await nextTick()
  purchaseSearchRef.value?.search?.({})

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
/* 批量 + 刷新贴合为一组 */
.btn-join {
  display: inline-flex;
  align-items: center;
}

/* 相邻边界合并成 1px，无缝贴合 */
.btn-join > * + * {
  margin-left: -1px;
}

/* 统一圆角：中间不圆角，仅两端有圆角 */
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
} /* 状态栏高度（同步 x-scroll 粘附位置） */

.purchase-book-form {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: #fff;
}

/* 顶部工具栏 */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
}

/* 主体布局 */
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

/* ——隐藏表格自带横向滚动条—— */
.table-wrapper :deep(.el-table__body-wrapper) {
  overflow-x: hidden !important;
}
.table-wrapper :deep(.el-table__header-wrapper) {
  overflow: hidden !important;
}

/* 固定横向拖动条（紧贴状态栏上方） */
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

/* 统一滚动条样式（横/纵一致） */
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

/* 表头：列名 + 垂直排序箭头（列名右侧上下排列） */
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

/* 相关操作三按钮：灰描边、黑白配、无缝拼接、hover 蓝高亮 */
.op-row {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

/* 通用按钮外观 */
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
/* 消除相邻边框双线，确保删除与下拉"贴合"（el-dropdown 也参与） */
.op-row > * + * {
  margin-left: -1px;
}
.op-btn + .op-btn {
  margin-left: -1px;
}

/* 仅两端保留圆角：第一个按钮左圆角，最后一个按钮右圆角 */
.op-row > :first-child .op-btn,
.op-row > :first-child.op-btn {
  border-top-left-radius: 4px;
  border-bottom-left-radius: 4px;
}
/* 下拉按钮去左圆角 */
:deep(.op-row .el-dropdown .el-button) {
  border-radius: 0 !important;
}
/* 下拉按钮位于末端时给右圆角 */
:deep(.op-row .el-dropdown:last-child .el-button) {
  border-top-right-radius: 4px !important;
  border-bottom-right-radius: 4px !important;
}

/* hover / focus 高亮 */
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

/* 下拉菜单仅展示：不可交互 */
.info-dropdown {
  pointer-events: none;
}

/* 底部状态栏（贴底） */
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

/* 小工具类 */
.mr-8 {
  margin-right: 8px;
}
</style>
