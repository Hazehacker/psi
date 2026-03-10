<template>
  <div class="invoice-report-container">
    <!-- 顶部操作栏 -->
    <div class="header-actions">
      <div class="left-actions">
        <GoodSearch v-model="searchFormData" :config="searchConfig" @search="handleSearch" />
      </div>

      <div class="right-actions">
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

    <!-- 表格主区域 -->
    <div class="table-main-container">
      <el-table
        :data="pagedTableData"
        style="width: 100%; height: 100%"
        v-loading="loading"
        border
        @selection-change="handleSelectionChange"
      >
        <!-- 选择框列 -->
        <el-table-column type="selection" width="55" align="center" />

        <el-table-column prop="type" label="单据类型" width="120" align="center">
          <template #default="scope">
            {{ getOrderTypeDisplayName(scope.row.type) }}
          </template>
        </el-table-column>
        <el-table-column prop="frame" label="所属组织" width="120" align="center" />
        <el-table-column prop="businessUnit" label="往来单位" width="120" align="center" />
        <el-table-column prop="time" label="单据时间" width="120" align="center">
          <template #default="scope">
            {{ formatDate(scope.row.time) }}
          </template>
        </el-table-column>
        <el-table-column prop="number" label="单据编号" width="150" align="center" />
        <el-table-column prop="total" label="单据金额" width="100" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.total) }}
          </template>
        </el-table-column>
        <el-table-column prop="invTime" label="开票时间" width="120" align="center">
          <template #default="scope">
            {{ formatDate(scope.row.invTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="invNumber" label="发票号码" width="150" align="center" />
        <el-table-column prop="title" label="发票抬头" width="150" align="center" />
        <el-table-column prop="invMoney" label="发票金额" width="100" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.invMoney) }}
          </template>
        </el-table-column>
        <el-table-column prop="file" label="开票附件" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.file" type="success" size="small">已上传</el-tag>
            <el-tag v-else type="info" size="small">未上传</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="data"
          label="备注信息"
          width="150"
          align="left"
          show-overflow-tooltip
        />

        <!-- 操作列 -->
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template #default="scope">
            <el-button type="primary" link @click="handleView(scope.row)">详情</el-button>
            <el-button type="danger" link @click="handleDelete([scope.row])">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页和统计 -->
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
            <span class="amount-value">{{ formatCurrency(totalBillAmount) }}</span>
          </div>
          <div class="amount-item">
            <span class="amount-label">发票总金额：</span>
            <span class="amount-value">{{ formatCurrency(totalInvoiceAmount) }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 发票弹框 -->
    <InvoiceDialog
      v-model:visible="showInvoiceDialog"
      :selected-records="selectedRows"
      :is-view-mode="true"
      @invoice-success="handleInvoiceSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Download, Refresh } from '@element-plus/icons-vue'
import GoodSearch from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import InvoiceDialog from './InvoiceDialog.vue'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent/type'
import { getInvoiceReportList, exportReportData, downloadFile } from '@/apis/finance/invoice'
import type {
  InvoiceReportQuery,
  InvoiceReportItem,
  ReportExportRequest
} from '@/apis/finance/invoice/type'

// 搜索配置
const searchConfig: GoodSearchConfig = {
  inline: false,
  showGoods: false,
  showNumber: true,
  showSupplier: true,
  showCustomer: true,
  showPeople: false,
  showBillDate: false,
  showArrivalDate: false,
  showUser: false,
  showExamine: false,
  showState: false,
  showRemark: false,
  showWarehouse: false,
  customFields: [
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
    },
    {
      key: 'invoiceStartDate',
      label: '开具开始日期',
      type: 'date',
      clearable: true
    },
    {
      key: 'invoiceEndDate',
      label: '开具结束日期',
      type: 'date',
      clearable: true
    },
    {
      key: 'invoiceNumber',
      label: '发票号码',
      type: 'input',
      clearable: true
    },
    {
      key: 'invoiceTitle',
      label: '发票抬头',
      type: 'input',
      clearable: true
    }
  ]
}

// 搜索表单数据
const searchFormData = ref<SearchFormData>({
  number: '',
  supplier: '',
  customer: '',
  orderType: '',
  invoiceStartDate: '',
  invoiceEndDate: '',
  invoiceNumber: '',
  invoiceTitle: ''
})

// 表格数据
const originalTableData = ref<InvoiceReportItem[]>([])
const loading = ref(false)

// 响应式数据
const currentPage = ref(1)
const pageSize = ref(10)
const selectedRows = ref<any[]>([])
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

const totalBillAmount = computed(() => {
  return filteredTableData.value.reduce((sum, item) => sum + (item.total || 0), 0)
})

const totalInvoiceAmount = computed(() => {
  return filteredTableData.value.reduce((sum, item) => sum + (item.invMoney || 0), 0)
})

// 方法
const handleSearch = async (formData: SearchFormData) => {
  Object.assign(searchFormData.value, formData)
  currentPage.value = 1
  await loadInvoiceReportList()
}

const handleRefresh = () => {
  Object.keys(searchFormData.value).forEach((key) => {
    ;(searchFormData.value as any)[key] = ''
  })
  currentPage.value = 1
  loadInvoiceReportList()
  ElMessage.success('数据已刷新')
}

const handleExport = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要导出的记录')
    return
  }

  const exportData: ReportExportRequest = {
    ids: selectedRows.value.map((row) => row.id)
  }

  exportReportData(
    exportData,
    (blob) => {
      downloadFile(blob, `购销发票报表_${new Date().toISOString().split('T')[0]}.xlsx`)
      ElMessage.success('导出成功')
    },
    (err) => {
      console.error('导出失败:', err)
      ElMessage.error('导出失败：' + (err.message || '未知错误'))
    }
  )
}

// 加载购销发票报表
const loadInvoiceReportList = async () => {
  loading.value = true
  try {
    const queryParams: InvoiceReportQuery = {
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      number: searchFormData.value.number || undefined,
      supplier: searchFormData.value.supplier || undefined,
      customer: searchFormData.value.customer || undefined,
      startDate: searchFormData.value.invoiceStartDate || undefined,
      endDate: searchFormData.value.invoiceEndDate || undefined,
      inr: searchFormData.value.invoiceNumber || undefined,
      title: searchFormData.value.invoiceTitle || undefined,
      mold: searchFormData.value.orderType ? [searchFormData.value.orderType] : undefined
    }

    getInvoiceReportList(
      queryParams,
      (data) => {
        originalTableData.value = data.rows || []
        ElMessage.success('数据加载成功')
      },
      (err) => {
        console.error('加载购销发票报表失败:', err)
        ElMessage.error('数据加载失败：' + (err.message || '未知错误'))
        originalTableData.value = []
      }
    )
  } catch (error) {
    console.error('加载购销发票报表异常:', error)
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

const getOrderTypeDisplayName = (type: string): string => {
  const typeMap: Record<string, string> = {
    buy: '采购单',
    bre: '采购退货单',
    sell: '销售单',
    sre: '销售退货单'
  }
  return typeMap[type] || type
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  loadInvoiceReportList()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadInvoiceReportList()
}

const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection
}

const handleView = (row: any) => {
  selectedRows.value = [row]
  showInvoiceDialog.value = true
}

const handleDelete = (rows: any[]) => {
  // 实际项目中这里应该调用API删除数据
  rows.forEach((row) => {
    const index = originalTableData.value.findIndex((item) => item.id === row.id)
    if (index !== -1) {
      originalTableData.value.splice(index, 1)
    }
  })
  ElMessage.success(`成功删除 ${rows.length} 条数据`)
}

const handleInvoiceSuccess = () => {
  ElMessage.success('开票成功')
  handleRefresh()
}

onMounted(() => {
  loadInvoiceReportList()
})
</script>

<style scoped>
.invoice-report-container {
  position: relative;
  padding: 16px;
  height: calc(100vh - 32px);
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
  overflow: hidden;
}

.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding: 12px 16px;
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  flex-shrink: 0;
}

.left-actions {
  display: flex;
  align-items: center;
}

.right-actions {
  display: flex;
  align-items: center;
  gap: 8px;
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
:deep(.el-table) {
  flex: 1;
  height: 100% !important;
}

:deep(.el-table__body-wrapper) {
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
  .invoice-report-container {
    padding: 8px;
    height: calc(100vh - 16px);
  }

  .header-actions {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
    padding: 12px;
  }

  .left-actions,
  .right-actions {
    justify-content: center;
  }

  .left-actions {
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
