<template>
  <div class="invoice-container">
    <!-- 操作按钮区域 -->
    <div class="operation-bar">
      <div class="operation-left">
        <GoodSearch
          v-model="searchFormData"
          :config="searchConfig"
          @search="handleSearch"
          class="small-search"
        />
      </div>

      <div class="operation-right">
        <el-button type="primary" @click="handleInvoice" class="action-btn">
          <el-icon><Document /></el-icon>
          开票
        </el-button>
        <el-button type="primary" @click="handleExport" class="action-btn">
          <el-icon><Download /></el-icon>
          导出
        </el-button>
        <el-button type="info" @click="handleRefresh" class="action-btn">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <!-- 表格区域 - 占据主要空间 -->
    <div class="table-main-container">
      <el-table
        :data="pagedTableData"
        style="width: 100%; height: 100%"
        v-loading="loading"
        border
        class="grid-table"
        @selection-change="handleSelectionChange"
      >
        <!-- 选择框列 -->
        <el-table-column type="selection" width="55" align="center" />

        <el-table-column prop="type" label="单据类型" width="120" align="center">
          <template #default="scope">
            {{ getOrderTypeDisplayName(scope.row.type) }}
          </template>
        </el-table-column>

        <el-table-column prop="frame" label="所属组织" width="120" align="center">
          <template #default="scope">
            {{ scope.row.frame }}
          </template>
        </el-table-column>

        <el-table-column prop="unit" label="往来单位" width="150">
          <template #default="scope">
            {{ scope.row.unit }}
          </template>
        </el-table-column>

        <el-table-column prop="time" label="单据时间" width="120" align="center">
          <template #default="scope">
            {{ formatDate(scope.row.time) }}
          </template>
        </el-table-column>

        <el-table-column prop="number" label="单据编号" width="150">
          <template #default="scope">
            {{ scope.row.number }}
          </template>
        </el-table-column>

        <el-table-column prop="status" label="发票状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="getInvoiceStatusType(scope.row.status)" size="small">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="totalCount" label="单据金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.totalCount) }}
          </template>
        </el-table-column>

        <el-table-column prop="invoicedCount" label="已开票金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.invoicedCount) }}
          </template>
        </el-table-column>

        <el-table-column prop="disinvoicedCount" label="未开票金额" width="120" align="right">
          <template #default="scope">
            <span :class="getUninvoicedAmountClass(scope.row)">
              {{ formatCurrency(scope.row.disinvoicedCount) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="开票金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.disinvoicedCount) }}
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页区域和金额统计 - 固定在底部 -->
    <div class="footer-container">
      <div class="pagination-container">
        <div class="pagination-left">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 30, 50]"
            :total="filteredTableData.length"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
        <div class="amount-summary">
          <div class="amount-item">
            <span class="amount-label">单据总金额：</span>
            <span class="amount-value total-amount">{{ formatCurrency(totalAmount) }}</span>
          </div>
          <div class="amount-item">
            <span class="amount-label">已开票总金额：</span>
            <span class="amount-value invoiced-amount">{{
              formatCurrency(totalInvoicedAmount)
            }}</span>
          </div>
          <div class="amount-item">
            <span class="amount-label">未开票总金额：</span>
            <span class="amount-value uninvoiced-amount">{{
              formatCurrency(totalUninvoicedAmount)
            }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 发票弹框 -->
    <InvoiceDialog
      v-model:visible="showInvoiceDialog"
      :selected-records="selectedRecords"
      @invoice-success="handleInvoiceSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Download, Refresh, Document } from '@element-plus/icons-vue'
import GoodSearch from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import InvoiceDialog from './InvoiceDialog.vue'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent/type'
import { getInvoiceList, exportInvoiceData, downloadFile } from '@/apis/finance/invoice'
import type {
  InvoiceListQuery,
  InvoiceListItem,
  InvoiceExportRequest
} from '@/apis/finance/invoice/type'

// 搜索配置
const searchConfig: GoodSearchConfig = {
  inline: false,
  showGoods: false,
  showNumber: true,
  showSupplier: true,
  showCustomer: true,
  showPeople: false,
  showBillDate: true,
  showArrivalDate: false,
  showUser: false,
  showExamine: false,
  showState: false,
  showRemark: false,
  showWarehouse: false,
  customFields: [
    {
      key: 'invoiceStatus',
      type: 'select',
      label: '发票状态',
      options: [
        { label: '未开票', value: '0' },
        { label: '部分开票', value: '1' },
        { label: '已开票', value: '2' },
        { label: '无需开具', value: '3' }
      ]
    },
    {
      key: 'orderType',
      type: 'select',
      label: '单据类型',
      options: [
        { label: '采购单', value: 'buy' },
        { label: '采购退货单', value: 'bre' },
        { label: '销售单', value: 'sell' },
        { label: '销售退货单', value: 'sre' }
      ]
    }
  ]
}

// 搜索表单数据
const searchFormData = ref<SearchFormData>({
  number: '',
  supplier: '',
  customer: '',
  startTime: '',
  endTime: '',
  invoiceStatus: '',
  orderType: ''
})

// 表格数据
const originalTableData = ref<InvoiceListItem[]>([])
const loading = ref(false)

// 响应式数据
const currentPage = ref(1)
const pageSize = ref(10)
const selectedRecords = ref<any[]>([])
const showInvoiceDialog = ref(false)

// 计算属性
const filteredTableData = computed(() => {
  return originalTableData.value
})

const pagedTableData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredTableData.value.slice(start, end)
})

const totalAmount = computed(() => {
  return filteredTableData.value.reduce((sum, item) => sum + (item.totalCount || 0), 0)
})

const totalInvoicedAmount = computed(() => {
  return filteredTableData.value.reduce((sum, item) => sum + (item.invoicedCount || 0), 0)
})

const totalUninvoicedAmount = computed(() => {
  return filteredTableData.value.reduce((sum, item) => sum + (item.disinvoicedCount || 0), 0)
})

// 方法
const handleSearch = async (formData: SearchFormData) => {
  Object.assign(searchFormData.value, formData)
  currentPage.value = 1
  await loadInvoiceList()
}

const handleRefresh = () => {
  Object.keys(searchFormData.value).forEach((key) => {
    ;(searchFormData.value as any)[key] = ''
  })
  currentPage.value = 1
  loadInvoiceList()
  ElMessage.success('数据已刷新')
}

const handleExport = () => {
  if (selectedRecords.value.length === 0) {
    ElMessage.warning('请选择要导出的记录')
    return
  }

  const exportData: InvoiceExportRequest = {
    keys: selectedRecords.value.map((record) => ({
      id: record.id,
      type: record.type as 'buy' | 'bre' | 'sell' | 'sre'
    }))
  }

  exportInvoiceData(
    exportData,
    (blob) => {
      downloadFile(blob, `发票数据_${new Date().toISOString().split('T')[0]}.xlsx`)
      ElMessage.success('导出成功')
    },
    (err) => {
      console.error('导出失败:', err)
      ElMessage.error('导出失败：' + (err.message || '未知错误'))
    }
  )
}

// 加载发票列表
const loadInvoiceList = async () => {
  loading.value = true
  try {
    const queryParams: InvoiceListQuery = {
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      number: searchFormData.value.number || undefined,
      supplier: searchFormData.value.supplier || undefined,
      customer: searchFormData.value.customer || undefined,
      startDate: searchFormData.value.startTime || undefined,
      endDate: searchFormData.value.endTime || undefined,
      type: searchFormData.value.orderType ? [searchFormData.value.orderType] : undefined,
      status: searchFormData.value.invoiceStatus
        ? [parseInt(searchFormData.value.invoiceStatus)]
        : undefined
    }

    getInvoiceList(
      queryParams,
      (data) => {
        originalTableData.value = data.rows || []
        ElMessage.success('数据加载成功')
      },
      (err) => {
        console.error('加载发票列表失败:', err)
        ElMessage.error('数据加载失败：' + (err.message || '未知错误'))
        originalTableData.value = []
      }
    )
  } catch (error) {
    console.error('加载发票列表异常:', error)
    ElMessage.error('数据加载异常')
    originalTableData.value = []
  } finally {
    loading.value = false
  }
}

const formatCurrency = (value: number) => {
  return (
    '¥' +
    value.toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    })
  )
}

const formatDate = (dateString: string) => {
  if (!dateString) return ''
  return dateString.split(' ')[0] // 只显示日期部分
}

const getInvoiceStatusType = (
  status: string
): 'primary' | 'success' | 'warning' | 'info' | 'danger' => {
  const statusMap: Record<string, 'primary' | 'success' | 'warning' | 'info' | 'danger'> = {
    未开票: 'danger',
    部分开票: 'warning',
    已开票: 'success',
    无需开具: 'info'
  }
  return statusMap[status] || 'info'
}

const getOrderTypeDisplayName = (type: string): string => {
  const typeMap: Record<string, string> = {
    buy: '采购单',
    bre: '采购退货单',
    sell: '销售单',
    sre: '销售退货单'
  }
  return typeMap[type] || type
}

const getUninvoicedAmountClass = (row: any) => {
  return (row.disinvoicedCount || 0) > 0 ? 'negative-amount' : 'positive-amount'
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  loadInvoiceList()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadInvoiceList()
}

const handleSelectionChange = (selection: any[]) => {
  selectedRecords.value = selection
}

const handleInvoice = () => {
  if (selectedRecords.value.length === 0) {
    ElMessage.warning('请选择要开票的记录')
    return
  }

  const hasInvoicedRecords = selectedRecords.value.some(
    (record) => record.status === '已开票' || record.status === '无需开具'
  )

  if (hasInvoicedRecords) {
    ElMessage.warning('已选择的记录中包含已开票或无需开具的记录，请重新选择')
    return
  }

  showInvoiceDialog.value = true
}

const handleInvoiceSuccess = () => {
  ElMessage.success('开票成功')
  handleRefresh()
}

onMounted(() => {
  loadInvoiceList()
})
</script>

<style scoped>
.invoice-container {
  position: relative;
  padding: 16px;
  height: calc(100vh - 32px);
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
  overflow: hidden;
}

.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: white;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  margin-bottom: 12px;
  min-height: 60px;
  flex-shrink: 0;
}

.operation-left {
  display: flex;
  align-items: center;
}

.operation-right {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 表格主容器 - 占据剩余空间 */
.table-main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  min-height: 0;
}

/* 表格样式调整 */
:deep(.grid-table) {
  flex: 1;
  height: 100% !important;
}

:deep(.grid-table .el-table__body-wrapper) {
  height: calc(100% - 40px) !important;
}

/* 底部容器 */
.footer-container {
  flex-shrink: 0;
  margin-top: 12px;
}

.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: white;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  flex-wrap: wrap;
  gap: 16px;
}

.amount-summary {
  display: flex;
  gap: 20px;
  align-items: center;
}

.amount-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.amount-label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
  white-space: nowrap;
}

.amount-value {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  padding: 5px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #ffffff;
  min-width: 120px;
  text-align: center;
  display: inline-block;
}

.negative-amount {
  color: #f56c6c;
}

.positive-amount {
  color: #67c23a;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .pagination-container {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .pagination-left {
    justify-content: center;
  }

  .amount-summary {
    justify-content: center;
    flex-wrap: wrap;
  }
}

@media (max-width: 768px) {
  .invoice-container {
    padding: 8px;
    height: calc(100vh - 16px);
  }

  .operation-bar {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
    padding: 12px;
  }

  .operation-left,
  .operation-right {
    justify-content: center;
  }

  .operation-left {
    border-bottom: 1px solid #e4e7ed;
    padding-bottom: 12px;
  }

  .pagination-container {
    padding: 8px 12px;
  }

  .amount-summary {
    gap: 12px;
  }

  .amount-item {
    flex-direction: column;
    gap: 4px;
    text-align: center;
  }

  .amount-value {
    min-width: 100px;
    padding: 4px 8px;
  }

  :deep(.el-pagination) {
    justify-content: center;
  }

  :deep(.el-pagination__sizes),
  :deep(.el-pagination__jump) {
    margin-top: 8px;
  }
}

/* 美化滚动条 */
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
</style>
