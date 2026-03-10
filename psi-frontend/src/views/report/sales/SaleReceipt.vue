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

        <!-- 客户列 -->
        <el-table-column prop="customerName" label="客户" width="150">
          <template #default="scope">
            {{ scope.row.customerName }}
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

        <!-- 单据收款 -->
        <el-table-column prop="receivedAmount" label="单据收款" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.receivedAmount) }}
          </template>
        </el-table-column>

        <!-- 应收款余额 -->
        <el-table-column prop="receivableBalance" label="应收款余额" width="130" align="right">
          <template #default="scope">
            <span class="receivable-balance">
              {{ formatCurrency(scope.row.receivableBalance) }}
            </span>
          </template>
        </el-table-column>

        <!-- 收款率 -->
        <el-table-column prop="receiptRate" label="收款率" width="100" align="center">
          <template #default="scope">
            <el-progress :percentage="scope.row.receiptRate" :show-text="true" />
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
            <span class="amount-label">应收款总余额：</span>
            <span class="amount-value receivable-amount">{{
              formatCurrency(receivableTotalBalance)
            }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Download, Refresh } from '@element-plus/icons-vue'
import GoodSearch from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent/type'

import {
  salesReceiptUsingGet,
  salesReceiptOpenApiExportUsingGet
} from '@/apis/report/sales/xiaoshoushoukuanbiao'
import type {
  SalesReceiptUsingGetParams,
  SalesReceiptOpenApiExportUsingGetParams,
  PageDTO13
} from '@/apis/report/sales/types'

// ===================== 1. 行数据类型（使用页面自己的命名） =====================
interface SalesReceiptRow {
  id?: number | string
  orderType: string // 单据类型  ← 后端：type
  orgName: string // 所属组织  ← 后端：frame
  customerName: string // 客户      ← 后端：customer
  orderDate: string // 单据时间  ← 后端：time
  orderNo: string | number // 单据编号  ← 后端：number
  orderAmount: number // 单据金额  ← 后端：total
  actualAmount: number // 实际金额  ← 后端：actual
  receivedAmount: number // 单据收款  ← 后端：invoiceCollection
  receivableBalance: number // 应收款余额← 后端：accountsReceivableBalance
  receiptRate: number // 收款率    ← 后端：collectionRate
  writeOffStatus: string // 核销状态文本：未核销/部分核销/已核销 ← 后端：nucleus(0/1/2)
  remark: string // 备注信息  ← 后端：data
}

// ===================== 2. GoodSearch 配置 =====================
const goodSearchConfig = ref<GoodSearchConfig>({
  inline: false, // 弹框模式
  showGoods: false,
  showNumber: true, // 单据编号
  showSupplier: false,
  showCustomer: true, // 客户
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
        { label: '销售单', value: '销售单' },
        { label: '销售退货单', value: '销售退货单' }
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

// GoodSearch 双向绑定的数据
const goodSearchForm = ref<SearchFormData>({
  customer: null,
  number: '',
  startTime: '',
  endTime: '',
  orderType: '',
  writeOffStatus: ''
})

// ===================== 3. 表格 & 分页状态 =====================
const tableData = ref<SalesReceiptRow[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)

// ===================== 4. 工具函数 =====================
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

// 销售退货单金额为红色，销售单为绿色
const getAmountClass = (row: SalesReceiptRow) => {
  return row.orderType === '销售退货单' ? 'negative-amount' : 'positive-amount'
}

// 当前页应收款总余额
const receivableTotalBalance = computed(() => {
  return tableData.value.reduce((sum, item) => sum + (Number(item.receivableBalance) || 0), 0)
})

// 为了不改 template，这里让 pagedTableData 直接等于接口返回的数据
const pagedTableData = computed(() => tableData.value)

// 前端搜索字段 → 后端查询参数适配
const buildSearchParams = (): Partial<SalesReceiptUsingGetParams> => {
  const params: Partial<SalesReceiptUsingGetParams> = {}
  const form = goodSearchForm.value

  // 客户
  if (form.customer) {
    params.customer = form.customer as string
  }

  // 单据编号
  if (form.number) {
    params.number = form.number as string
  }

  // 单据开始/结束时间
  if (form.startTime) {
    params.begintime = form.startTime as string
  }
  if (form.endTime) {
    params.endtime = form.endTime as string
  }

  // 单据类型：直接用“销售单/销售退货单”
  if (form.orderType) {
    params.type = form.orderType as string
  }

  // 核销状态：文案 → 数字 0/1/2
  if (form.writeOffStatus) {
    let nucleus: 0 | 1 | 2 | undefined
    if (form.writeOffStatus === '未核销') nucleus = 0
    else if (form.writeOffStatus === '部分核销') nucleus = 1
    else if (form.writeOffStatus === '已核销') nucleus = 2

    if (nucleus !== undefined) {
      params.nucleus = nucleus
    }
  }

  return params
}

// ===================== 5. 调用查询接口，做字段映射 =====================
const fetchTableData = async () => {
  loading.value = true
  try {
    const params: SalesReceiptUsingGetParams = {
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      ...(buildSearchParams() as any)
    }

    const res = await salesReceiptUsingGet({ params })
    const anyRes: any = res

    let pageWrapper: PageDTO13 | undefined

    // 兼容多种 JsonVO 包装
    if (anyRes?.data?.data?.rows || anyRes?.data?.data?.total != null) {
      pageWrapper = anyRes.data.data as PageDTO13
    } else if (anyRes?.data?.rows || anyRes?.data?.total != null) {
      pageWrapper = anyRes.data as PageDTO13
    } else if (anyRes?.rows || anyRes?.total != null) {
      pageWrapper = anyRes as PageDTO13
    } else {
      console.warn('【销售收款表】未识别的分页结构', anyRes)
      pageWrapper = { rows: [], total: 0 } as PageDTO13
    }

    const list = pageWrapper?.rows ?? []
    const totalCount = pageWrapper?.total ?? 0

    // 这里做后端 rows → 前端 SalesReceiptRow 的字段映射
    tableData.value = list.map((item: any, index: number): SalesReceiptRow => {
      const nucleus = item.nucleus as 0 | 1 | 2 | undefined
      let writeOffStatus = ''
      if (nucleus === 0) writeOffStatus = '未核销'
      else if (nucleus === 1) writeOffStatus = '部分核销'
      else if (nucleus === 2) writeOffStatus = '已核销'

      return {
        id: item.id ?? index,
        orderType: item.type ?? '',
        orgName: item.frame ?? '',
        customerName: item.customer ?? '',
        orderDate: item.time ?? '',
        orderNo: item.number ?? '',
        orderAmount: item.total ?? 0,
        actualAmount: item.actual ?? 0,
        receivedAmount: item.invoiceCollection ?? 0,
        receivableBalance: item.accountsReceivableBalance ?? 0,
        receiptRate: item.collectionRate ?? 0,
        writeOffStatus,
        remark: item.data ?? ''
      }
    })

    total.value = totalCount
  } catch (error: any) {
    console.error('获取销售收款表失败', error)
    ElMessage.error(error?.message || '获取销售收款表失败')
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// ===================== 6. 搜索 / 刷新 / 分页 =====================

// GoodSearch 搜索回调
const handleSearch = (formData: SearchFormData) => {
  Object.assign(goodSearchForm.value, formData)
  currentPage.value = 1
  fetchTableData()
  ElMessage.success('搜索完成')
}

// 重置搜索条件
const handleResetSearch = () => {
  Object.assign(goodSearchForm.value, {
    customer: null,
    number: '',
    startTime: '',
    endTime: '',
    orderType: '',
    writeOffStatus: ''
  })
  ElMessage.info('已重置搜索条件')
}

// 刷新按钮：重置 + 重新查询
const handleRefresh = () => {
  handleResetSearch()
  currentPage.value = 1
  fetchTableData()
  ElMessage.success('数据已刷新')
}

// 分页事件：改变页码/每页条数时重新请求
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchTableData()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchTableData()
}

// ===================== 7. 导出接口 =====================
const handleExport = async () => {
  try {
    ElMessage.info('正在导出数据...')

    const commonParams = buildSearchParams()
    const params: SalesReceiptOpenApiExportUsingGetParams = {
      // 一般导出用“当前条件下的所有数据”，所以给个大 pageSize
      pageIndex: 1,
      pageSize: 99999,
      ...(commonParams as any)
    }

    const res = await salesReceiptOpenApiExportUsingGet({ params })
    const anyRes: any = res

    const blob: Blob =
      anyRes instanceof Blob
        ? anyRes
        : anyRes?.data instanceof Blob
          ? anyRes.data
          : new Blob([anyRes], {
              type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
            })

    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `销售收款表_${new Date().toISOString().split('T')[0]}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    URL.revokeObjectURL(url)

    ElMessage.success('导出成功，已开始下载文件')
  } catch (error: any) {
    console.error('导出销售收款表失败', error)
    ElMessage.error(error?.message || '导出销售收款表失败')
  }
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

.receivable-balance {
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

/* .receivable-amount {
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
