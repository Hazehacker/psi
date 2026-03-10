<template>
  <div class="sys area">
    <!-- 操作按钮区域 -->
    <div class="operation-bar">
      <div class="operation-left">
        <!-- 使用搜索组件 -->
        <GoodSearch
          :modelValue="searchFormData"
          :config="searchConfig"
          @update:modelValue="handleSearchFormUpdate"
          @search="handleGoodSearch"
          ref="goodSearchRef"
        />
      </div>

      <div class="operation-right">
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
        :data="currentPageData"
        style="width: 100%"
        v-loading="loading"
        border
        class="grid-table"
      >
        <!-- 单据类型列 -->
        <el-table-column prop="documentType" label="单据类型" width="120" align="center">
          <template #default="scope">
            <span>{{ scope.row.documentType || '-' }}</span>
          </template>
        </el-table-column>

        <!-- 所属组织列 -->
        <el-table-column prop="organization" label="所属组织" width="120" align="center">
          <template #default="scope">
            <span>{{ scope.row.organization || '-' }}</span>
          </template>
        </el-table-column>

        <!-- 往来单位列 -->
        <el-table-column prop="partnerUnit" label="往来单位" width="150">
          <template #default="scope">
            <span>{{ scope.row.partnerUnit || '-' }}</span>
          </template>
        </el-table-column>

        <!-- 单据时间 -->
        <el-table-column prop="documentDate" label="单据时间" width="120" align="center">
          <template #default="scope">
            <span>{{ formatDate(scope.row.documentDate) || '-' }}</span>
          </template>
        </el-table-column>

        <!-- 单据编号 -->
        <el-table-column prop="documentNumber" label="单据编号" width="150">
          <template #default="scope">
            <span>{{ scope.row.documentNumber || '-' }}</span>
          </template>
        </el-table-column>

        <!-- 支出类别 -->
        <el-table-column prop="expenseType" label="支出类别" width="120" align="center">
          <template #default="scope">
            <span>{{ scope.row.expenseType || '-' }}</span>
          </template>
        </el-table-column>

        <!-- 结算状态 -->
        <el-table-column prop="settlementStatus" label="结算状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="getSettlementStatusType(scope.row.settlementStatus)" size="small">
              {{ getSettlementStatusText(scope.row.settlementStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 金额 -->
        <el-table-column prop="amount" label="金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.amount) }}
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <div class="pagination-container">
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
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Download, Refresh } from '@element-plus/icons-vue'
import GoodSearch from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent/type'
import { getTradeExpenseList, exportReportData } from '@/apis/finance/cost'

// 定义接口类型 - 严格按照后端文档
interface TradeExpenseReportItem {
  id?: string
  documentType?: string
  organization?: string
  partnerUnit?: string
  documentDate?: string
  documentNumber?: string
  expenseType?: string
  settlementStatus?: string
  amount?: number
  infos?: any[]
}

// 搜索组件引用
const goodSearchRef = ref()

// 表格数据
const tableData = ref<TradeExpenseReportItem[]>([])
const loading = ref(false)

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

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

// 搜索配置 - 根据接口文档调整
const searchConfig = reactive<GoodSearchConfig>({
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
  inline: false,
  customFields: [
    {
      key: 'expenseType',
      type: 'select',
      label: '支出类别',
      options: [
        { label: '运输费', value: '运输费' },
        { label: '包装费', value: '包装费' },
        { label: '差旅费', value: '差旅费' }
        // 根据实际数据调整选项
      ]
    },
    {
      key: 'settlementStatus',
      type: 'select',
      label: '结算状态',
      options: [
        { label: '部分结算', value: '1' },
        { label: '已结算', value: '2' }
        // 注意：接口文档中只有1和2，没有0（未结算）
      ]
    },
    {
      key: 'documentType',
      type: 'select',
      label: '单据类型',
      options: [
        { label: '销售单', value: '销售单' },
        { label: '采购单', value: '采购单' },
        { label: '支出单', value: '支出单' }
      ]
    }
  ]
})

// 处理搜索表单更新
const handleSearchFormUpdate = (newValue: SearchFormData) => {
  Object.assign(searchFormData.value, newValue)
}

// 处理搜索
const handleGoodSearch = async (formData: SearchFormData) => {
  console.log('搜索条件:', formData)
  Object.assign(searchFormData.value, formData)
  currentPage.value = 1
  await loadTableData()
  ElMessage.success('搜索完成')
}

// 加载表格数据
const loadTableData = async () => {
  try {
    loading.value = true

    // 根据接口文档构建参数
    const params = {
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      customerId: searchFormData.value.customer || undefined,
      supplierId: searchFormData.value.supplier || undefined,
      documentNumber: searchFormData.value.number || undefined,
      documentType: searchFormData.value.documentType
        ? [searchFormData.value.documentType]
        : undefined,
      expenseTypeId: searchFormData.value.expenseType
        ? [searchFormData.value.expenseType]
        : undefined,
      settlementStatus: searchFormData.value.settlementStatus
        ? [parseInt(searchFormData.value.settlementStatus)]
        : undefined,
      startDate: searchFormData.value.startTime || undefined,
      endDate: searchFormData.value.endTime || undefined
    }

    console.log('📤 发送报表查询请求:', params)

    const response = await getTradeExpenseList(params)
    console.log('📥 报表查询完整响应:', response)

    if (response.code === 10000 && response.data) {
      console.log('🔍 响应数据详情:', {
        total: response.data.total,
        pageIndex: response.data.pageIndex,
        pageSize: response.data.pageSize,
        rowsCount: response.data.rows ? response.data.rows.length : 0,
        firstRow: response.data.rows ? response.data.rows[0] : '无数据'
      })

      // 直接使用后端返回的数据，不需要映射
      tableData.value = response.data.rows || []
      total.value = response.data.total || 0

      // 检查数据完整性
      if (tableData.value.length > 0) {
        const firstRow = tableData.value[0]
        console.log('📊 第一行数据字段检查:', {
          documentType: firstRow.documentType,
          organization: firstRow.organization,
          partnerUnit: firstRow.partnerUnit,
          documentDate: firstRow.documentDate,
          documentNumber: firstRow.documentNumber,
          expenseType: firstRow.expenseType,
          settlementStatus: firstRow.settlementStatus,
          amount: firstRow.amount
        })
      }

      if (tableData.value.length === 0) {
        ElMessage.warning('查询结果为空')
      } else {
        ElMessage.success(`加载成功，共 ${total.value} 条记录`)
      }
    } else {
      console.error('❌ 获取数据失败:', response)
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('❌ 加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 刷新功能
const handleRefresh = async () => {
  // 重置搜索表单
  if (goodSearchRef.value) {
    goodSearchRef.value.resetForm()
  }

  // 重置搜索条件
  Object.assign(searchFormData.value, {
    number: '',
    supplier: '',
    customer: '',
    startTime: '',
    endTime: '',
    expenseType: '',
    settlementStatus: '',
    documentType: ''
  })

  currentPage.value = 1
  await loadTableData()
  ElMessage.success('已刷新数据，显示全部记录')
}

// 导出功能
const handleExport = async () => {
  try {
    ElMessage.info('正在准备导出报表数据...')

    const params = {
      customerId: searchFormData.value.customer || undefined,
      supplierId: searchFormData.value.supplier || undefined,
      documentNumber: searchFormData.value.number || undefined,
      documentType: searchFormData.value.documentType
        ? [searchFormData.value.documentType]
        : undefined,
      expenseTypeId: searchFormData.value.expenseType
        ? [searchFormData.value.expenseType]
        : undefined,
      settlementStatus: searchFormData.value.settlementStatus
        ? [parseInt(searchFormData.value.settlementStatus)]
        : undefined,
      startDate: searchFormData.value.startTime || undefined,
      endDate: searchFormData.value.endTime || undefined
    }

    const response = await exportReportData(params)

    // 创建下载链接
    const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url

    // 从响应头获取文件名或使用默认文件名
    const contentDisposition = response.headers['content-disposition']
    let filename = `购销费用报表_${new Date().toISOString().split('T')[0]}.xlsx`
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

// 格式化日期显示
const formatDate = (dateString?: string) => {
  if (!dateString) return ''
  try {
    const date = new Date(dateString)
    return date.toISOString().split('T')[0]
  } catch {
    return dateString
  }
}

// 获取结算状态显示文本
const getSettlementStatusText = (status?: string) => {
  if (!status) return '未知'
  switch (status) {
    case '1':
      return '部分结算'
    case '2':
      return '已结算'
    default:
      return status
  }
}

// 获取结算状态标签类型
const getSettlementStatusType = (status?: string) => {
  if (!status) return 'info'
  switch (status) {
    case '1':
      return 'warning' // 部分结算
    case '2':
      return 'success' // 已结算
    default:
      return 'info'
  }
}

// 当前页数据
const currentPageData = computed(() => {
  return tableData.value
})

// 初始化
onMounted(() => {
  loadTableData()
})
</script>

<!-- 样式保持不变 -->

<style scoped>
.sys.area {
  position: relative;
  padding: 16px;
  height: calc(100vh - 32px);
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  background: white;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  margin-bottom: 0;
}

.operation-left,
.operation-right {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 16px;
}

.custom-divider {
  margin: 8px 0;
  border-color: #e4e7ed;
}

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

/* 分页容器样式 */
.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 12px 16px;
  border-top: 1px solid #ebeef5;
  background: #fafafa;
}

.parent-row {
  font-weight: 600;
  color: #303133;
}

.child-row {
  color: #606266;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .pagination-container {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
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

  :deep(.el-pagination) {
    justify-content: center;
  }

  :deep(.el-pagination__sizes),
  :deep(.el-pagination__jump) {
    margin-top: 8px;
  }
}
</style>
