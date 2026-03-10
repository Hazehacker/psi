<template>
  <div class="sys area">
    <!-- 操作按钮区域 -->
    <div class="operation-bar">
      <div class="operation-left">
        <!-- 搜索按钮 - 小型化 -->
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
        <!-- 表格列保持不变 -->
        <!-- ... 原有的表格列定义 ... -->

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
            <span class="amount-label">采购总金额：</span>
            <span class="amount-value purchase-amount">{{
              formatCurrency(purchaseTotalAmount)
            }}</span>
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
  procurementDetailFormQueryUsingGet,
  procurementDetailFormOpenApiExportUsingGet
} from '@/apis/report/purchase/caigoumingxibiao'

import type {
  ProcurementDetailFormQueryUsingGetParams,
  PageDTO9,
  ProcurementDetailFormOpenApiExportUsingGetParams
} from '@/apis/report/purchase/types'

// ===================== 1. 搜索配置 =====================
const searchConfig: GoodSearchConfig = {
  inline: false, // 弹窗模式

  showGoods: true,
  showNumber: true,
  showSupplier: true,
  showCustomer: false,
  showPeople: false,
  showBillDate: true,
  showArrivalDate: false,
  showUser: false,
  showExamine: false,
  showState: false,
  showRemark: true,
  showWarehouse: true, // 仓库选择

  customFields: [
    {
      key: 'orderType',
      type: 'select',
      label: '单据类型',
      options: [
        { label: '采购单', value: '采购单' },
        { label: '采购退货单', value: '采购退货单' }
      ]
    }
  ]
}

// 搜索表单数据
const searchFormData = reactive<SearchFormData>({
  goods: '',
  number: '',
  supplier: '',
  customer: null,
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

// ===================== 2. 表格行类型 & 状态 =====================
type PurchaseDetailRow = {
  id?: number | string
  unitType?: string
  orgName?: string
  supplierName?: string
  orderDate?: string
  orderNo?: string
  orderType?: string
  productName?: string
  auxiliaryAttr?: string
  warehouse?: string
  unit?: string
  unitPrice?: number
  quantity?: number
  discountAmount?: number
  amount?: number
  taxAmount?: number
  totalAmount?: number
  remark?: string
}

const tableData = ref<PurchaseDetailRow[]>([]) // 当前页数据
const total = ref(0) // 总条数

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)

// ===================== 3. 工具函数 =====================
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

const getAmountClass = (row: PurchaseDetailRow) => {
  return row.orderType === '采购退货单' ? 'negative-amount' : 'positive-amount'
}

const getTotalAmountClass = (row: PurchaseDetailRow) => {
  return row.orderType === '采购退货单' ? 'negative-amount' : 'positive-amount'
}

// 把搜索条件转成后端的参数类型 ProcurementDetailFormQueryUsingGetParams
const buildSearchParams = (): Partial<ProcurementDetailFormQueryUsingGetParams> => {
  const params: Partial<ProcurementDetailFormQueryUsingGetParams> = {}

  // 供应商 -> supplier
  if (searchFormData.supplier) {
    params.supplier = searchFormData.supplier as string
  }

  // 商品名称 -> name
  if (searchFormData.goods) {
    params.name = searchFormData.goods as string
  }

  // 单据编号 -> number
  if (searchFormData.number) {
    params.number = searchFormData.number as string
  }

  // 仓库 -> warehouse
  if (searchFormData.warehouse) {
    params.warehouse = searchFormData.warehouse as string
  }

  // 备注信息 -> data
  if (searchFormData.data) {
    params.data = searchFormData.data as string
  }

  // 开始日期 -> startTime
  if (searchFormData.startTime) {
    params.startTime = searchFormData.startTime as string
  }

  // 结束日期 -> endTime
  if (searchFormData.endTime) {
    params.endTime = searchFormData.endTime as string
  }

  // 单据类型："采购单"/"采购退货单" -> type: 0 | 1
  // 0 = 采购退货单, 1 = 采购单
  if (searchFormData.orderType) {
    let t: 0 | 1 | undefined
    if (searchFormData.orderType === '采购退货单') t = 0
    if (searchFormData.orderType === '采购单') t = 1
    if (t !== undefined) {
      params.type = t
    }
  }

  return params
}

// ===================== 4. 调用接口获取数据 =====================
const fetchTableData = async () => {
  loading.value = true
  try {
    const params: ProcurementDetailFormQueryUsingGetParams = {
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      ...buildSearchParams()
    }

    const res = await procurementDetailFormQueryUsingGet({ params })

    // 👉 关键：先打出来看真实结构
    console.log('【采购明细接口返回】', res)

    const anyRes: any = res

    // 这里兼容三种可能：
    // 1）JsonVO<JsonVOPageDTO9>：res.data.data 是 PageDTO9
    // 2）JsonVOPageDTO9：      res.data     是 PageDTO9
    // 3）直接 PageDTO9：       res          是 PageDTO9
    let pageWrapper: PageDTO9 | undefined

    if (anyRes?.data?.data?.rows || anyRes?.data?.data?.total != null) {
      // 形如 { code, data: { code, data: { pageIndex, rows, total }, message }, message }
      pageWrapper = anyRes.data.data as PageDTO9
    } else if (anyRes?.data?.rows || anyRes?.data?.total != null) {
      // 形如 { code, data: { pageIndex, rows, total }, message }
      pageWrapper = anyRes.data as PageDTO9
    } else if (anyRes?.rows || anyRes?.total != null) {
      // 形如 { pageIndex, rows, total }
      pageWrapper = anyRes as PageDTO9
    } else {
      console.warn('⚠ 未识别的分页结构，请看上面的接口返回 log', anyRes)
    }

    const list = pageWrapper?.rows ?? []
    const totalCount = pageWrapper?.total ?? 0

    // 再打印一下，方便你看 list 里是不是有东西
    console.log('【解析后的 rows】', list, 'total =', totalCount)

    tableData.value = list.map((item: any) => {
      const row: PurchaseDetailRow = {
        // 这些后端没有给，就先留空或以后按需要补
        id: undefined,
        //TODO 后端没有给这些字段，先写死，后续如果接口加上了再改
        unitType: '采购单位', // 如果后端以后给了再改
        orgName: '', //所属组织，后端没有给

        // ====== 一一对应后端字段 ======
        // 供应商
        supplierName: item.supplier,
        // 单据时间
        orderDate: item.time,
        // 单据编号
        orderNo: item.number,
        // 单据类型（文本）
        orderType: item.type === 0 ? '采购退货单' : '采购单', // 如果你们约定相反，就把这行反过来

        // 商品名称
        productName: item.name,
        // 辅助属性
        auxiliaryAttr: item.attr,
        // 仓库
        warehouse: item.warehouse,
        // 单位
        unit: item.unit,

        // 单价
        unitPrice: item.price,
        // 数量
        quantity: item.nums,
        // 折扣额
        discountAmount: item.dsc,
        // 金额
        amount: item.total,
        // 税额
        taxAmount: item.tat,
        // 价税合计
        totalAmount: item.tpt,
        // 备注信息
        remark: item.data
      }

      return row
    })

    total.value = totalCount
  } catch (error: any) {
    console.error('获取采购明细失败', error)
    ElMessage.error(error?.message || '获取采购明细失败')
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// ===================== 5. 搜索 & 刷新 =====================
const handleSearch = (formData: SearchFormData) => {
  Object.assign(searchFormData, formData)
  currentPage.value = 1
  fetchTableData()
}

const handleRefresh = () => {
  // 清空搜索条件
  Object.keys(searchFormData).forEach((key) => {
    // @ts-ignore
    searchFormData[key] = ''
  })
  searchFormData.customer = null
  searchFormData.people = null
  searchFormData.user = null

  currentPage.value = 1
  fetchTableData()
  ElMessage.success('数据已刷新')
}

// ===================== 6. 导出 =====================
const handleExport = async () => {
  try {
    ElMessage.info('正在导出数据...')

    // 公共查询条件（备注、日期、供应商、商品、编号、仓库、单据类型）
    const commonParams = buildSearchParams()

    // ✅ 这里决定导出范围：
    // 如果你希望导出“当前页”，就用 currentPage / pageSize
    // 如果你希望导出“当前条件下的所有数据”，可以改成 pageIndex:1, pageSize:99999
    const params: ProcurementDetailFormOpenApiExportUsingGetParams = {
      pageIndex: 1,
      pageSize: 99999,
      ...(commonParams as any)
    }

    // 调用后端导出接口（返回 Blob 或 JsonVO<Blob> 之类的）
    const res = await procurementDetailFormOpenApiExportUsingGet({ params })
    console.log('【采购明细表导出接口返回】', res)

    const anyRes: any = res
    // 尽量兼容几种返回方式：直接 Blob / { data: Blob } / 其他
    const blob: Blob =
      anyRes instanceof Blob
        ? anyRes
        : anyRes?.data instanceof Blob
          ? anyRes.data
          : new Blob([anyRes], {
              type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
            })

    // 生成下载链接，触发浏览器下载
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `采购明细表_${new Date().toISOString().split('T')[0]}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    URL.revokeObjectURL(url)

    ElMessage.success('导出成功，已开始下载文件')
  } catch (error: any) {
    console.error('导出采购明细失败', error)
    ElMessage.error(error?.message || '导出采购明细失败')
  }
}
// 现在先用前端导出 CSV，如果以后要对接后端 Blob 导出可以改这里
// const handleExport = async () => {
//   try {
//     const params: ProcurementDetailFormQueryUsingGetParams = {
//       pageIndex: 1,
//       pageSize: 9999,
//       ...buildSearchParams()
//     }
//     const blob = await procurementDetailFormOpenApiExportUsingGet({ params })
//     // 这里把 blob 保存成本地文件即可
//   } catch (e) {
//     ElMessage.error('导出失败')
//   }
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
      商品名称: item.productName,
      辅助属性: item.auxiliaryAttr,
      仓库: item.warehouse,
      单位: item.unit,
      单价: item.unitPrice,
      数量: item.quantity,
      折扣额: item.discountAmount,
      金额: item.amount,
      税额: item.taxAmount,
      价税合计: item.totalAmount,
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
  link.setAttribute('download', `采购明细表_${new Date().toISOString().split('T')[0]}.csv`)
  link.style.visibility = 'hidden'

  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

// ===================== 7. 金额统计（按当前页数据） =====================
const purchaseTotalAmount = computed(() => {
  return tableData.value
    .filter((item) => item.orderType === '采购单')
    .reduce((sum, item) => sum + (Number(item.totalAmount) || 0), 0)
})

const returnTotalAmount = computed(() => {
  return Math.abs(
    tableData.value
      .filter((item) => item.orderType === '采购退货单')
      .reduce((sum, item) => sum + (Number(item.totalAmount) || 0), 0)
  )
})

// ===================== 8. 分页事件 =====================
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchTableData()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchTableData()
}

// ===================== 9. 初始化 =====================
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

/* 金额样式 */
/* .positive-amount {
  color: #67c23a;
  font-weight: bold;
}

.negative-amount {
  color: #f56c6c;
  font-weight: bold;
}

.purchase-amount {
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
