<template>
  <div class="sys area">
    <!-- 操作按钮区域 -->
    <div class="operation-bar">
      <div class="operation-left">
        <!-- 搜索按钮 - 小型化 -->
        <GoodSearch
          v-model="searchFormData"
          :config="searchConfig"
          @search="handleSearch"
          class="small-search"
        />
      </div>

      <div class="operation-right">
        <el-button type="primary" @click="handleSettlement" class="action-btn">
          <el-icon><Money /></el-icon>
          结算
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

    <el-divider class="custom-divider" />

    <!-- 表格区域 - 占据主要空间 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        border
        class="grid-table"
        @selection-change="handleSelectionChange"
      >
        <!-- 选择框列 -->
        <el-table-column type="selection" width="55" align="center" />

        <!-- 单据类型列 -->
        <el-table-column prop="documentType" label="单据类型" width="120" align="center">
          <template #default="scope">
            {{ scope.row.documentType || scope.row.orderType }}
          </template>
        </el-table-column>

        <!-- 所属组织列 -->
        <el-table-column prop="organization" label="所属组织" width="120" align="center">
          <template #default="scope">
            {{ scope.row.organization || scope.row.orgName }}
          </template>
        </el-table-column>

        <!-- 往来单位列 -->
        <el-table-column prop="partnerUnit" label="往来单位" width="150">
          <template #default="scope">
            {{ scope.row.partnerUnit || scope.row.contactUnit }}
          </template>
        </el-table-column>

        <!-- 单据时间 -->
        <el-table-column prop="documentDate" label="单据时间" width="120" align="center">
          <template #default="scope">
            {{ scope.row.documentDate || scope.row.orderDate }}
          </template>
        </el-table-column>

        <!-- 单据编号 -->
        <el-table-column prop="documentNumber" label="单据编号" width="150">
          <template #default="scope">
            {{ scope.row.documentNumber || scope.row.orderNo }}
          </template>
        </el-table-column>

        <!-- 支出类别 -->
        <el-table-column prop="expenseType" label="支出类别" width="120" align="center">
          <template #default="scope">
            {{ scope.row.expenseType }}
          </template>
        </el-table-column>

        <!-- 结算状态 -->
        <el-table-column prop="settlementStatus" label="结算状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="getSettlementStatusType(scope.row.settlementStatus)" size="small">
              {{ scope.row.settlementStatus }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 金额 -->
        <el-table-column prop="amount" label="金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.amount) }}
          </template>
        </el-table-column>

        <!-- 已结算金额 -->
        <el-table-column prop="settledAmount" label="已结算金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.settledAmount) }}
          </template>
        </el-table-column>

        <!-- 未结算金额 -->
        <el-table-column prop="unsettledAmount" label="未结算金额" width="120" align="right">
          <template #default="scope">
            <span :class="getUnsettledAmountClass(scope.row)">
              {{ formatCurrency(scope.row.unsettledAmount) }}
            </span>
          </template>
        </el-table-column>

        <!-- 结算金额 -->
        <el-table-column prop="settlementAmount" label="结算金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.settlementAmount) }}
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域和金额统计 -->
      <div class="pagination-container">
        <div class="pagination-left">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 30, 50]"
            :total="total"
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
            <span class="amount-label">已结算总金额：</span>
            <span class="amount-value settled-amount">{{
              formatCurrency(totalSettledAmount)
            }}</span>
          </div>
          <div class="amount-item">
            <span class="amount-label">未结算总金额：</span>
            <span class="amount-value unsettled-amount">{{
              formatCurrency(totalUnsettledAmount)
            }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 结算弹框 -->
    <SettlementDialog
      v-model:visible="showSettlementDialog"
      :selected-records="selectedRecords"
      :settlement-data="settlementData"
      @settlement-success="handleSettlementSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Download, Refresh, Money } from '@element-plus/icons-vue'
import GoodSearch from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import SettlementDialog from '@/views/finance/cost/SettlementDialog.vue'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent/type'
import { getTradeExpensePage, exportExpenseData, getSettles } from '@/apis/finance/cost'

// 定义接口类型
interface TradeExpenseItem {
  id?: string
  documentType?: string
  orderType?: string
  organization?: string
  orgName?: string
  partnerUnit?: string
  contactUnit?: string
  documentDate?: string
  orderDate?: string
  documentNumber?: string
  orderNo?: string
  expenseType?: string
  settlementStatus?: string
  amount?: number
  settledAmount?: number
  unsettledAmount?: number
  settlementAmount?: number
}

interface CostDTO {
  id?: string
  money?: number
  type?: string
  data?: string
}

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
      key: 'expenseType',
      type: 'select',
      label: '支出类别',
      options: [
        { label: '采购支出', value: '采购支出' },
        { label: '人力成本', value: '人力成本' },
        { label: '租金支出', value: '租金支出' },
        { label: '水电物业费', value: '水电物业费' },
        { label: '运输物流费', value: '运输物流费' }
      ]
    },
    {
      key: 'settlementStatus',
      type: 'select',
      label: '结算状态',
      options: [
        { label: '未结算', value: '0' },
        { label: '部分结算', value: '1' },
        { label: '已结算', value: '2' }
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
        { label: '销售退货单', value: 'sre' },
        { label: '调拨单', value: 'swap' },
        { label: '其它入库单', value: 'entry' },
        { label: '其它出库单', value: 'extry' }
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
  expenseType: '',
  settlementStatus: '',
  orderType: ''
})

// 表格数据
const tableData = ref<TradeExpenseItem[]>([])
const loading = ref(false)

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 选择的行数据
const selectedRecords = ref<TradeExpenseItem[]>([])

// 结算弹框
const showSettlementDialog = ref(false)
const settlementData = ref<CostDTO[]>([])

// 搜索处理
const handleSearch = async (formData: SearchFormData) => {
  Object.assign(searchFormData.value, formData)
  currentPage.value = 1
  await loadTableData()
  ElMessage.success('搜索完成')
}

// 刷新功能
const handleRefresh = async () => {
  // 清空搜索表单数据
  Object.keys(searchFormData.value).forEach((key) => {
    ;(searchFormData.value as any)[key] = ''
  })
  currentPage.value = 1
  await loadTableData()
  ElMessage.success('数据已刷新')
}

// 在 PurchaseSalesCost.vue 中添加数据映射函数
const mapBackendDataToFrontend = (backendData: any): TradeExpenseItem => {
  return {
    id: backendData.id,
    documentType: backendData.documentType || backendData.orderType,
    organization: backendData.organization || backendData.orgName,
    partnerUnit: backendData.partnerUnit || backendData.contactUnit,
    documentDate: backendData.documentDate || backendData.orderDate,
    documentNumber: backendData.documentNumber || backendData.orderNo,
    expenseType: backendData.expenseType,
    settlementStatus: backendData.settlementStatus,
    amount: backendData.amount,
    settledAmount: backendData.settledAmount,
    unsettledAmount: backendData.unsettledAmount,
    settlementAmount: backendData.settlementAmount
  }
}

// 修改 loadTableData 函数
const loadTableData = async () => {
  try {
    loading.value = true

    const params = {
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      customerId: searchFormData.value.customer || undefined,
      supplierId: searchFormData.value.supplier || undefined,
      orderNumber: searchFormData.value.number || undefined,
      documentType: searchFormData.value.orderType ? [searchFormData.value.orderType] : undefined,
      expenseCategoryId: searchFormData.value.expenseType
        ? [searchFormData.value.expenseType]
        : undefined,
      settleStatus: searchFormData.value.settlementStatus
        ? [parseInt(searchFormData.value.settlementStatus)]
        : undefined,
      startDate: searchFormData.value.startTime || undefined,
      endDate: searchFormData.value.endTime || undefined
    }

    console.log('发送查询请求:', params)

    const response = await getTradeExpensePage(params)
    console.log('查询响应:', response)

    if (response.code === 10000 && response.data) {
      // 使用数据映射函数
      tableData.value = (response.data.rows || []).map(mapBackendDataToFrontend)
      total.value = response.data.total || 0
      console.log('处理后的表格数据:', tableData.value)
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

// 导出功能
const handleExport = async () => {
  try {
    ElMessage.info('正在准备导出数据...')

    const params = {
      ids: selectedRecords.value.map((item) => item.id!).filter(Boolean),
      customerId: searchFormData.value.customer || undefined,
      supplierId: searchFormData.value.supplier || undefined,
      documentNumber: searchFormData.value.number || undefined,
      documentType: searchFormData.value.orderType ? [searchFormData.value.orderType] : undefined,
      expenseTypeId: searchFormData.value.expenseType
        ? [searchFormData.value.expenseType]
        : undefined,
      settlementStatus: searchFormData.value.settlementStatus
        ? [parseInt(searchFormData.value.settlementStatus)]
        : undefined,
      startDate: searchFormData.value.startTime || undefined,
      endDate: searchFormData.value.endTime || undefined
    }

    const response = await exportExpenseData(params)

    // 创建下载链接
    const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url

    // 从响应头获取文件名或使用默认文件名
    const contentDisposition = response.headers['content-disposition']
    let filename = `购销费用_${new Date().toISOString().split('T')[0]}.xlsx`
    if (contentDisposition) {
      const filenameMatch = contentDisposition.match(/filename="?(.+)"?/i)
      if (filenameMatch && filenameMatch[1]) {
        filename = filenameMatch[1]
      }
    }

    link.download = filename
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)

    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}

// 结算功能
const handleSettlement = async () => {
  if (selectedRecords.value.length === 0) {
    ElMessage.warning('请选择要结算的记录')
    return
  }

  // 检查是否选择了已结算的记录
  const hasSettledRecords = selectedRecords.value.some(
    (record: TradeExpenseItem) => record.settlementStatus === '已结算'
  )

  if (hasSettledRecords) {
    ElMessage.warning('已选择的记录中包含已结算的记录，请重新选择')
    return
  }

  try {
    // 获取结算单数据 - 确保ID有效
    const selectedIds = selectedRecords.value
      .map((item) => item.id)
      .filter((id): id is string => !!id && id.trim() !== '') // 确保id是有效的非空字符串

    if (selectedIds.length === 0) {
      ElMessage.warning('选中的记录没有有效的ID，请联系管理员')
      return
    }

    console.log('发送结算请求，IDs:', selectedIds)

    const response = await getSettles(selectedIds)
    if (response.code === 10000 && response.data) {
      // 将结算数据传递给弹框
      settlementData.value = response.data
      showSettlementDialog.value = true
    } else {
      ElMessage.error(response.message || '获取结算数据失败')
    }
  } catch (error) {
    console.error('获取结算数据失败:', error)
    // 更详细的错误信息
    if (error && typeof error === 'object' && 'message' in error) {
      ElMessage.error(`获取结算数据失败: ${error.message}`)
    } else {
      ElMessage.error('获取结算数据失败，请检查网络连接')
    }
  }
}

// 结算成功处理
const handleSettlementSuccess = () => {
  ElMessage.success('结算成功')
  // 刷新数据
  loadTableData()
}

// 分页处理
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  loadTableData()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadTableData()
}

// 选择行处理
const handleSelectionChange = (selection: TradeExpenseItem[]) => {
  selectedRecords.value = selection
}

// 格式化金额显示
const formatCurrency = (value?: number) => {
  if (value === undefined || value === null) return '¥0.00'
  return (
    '¥' +
    value.toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    })
  )
}

// 获取结算状态标签类型
const getSettlementStatusType = (status?: string) => {
  switch (status) {
    case '未结算':
      return 'danger'
    case '部分结算':
      return 'warning'
    case '已结算':
      return 'success'
    default:
      return 'info'
  }
}

// 获取未结算金额显示样式
const getUnsettledAmountClass = (row: TradeExpenseItem) => {
  return (row.unsettledAmount || 0) > 0 ? 'negative-amount' : 'positive-amount'
}

// 计算总金额统计（当前页）
const totalAmount = computed(() => {
  return tableData.value.reduce(
    (sum: number, item: TradeExpenseItem) => sum + (item.amount || 0),
    0
  )
})

const totalSettledAmount = computed(() => {
  return tableData.value.reduce(
    (sum: number, item: TradeExpenseItem) => sum + (item.settledAmount || 0),
    0
  )
})

const totalUnsettledAmount = computed(() => {
  return tableData.value.reduce(
    (sum: number, item: TradeExpenseItem) => sum + (item.unsettledAmount || 0),
    0
  )
})

onMounted(() => {
  // 组件挂载后加载数据
  loadTableData()
})
</script>

<style scoped>
.sys.area {
  position: relative;
  padding: 16px;
  height: calc(100vh - 32px);
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

/* 操作栏样式 */
.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 16px;
  background: white;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  margin-bottom: 0;
  min-height: 60px;
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

/* 小型搜索按钮样式 */
.small-search {
  :deep(.search-trigger-btn) {
    padding: 6px 12px;
    font-size: 12px;
    height: 32px;
  }
}

/* 分割线样式 */
.custom-divider {
  margin: 8px 0;
  border-color: #e4e7ed;
}

/* 表格容器 - 占据剩余空间 */
.table-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
  background: white;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 表格样式 */
:deep(.el-table) {
  flex: 1;
}

:deep(.el-table .el-table__row) {
  cursor: pointer;
}

/* 网状表格样式 */
:deep(.grid-table) {
  border: 1px solid #ebeef5;
}

:deep(.grid-table .el-table__header-wrapper) {
  border-bottom: 1px solid #ebeef5;
}

:deep(.grid-table .el-table__cell) {
  border-right: 1px solid #ebeef5;
}

:deep(.grid-table .el-table__row) {
  border-bottom: 1px solid #ebeef5;
}

/* 分页容器样式 */
.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-top: 1px solid #ebeef5;
  background: #fafafa;
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
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
}

.amount-value:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border-color: #c0c4cc;
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
  .sys.area {
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
  width: 6px;
  height: 6px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
