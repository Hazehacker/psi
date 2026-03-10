<template>
  <div class="sys area">
    <!-- 操作按钮区域 -->
    <div class="operation-bar">
      <div class="operation-left">
        <!-- 直接使用 GoodSearch 组件的搜索弹框 -->
        <GoodSearch
          v-model="goodSearchForm"
          :config="goodSearchConfig"
          @search="handleSearch"
          class="good-search-custom"
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
      <el-table :data="tableData" style="width: 100%" v-loading="loading" border class="grid-table">
        <!-- 单据类型列 -->
        <el-table-column prop="orderType" label="单据类型" width="120" align="center">
          <template #default="scope">
            {{ scope.row.orderType }}
          </template>
        </el-table-column>

        <!-- 所属组织列 -->
        <el-table-column prop="orgName" label="所属组织" width="120" align="center">
          <template #default="scope">
            {{ scope.row.orgName }}
          </template>
        </el-table-column>

        <!-- 供应商列 -->
        <el-table-column prop="supplierName" label="供应商" width="150">
          <template #default="scope">
            {{ scope.row.supplierName }}
          </template>
        </el-table-column>

        <!-- 单据时间 -->
        <el-table-column prop="orderDate" label="单据时间" width="120" align="center">
          <template #default="scope">
            {{ scope.row.orderDate }}
          </template>
        </el-table-column>

        <!-- 单据编号 -->
        <el-table-column prop="orderNo" label="单据编号" width="150">
          <template #default="scope">
            {{ scope.row.orderNo }}
          </template>
        </el-table-column>

        <!-- 单据金额 -->
        <el-table-column prop="orderAmount" label="单据金额" width="120" align="right">
          <template #default="scope">
            <span :class="getAmountClass(scope.row)">
              {{ formatCurrency(scope.row.orderAmount) }}
            </span>
          </template>
        </el-table-column>

        <!-- 实际金额 -->
        <el-table-column prop="actualAmount" label="实际金额" width="120" align="right">
          <template #default="scope">
            <span :class="getAmountClass(scope.row)">
              {{ formatCurrency(scope.row.actualAmount) }}
            </span>
          </template>
        </el-table-column>

        <!-- 单据付款 -->
        <el-table-column prop="paidAmount" label="单据付款" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.paidAmount) }}
          </template>
        </el-table-column>

        <!-- 应付款余额 -->
        <el-table-column prop="payableBalance" label="应付款余额" width="130" align="right">
          <template #default="scope">
            <span class="payable-balance">
              {{ formatCurrency(scope.row.payableBalance) }}
            </span>
          </template>
        </el-table-column>

        <!-- 付款率 -->
        <el-table-column prop="paymentRate" label="付款率" width="100" align="center">
          <template #default="scope">
            <el-progress :percentage="scope.row.paymentRate" :show-text="true" />
          </template>
        </el-table-column>

        <!-- 核销状态 -->
        <el-table-column prop="writeOffStatus" label="核销状态" width="120" align="center">
          <template #default="scope">
            {{ scope.row.writeOffStatus }}
          </template>
        </el-table-column>

        <!-- 备注信息 -->
        <el-table-column prop="remark" label="备注信息" min-width="150">
          <template #default="scope">
            {{ scope.row.remark }}
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
            <span class="amount-label">应付款总余额：</span>
            <span class="amount-value payable-amount">{{
              formatCurrency(payableTotalBalance)
            }}</span>
          </div>
        </div>
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

import {
  procurementPaymentFormUsingGet,
  procurementPaymentFormOpenApiExportUsingGet
} from '@/apis/report/purchase/caigoufukuanbiao'
import type {
  ProcurementPaymentFormUsingGetParams,
  ProcurementPaymentFormOpenApiExportUsingGetParams,
  PageDTO7
} from '@/apis/report/purchase/types'

// ===================== 1. 类型定义 =====================
interface PurchasePaymentItem {
  id: number //
  orderType: string // 单据类型
  orgName: string //所属组织
  supplierName: string //供应商
  orderDate: string // 单据时间
  orderNo: string // 单据编号
  orderAmount: number // 单据金额
  actualAmount: number // 实际金额
  paidAmount: number // 单据付款
  payableBalance: number // 应付款余额
  paymentRate: number // 付款率
  writeOffStatus: string // 核销状态
  remark: string // 备注信息
}

interface SearchForm {
  supplierName: string
  supplierId: string
  orderNo: string
  orderType: string
  orderStartDate: string
  orderEndDate: string
  writeOffStatus: string
}

// ===================== 2. 搜索相关 =====================

// 页面内部使用的搜索表单（最终用于拼接口参数）
const searchForm = reactive<SearchForm>({
  supplierName: '',
  supplierId: '',
  orderNo: '',
  orderType: '',
  orderStartDate: '',
  orderEndDate: '',
  writeOffStatus: ''
})

// GoodSearch 组件配置
const goodSearchConfig = ref<GoodSearchConfig>({
  inline: false, // 弹窗模式
  showGoods: false,
  showNumber: true, // 单据编号
  showSupplier: true, // 供应商
  showCustomer: false,
  showPeople: false,
  showBillDate: true, // 单据日期
  showArrivalDate: false,
  showUser: false,
  showExamine: false,
  showState: false,
  showRemark: false,
  customFields: [
    {
      key: 'orderType',
      type: 'select',
      label: '单据类型',
      options: [
        { label: '采购单', value: '采购单' },
        { label: '采购退货单', value: '采购退货单' }
      ]
    },
    {
      key: 'writeOffStatus',
      type: 'select',
      label: '核销状态',
      options: [
        { label: '未核销', value: '未核销' },
        { label: '部分核销', value: '部分核销' },
        { label: '已核销', value: '已核销' }
      ]
    }
  ]
})

// GoodSearch 的表单 v-model
const goodSearchForm = ref<SearchFormData>({
  supplier: null,
  number: '',
  startTime: '',
  endTime: '',
  orderType: '',
  writeOffStatus: ''
})

// 处理搜索事件：同步 GoodSearch 数据到 searchForm，然后调接口
const handleSearch = (formData: SearchFormData) => {
  if (formData.supplier) {
    searchForm.supplierName = formData.supplier as string
  } else {
    searchForm.supplierName = ''
    searchForm.supplierId = ''
  }

  searchForm.orderNo = formData.number || ''
  searchForm.orderStartDate = formData.startTime || ''
  searchForm.orderEndDate = formData.endTime || ''
  searchForm.orderType = (formData.orderType as string) || ''
  searchForm.writeOffStatus = (formData.writeOffStatus as string) || ''

  currentPage.value = 1
  fetchTableData()
  ElMessage.success('搜索完成')
}

// 重置搜索条件
const handleResetSearch = () => {
  Object.assign(searchForm, {
    supplierName: '',
    supplierId: '',
    orderNo: '',
    orderType: '',
    orderStartDate: '',
    orderEndDate: '',
    writeOffStatus: ''
  })
  Object.assign(goodSearchForm.value, {
    supplier: null,
    number: '',
    startTime: '',
    endTime: '',
    orderType: '',
    writeOffStatus: ''
  })
}

// 刷新：重置搜索 + 回到第一页 + 重新拉数据
const handleRefresh = () => {
  handleResetSearch()
  currentPage.value = 1
  fetchTableData()
  ElMessage.success('数据已刷新')
}

// ===================== 3. 表格 & 分页状态 =====================

const tableData = ref<PurchasePaymentItem[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)

// ===================== 4. 工具函数 =====================

// 格式化金额
const formatCurrency = (value: number | string | null | undefined) => {
  const num = Number(value) || 0
  return (
    '¥' +
    num.toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    })
  )
}

// 获取金额显示样式
const getAmountClass = (row: PurchasePaymentItem) => {
  return row.orderType === '采购退货单' ? 'negative-amount' : 'positive-amount'
}

// 把 searchForm 转成后端需要的查询参数
const buildSearchParams = (): Partial<ProcurementPaymentFormUsingGetParams> => {
  const params: Partial<ProcurementPaymentFormUsingGetParams> = {}

  if (searchForm.supplierName) {
    params.supplier = searchForm.supplierName
  }
  if (searchForm.orderNo) {
    params.number = searchForm.orderNo
  }
  if (searchForm.orderStartDate) {
    params.startTime = searchForm.orderStartDate
  }
  if (searchForm.orderEndDate) {
    params.endTime = searchForm.orderEndDate
  }
  if (searchForm.orderType) {
    params.type = searchForm.orderType as ProcurementPaymentFormUsingGetParams['type']
  }
  if (searchForm.writeOffStatus) {
    let n: 0 | 1 | 2 | undefined
    if (searchForm.writeOffStatus === '未核销') n = 0
    else if (searchForm.writeOffStatus === '部分核销') n = 1
    else if (searchForm.writeOffStatus === '已核销') n = 2
    if (n !== undefined) params.nucleus = n
  }

  return params
}

// 把后端的每一行 row 映射成表格用的 PurchasePaymentItem
const mapBackendRowToItem = (item: any): PurchasePaymentItem => {
  // 单据类型：后端已经直接给了 "采购单" / "采购退货单"
  const orderType = typeof item.type === 'string' ? item.type : String(item.type ?? '')

  // 金额相关，全部转成 number，避免字符串影响
  const orderAmount = Number(item.total ?? 0) // 单据金额
  const actualAmount = Number(item.actual ?? orderAmount) // 实际金额
  const paidAmount = Number(item.payment ?? 0) // 单据付款
  const payableBalance = Number(item.balance != null ? item.balance : orderAmount - paidAmount) // 应付款余额

  // 付款率：后端给的是 "100%" 字符串 → 转成数字 100 给 el-progress 用
  let paymentRate = 0
  if (typeof item.rate === 'string') {
    const num = parseFloat(item.rate.replace('%', ''))
    paymentRate = isNaN(num) ? 0 : num
  } else if (typeof item.rate === 'number') {
    paymentRate = item.rate
  } else if (orderAmount) {
    paymentRate = Math.round((paidAmount / orderAmount) * 100)
  }

  // 核销状态：由 nucleus 数字转换
  let writeOffStatus = ''
  if (typeof item.nucleus === 'number') {
    switch (item.nucleus) {
      case 0:
        writeOffStatus = '未核销'
        break
      case 1:
        writeOffStatus = '部分核销'
        break
      case 2:
        writeOffStatus = '已核销'
        break
    }
  }

  const row: PurchasePaymentItem = {
    id: item.id ?? 0,
    orderType,
    // 所属组织：后端字段是 frame
    orgName: item.frame ?? '',
    // 供应商
    supplierName: item.supplier ?? '',
    // 单据时间
    orderDate: item.time ?? '',
    // 单据编号
    orderNo: item.number ?? '',
    // 金额相关
    orderAmount,
    actualAmount,
    paidAmount,
    payableBalance,
    paymentRate,
    writeOffStatus,
    // 备注：后端字段 data
    remark: item.data ?? ''
  }

  return row
}

// ===================== 5. 调用接口获取数据 =====================

const fetchTableData = async () => {
  loading.value = true
  try {
    const params: ProcurementPaymentFormUsingGetParams = {
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      ...buildSearchParams()
    }

    const res = await procurementPaymentFormUsingGet({ params })

    console.log('【采购付款表接口返回】', res)

    const anyRes: any = res
    let pageWrapper: PageDTO7 | undefined

    // 兼容三种常见结构：JsonVO<JsonVO<PageDTO7>> / JsonVO<PageDTO7> / PageDTO7
    if (anyRes?.data?.data?.rows || anyRes?.data?.data?.total != null) {
      pageWrapper = anyRes.data.data as PageDTO7
    } else if (anyRes?.data?.rows || anyRes?.data?.total != null) {
      pageWrapper = anyRes.data as PageDTO7
    } else if (anyRes?.rows || anyRes?.total != null) {
      pageWrapper = anyRes as PageDTO7
    } else {
      console.warn('⚠ 未识别的分页结构，请查看上面的接口返回 log', anyRes)
    }

    const list = pageWrapper?.rows ?? []
    const totalCount = pageWrapper?.total ?? 0

    console.log('【解析后的 rows】', list, 'total =', totalCount)

    tableData.value = list.map((item: any) => mapBackendRowToItem(item))
    total.value = totalCount
  } catch (error: any) {
    console.error('获取采购付款表失败', error)
    ElMessage.error(error?.message || '获取采购付款表失败')
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// ===================== 6. 导出 =====================

const handleExport = async () => {
  try {
    ElMessage.info('正在发送导出请求，请稍候...')

    const params: ProcurementPaymentFormOpenApiExportUsingGetParams = {
      // 👉 如果你希望导出“当前页”
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      // 👉 如果希望导出“全部数据”，可以改成 pageIndex:1, pageSize:99999
      ...buildSearchParams()
    }

    const res = await procurementPaymentFormOpenApiExportUsingGet({ params })

    // 这个接口的返回类型是 request<string>，一般是：
    // 1）返回一个简单的提示字符串，比如 "OK" / "success"
    // 2）返回一个下载地址
    console.log('【采购付款表导出接口返回】', res)

    ElMessage.success('导出任务已提交，请在后端查看打印结果')
  } catch (error: any) {
    console.error('导出采购付款表失败', error)
    ElMessage.error(error?.message || '导出采购付款表失败')
  }
}

// 如果后面要改成直接调后端的导出接口，可以这样用：
// const handleExport = async () => {
//   const params: ProcurementPaymentFormOpenApiExportUsingGetParams = {
//     pageIndex: 1,
//     pageSize: 9999,
//     ...buildSearchParams()
//   }
//   const url = await procurementPaymentFormOpenApiExportUsingGet({ params })
//   // 根据你们后端返回的 string 是下载地址还是别的，再做处理
// }

const prepareExportData = () => {
  const flatData: any[] = []

  tableData.value.forEach((item) => {
    flatData.push({
      单据类型: item.orderType,
      所属组织: item.orgName,
      供应商: item.supplierName,
      单据时间: item.orderDate,
      单据编号: item.orderNo,
      单据金额: item.orderAmount,
      实际金额: item.actualAmount,
      单据付款: item.paidAmount,
      应付款余额: item.payableBalance,
      付款率: `${item.paymentRate}%`,
      核销状态: item.writeOffStatus,
      备注信息: item.remark
    })
  })

  return flatData
}

const downloadCSV = (data: any[]) => {
  if (!data.length) return

  const headers = Object.keys(data[0]).join(',')
  const rows = data.map((row) =>
    Object.values(row)
      .map((value) => `"${value ?? ''}"`)
      .join(',')
  )

  const csvContent = [headers, ...rows].join('\n')
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  const url = URL.createObjectURL(blob)

  link.setAttribute('href', url)
  link.setAttribute('download', `采购付款表_${new Date().toISOString().split('T')[0]}.csv`)
  link.style.visibility = 'hidden'

  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

// ===================== 7. 统计 & 分页 =====================

// 应付款总余额（按当前页统计）
const payableTotalBalance = computed(() => {
  return tableData.value.reduce((sum, item) => sum + (item.payableBalance || 0), 0)
})

// 分页事件：修改后要重新调接口
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchTableData()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchTableData()
}

// ===================== 8. 初始化 =====================
onMounted(() => {
  fetchTableData()
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

.action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
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

/* 金额样式 */
/* .positive-amount {
  color: #67c23a;
  font-weight: 600;
}

.negative-amount {
  color: #f56c6c;
  font-weight: 600;
}

.payable-balance {
  color: #e6a23c;
  font-weight: 600;
} */

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

/* .payable-amount {
  color: #e6a23c;
  border-color: #e6a23c;
  background-color: #fdf6ec;
} */

.amount-value:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border-color: #c0c4cc;
}

/* GoodSearch 组件样式调整 */
:deep(.good-search-custom .el-button) {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sys.area {
    padding: 8px;
    height: calc(100vh - 16px);
  }

  .operation-bar {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
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
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .amount-summary {
    justify-content: center;
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
