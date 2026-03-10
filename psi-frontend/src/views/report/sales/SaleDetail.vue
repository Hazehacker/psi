<template>
  <div class="sys area">
    <!-- 操作按钮区域 -->
    <div class="operation-bar">
      <div class="operation-left">
        <!-- 搜索按钮 - 使用GoodSearch组件 -->
        <GoodSearch
          :model-value="searchFormData"
          @update:model-value="(val) => Object.assign(searchFormData, val)"
          :config="searchConfig"
          @search="handleSearch"
          class="small-search"
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
        <!-- 单据类型列（由后端 type=0/1 映射） -->
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

        <!-- 商品名称 -->
        <el-table-column prop="productName" label="商品名称" width="150">
          <template #default="scope">
            {{ scope.row.productName }}
          </template>
        </el-table-column>

        <!-- 辅助属性 -->
        <el-table-column prop="auxiliaryAttr" label="辅助属性" width="120">
          <template #default="scope">
            {{ scope.row.auxiliaryAttr }}
          </template>
        </el-table-column>

        <!-- 仓库 -->
        <el-table-column prop="warehouse" label="仓库" width="100" align="center">
          <template #default="scope">
            {{ scope.row.warehouse }}
          </template>
        </el-table-column>

        <!-- 单位 -->
        <el-table-column prop="unit" label="单位" width="80" align="center">
          <template #default="scope">
            {{ scope.row.unit }}
          </template>
        </el-table-column>

        <!-- 单价 -->
        <el-table-column prop="unitPrice" label="单价" width="100" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.unitPrice) }}
          </template>
        </el-table-column>

        <!-- 数量 -->
        <el-table-column prop="quantity" label="数量" width="100" align="right">
          <template #default="scope">
            {{ scope.row.quantity }}
          </template>
        </el-table-column>

        <!-- 折扣额 -->
        <el-table-column prop="discountAmount" label="折扣额" width="100" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.discountAmount) }}
          </template>
        </el-table-column>

        <!-- 金额 -->
        <el-table-column prop="amount" label="金额" width="120" align="right">
          <template #default="scope">
            <span :class="getAmountClass(scope.row)">
              {{ formatCurrency(scope.row.amount) }}
            </span>
          </template>
        </el-table-column>

        <!-- 税额 -->
        <el-table-column prop="taxAmount" label="税额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.taxAmount) }}
          </template>
        </el-table-column>

        <!-- 价税合计 -->
        <el-table-column prop="totalAmount" label="价税合计" width="120" align="right">
          <template #default="scope">
            <span :class="getTotalAmountClass(scope.row)">
              {{ formatCurrency(scope.row.totalAmount) }}
            </span>
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
            <span class="amount-label">销售总金额：</span>
            <span class="amount-value sale-amount">{{ formatCurrency(saleTotalAmount) }}</span>
          </div>
          <div class="amount-item">
            <span class="amount-label">退货总金额：</span>
            <span class="amount-value return-amount">{{ formatCurrency(returnTotalAmount) }}</span>
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
  salesDetailFormQueryUsingGet,
  salesDetailFormOpenApiExportUsingGet
} from '@/apis/report/sales/xiaoshoumingxibiao'
import type {
  SalesDetailFormQueryUsingGetParams,
  SalesDetailFormOpenApiExportUsingGetParams,
  PageDTO14
} from '@/apis/report/sales/types'

// ====================== 1. 类型定义 ======================

interface SalesDetailRow {
  id?: number | string
  unitType: string
  orgName: string
  customerName: string
  orderDate: string
  orderNo: string
  orderType: '销售单' | '销售退货单'
  productName: string
  auxiliaryAttr: string
  warehouse: string
  unit: string
  unitPrice: number
  quantity: number
  discountAmount: number
  amount: number
  taxAmount: number
  totalAmount: number
  remark: string
}

// 后端 type(0/1) -> 前端表格里的中文单据类型
// 0 = 销售退货单, 1 = 销售单
const mapTypeToOrderType = (type: number | null | undefined): '销售单' | '销售退货单' => {
  if (type === 0) return '销售退货单'
  if (type === 1) return '销售单'
  // 兜底：如果后端给了别的值，就当普通销售单
  return '销售单'
}

// 前端搜索里的“单据类型”下拉（销售单/销售退货单） -> 后端的 0/1
const mapOrderTypeToType = (orderType?: string): 0 | 1 | undefined => {
  if (orderType === '销售退货单') return 0
  if (orderType === '销售单') return 1
  return undefined
}

// ====================== 2. GoodSearch 配置 ======================

const searchConfig: GoodSearchConfig = {
  inline: false,

  showGoods: true,
  showNumber: true,
  showSupplier: false,
  showCustomer: true,
  showPeople: false,
  showBillDate: true,
  showArrivalDate: false,
  showUser: false,
  showExamine: false,
  showState: false,
  showRemark: true,
  showWarehouse: true,

  customFields: [
    {
      key: 'orderType',
      type: 'select',
      label: '单据类型',
      options: [
        { label: '销售单', value: '销售单' },
        { label: '销售退货单', value: '销售退货单' }
      ]
    }
  ]
}

const getDefaultSearchFormData = (): SearchFormData => ({
  goods: '',
  number: '',
  supplier: '',
  customer: '',
  people: null,
  user: null,
  startTime: '',
  endTime: '',
  startArrival: '',
  endArrival: '',
  examine: '',
  state: '',
  data: '',
  warehouse: '',
  orderType: ''
})

const searchFormData = reactive<SearchFormData>(getDefaultSearchFormData())

// ====================== 3. 表格 & 分页状态 ======================

const tableData = ref<SalesDetailRow[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)

// ====================== 4. 构造查询参数 & 数据映射 ======================

// 从搜索表单构建后端查询参数
const buildQueryParams = (): Partial<SalesDetailFormQueryUsingGetParams> => {
  const params: Partial<SalesDetailFormQueryUsingGetParams> = {}

  // 客户：GoodSearch 可能给字符串或对象，这里做个兜底
  if (searchFormData.customer) {
    if (typeof searchFormData.customer === 'string') {
      params.customer = searchFormData.customer
    } else {
      const obj = searchFormData.customer as any
      params.customer = obj.name || obj.customerName || ''
    }
  }

  if (searchFormData.data) params.data = searchFormData.data as string
  if (searchFormData.startTime) params.startTime = searchFormData.startTime as string
  if (searchFormData.endTime) params.endTime = searchFormData.endTime as string
  if (searchFormData.goods) params.name = searchFormData.goods as string
  if (searchFormData.number) params.number = searchFormData.number as string
  if (searchFormData.warehouse) params.warehouse = searchFormData.warehouse as string

  const t = mapOrderTypeToType(searchFormData.orderType as string)
  if (t !== undefined) params.type = t

  return params
}

// 后端 row -> 前端表格行
const mapBackendRowToItem = (item: any): SalesDetailRow => {
  return {
    id: item.id ?? undefined,
    unitType: '销售单位', // 后端没给，就写个固定文案
    orgName: item.frame ?? '',
    customerName: item.customer ?? '',
    orderDate: item.time ?? '',
    orderNo: item.number ?? '',
    orderType: mapTypeToOrderType(item.type),
    productName: item.name ?? '',
    auxiliaryAttr: item.attr ?? '',
    warehouse: item.warehouse ?? '',
    unit: item.unit ?? '',
    unitPrice: Number(item.price ?? 0),
    quantity: Number(item.nums ?? 0),
    discountAmount: Number(item.dsc ?? 0),
    amount: Number(item.total ?? 0),
    taxAmount: Number(item.tat ?? 0),
    totalAmount: Number(item.tpt ?? 0),
    remark: item.data ?? ''
  }
}

// ====================== 5. 调用查询接口 ======================

const fetchTableData = async () => {
  loading.value = true
  try {
    const params: SalesDetailFormQueryUsingGetParams = {
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      ...(buildQueryParams() as any)
    }

    const res = await salesDetailFormQueryUsingGet({ params })
    console.log('【销售明细表接口返回】', res)

    const anyRes: any = res
    let pageWrapper: PageDTO14 | undefined

    // 兼容 JsonVO<JsonVO<PageDTO14>> / JsonVO<PageDTO14> / PageDTO14 这几种结构
    if (anyRes?.data?.data?.rows || anyRes?.data?.data?.total != null) {
      pageWrapper = anyRes.data.data as PageDTO14
    } else if (anyRes?.data?.rows || anyRes?.data?.total != null) {
      pageWrapper = anyRes.data as PageDTO14
    } else if (anyRes?.rows || anyRes?.total != null) {
      pageWrapper = anyRes as PageDTO14
    } else {
      console.warn('⚠ 未识别的分页结构，请查看接口返回：', anyRes)
    }

    const rows = pageWrapper?.rows ?? []
    const totalCount = pageWrapper?.total ?? 0

    tableData.value = rows.map((item: any) => mapBackendRowToItem(item))
    total.value = totalCount
  } catch (error: any) {
    console.error('获取销售明细表失败', error)
    ElMessage.error(error?.message || '获取销售明细表失败')
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// ====================== 6. 搜索 / 刷新 ======================

const handleSearch = (formData: SearchFormData) => {
  Object.assign(searchFormData, formData)
  currentPage.value = 1
  fetchTableData()
  ElMessage.success('搜索完成')
}

const handleRefresh = () => {
  Object.assign(searchFormData, getDefaultSearchFormData())
  currentPage.value = 1
  fetchTableData()
  ElMessage.success('数据已刷新')
}

// ====================== 7. 导出接口（走后端 Blob） ======================

const handleExport = async () => {
  try {
    ElMessage.info('正在导出数据...')

    const commonParams = buildQueryParams()
    const params: SalesDetailFormOpenApiExportUsingGetParams = {
      pageIndex: 1,
      pageSize: 99999,
      ...(commonParams as any)
    }

    const res = await salesDetailFormOpenApiExportUsingGet({ params })
    console.log('【销售明细表导出接口返回】', res)

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
    link.download = `销售明细表_${new Date().toISOString().split('T')[0]}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    URL.revokeObjectURL(url)

    ElMessage.success('导出成功，已开始下载文件')
  } catch (error: any) {
    console.error('导出销售明细表失败', error)
    ElMessage.error(error?.message || '导出销售明细表失败')
  }
}

// ====================== 8. 金额展示 & 合计 ======================

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

const getAmountClass = (row: SalesDetailRow) => {
  return row.orderType === '销售退货单' ? 'negative-amount' : 'positive-amount'
}

const getTotalAmountClass = (row: SalesDetailRow) => {
  return row.orderType === '销售退货单' ? 'negative-amount' : 'positive-amount'
}

// 这里的合计是“当前页”的销售/退货总金额（跟你之前一样）
const saleTotalAmount = computed(() =>
  tableData.value
    .filter((item) => item.orderType === '销售单')
    .reduce((sum, item) => sum + (item.totalAmount || 0), 0)
)

const returnTotalAmount = computed(() =>
  Math.abs(
    tableData.value
      .filter((item) => item.orderType === '销售退货单')
      .reduce((sum, item) => sum + (item.totalAmount || 0), 0)
  )
)

// ====================== 9. 分页事件 ======================

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchTableData()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchTableData()
}

// ====================== 10. 初始化 ======================

onMounted(() => {
  // 默认就按空条件拉一页数据
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

/* 金额样式
.positive-amount {
  color: #67c23a;
  font-weight: bold;
}

.negative-amount {
  color: #f56c6c;
  font-weight: bold;
}

.sale-amount {
  color: #67c23a;
  border-color: #67c23a !important;
  background-color: #f0f9eb !important;
}

.return-amount {
  color: #f56c6c;
  border-color: #f56c6c !important;
  background-color: #fef0f0 !important;
} */
</style>
