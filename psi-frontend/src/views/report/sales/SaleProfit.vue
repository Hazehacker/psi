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
        <!-- 单据类型 -->
        <el-table-column prop="orderType" label="单据类型" width="100" align="center">
          <template #default="scope">
            {{ scope.row.orderType }}
          </template>
        </el-table-column>

        <!-- 所属组织 -->
        <el-table-column prop="orgName" label="所属组织" width="120" align="center" />

        <!-- 客户 -->
        <el-table-column prop="customerName" label="客户" width="150" />

        <!-- 单据时间 -->
        <el-table-column prop="orderDate" label="单据时间" width="120" align="center" />

        <!-- 单据编号 -->
        <el-table-column prop="orderNo" label="单据编号" width="150" />

        <!-- 商品名称 - 明细显示 -->
        <el-table-column v-if="showDetails" prop="productName" label="商品名称" width="150" />

        <!-- 辅助属性 - 明细显示 -->
        <el-table-column v-if="showDetails" prop="auxiliaryAttr" label="辅助属性" width="120" />

        <!-- 单位 - 明细显示 -->
        <el-table-column v-if="showDetails" prop="unit" label="单位" width="80" align="center" />

        <!-- 单价 - 明细显示 -->
        <el-table-column v-if="showDetails" prop="unitPrice" label="单价" width="100" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.unitPrice) }}
          </template>
        </el-table-column>

        <!-- 数量 - 明细显示 -->
        <el-table-column
          v-if="showDetails"
          prop="quantity"
          label="数量"
          width="100"
          align="right"
        />

        <!-- 折扣额 - 明细显示 -->
        <el-table-column
          v-if="showDetails"
          prop="discountAmount"
          label="折扣额"
          width="100"
          align="right"
        >
          <template #default="scope">
            {{ formatCurrency(scope.row.discountAmount) }}
          </template>
        </el-table-column>

        <!-- 金额 - 明细显示 -->
        <el-table-column v-if="showDetails" prop="amount" label="金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.amount) }}
          </template>
        </el-table-column>

        <!-- 税额 - 明细显示 -->
        <el-table-column v-if="showDetails" prop="taxAmount" label="税额" width="100" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.taxAmount) }}
          </template>
        </el-table-column>

        <!-- 价税合计 - 明细显示 -->
        <el-table-column
          v-if="showDetails"
          prop="totalWithTax"
          label="价税合计"
          width="120"
          align="right"
        >
          <template #default="scope">
            {{ formatCurrency(scope.row.totalWithTax) }}
          </template>
        </el-table-column>

        <!-- 以下为非明细列，始终显示 -->
        <!-- 单据金额 -->
        <el-table-column prop="orderAmount" label="单据金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.orderAmount) }}
          </template>
        </el-table-column>

        <!-- 优惠金额 -->
        <el-table-column prop="discountTotal" label="优惠金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.discountTotal) }}
          </template>
        </el-table-column>

        <!-- 实际金额 -->
        <el-table-column prop="actualAmount" label="实际金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.actualAmount) }}
          </template>
        </el-table-column>

        <!-- 成本 -->
        <el-table-column prop="cost" label="成本" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.cost) }}
          </template>
        </el-table-column>

        <!-- 毛利润 -->
        <el-table-column prop="grossProfit" label="毛利润" width="120" align="right">
          <template #default="scope">
            <span :class="getProfitClass(scope.row.grossProfit)">
              {{ formatCurrency(scope.row.grossProfit) }}
            </span>
          </template>
        </el-table-column>

        <!-- 毛利率 -->
        <el-table-column prop="grossProfitRate" label="毛利率" width="100" align="right">
          <template #default="scope">
            <span :class="getProfitClass(scope.row.grossProfitRate)">
              {{ formatPercentage(scope.row.grossProfitRate) }}
            </span>
          </template>
        </el-table-column>

        <!-- 单据费用 -->
        <el-table-column prop="orderFee" label="单据费用" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.orderFee) }}
          </template>
        </el-table-column>

        <!-- 净利润 -->
        <el-table-column prop="netProfit" label="净利润" width="120" align="right">
          <template #default="scope">
            <span :class="getProfitClass(scope.row.netProfit)">
              {{ formatCurrency(scope.row.netProfit) }}
            </span>
          </template>
        </el-table-column>

        <!-- 净利率 -->
        <el-table-column prop="netProfitRate" label="净利率" width="100" align="right">
          <template #default="scope">
            <span :class="getProfitClass(scope.row.netProfitRate)">
              {{ formatPercentage(scope.row.netProfitRate) }}
            </span>
          </template>
        </el-table-column>

        <!-- 核销金额 -->
        <el-table-column prop="verificationAmount" label="核销金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.verificationAmount) }}
          </template>
        </el-table-column>

        <!-- 核销状态 -->
        <el-table-column prop="verificationStatus" label="核销状态" width="100" align="center">
          <template #default="scope">
            {{ scope.row.verificationStatus }}
          </template>
        </el-table-column>

        <!-- 制单人 -->
        <el-table-column prop="createdBy" label="制单人" width="100" align="center" />

        <!-- 关联人员 -->
        <el-table-column prop="relatedPerson" label="关联人员" width="100" align="center" />

        <!-- 备注信息 -->
        <el-table-column prop="remark" label="备注信息" min-width="150" />
      </el-table>

      <!-- 分页区域 -->
      <div class="pagination-container pagination-left">
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

// 搜索组件引用
const goodSearchRef = ref()

// 是否显示明细
const showDetails = ref(false)

// 扁平化数据 - 销售利润表数据
const originalTableData = ref([
  {
    id: 1,
    orderType: '销售单',
    orgName: '总部',
    customerName: '上海客户A',
    orderDate: '2023-10-01',
    orderNo: 'SO20231001001',
    productName: '成品A',
    auxiliaryAttr: '规格A',
    unit: '件',
    unitPrice: 500,
    quantity: 100,
    discountAmount: 1000,
    amount: 50000,
    taxAmount: 6500,
    totalWithTax: 56500,
    orderAmount: 56500,
    discountTotal: 1000,
    actualAmount: 55500,
    cost: 35000,
    grossProfit: 20500,
    grossProfitRate: 0.3694,
    orderFee: 500,
    netProfit: 20000,
    netProfitRate: 0.3604,
    verificationAmount: 55500,
    verificationStatus: '已核销',
    createdBy: '张三',
    relatedPerson: '李四',
    remark: '正常销售订单'
  },
  {
    id: 2,
    orderType: '销售退货单',
    orgName: '分部',
    customerName: '北京客户B',
    orderDate: '2023-10-05',
    orderNo: 'SR20231005001',
    productName: '成品B',
    auxiliaryAttr: '型号B',
    unit: '套',
    unitPrice: 400,
    quantity: -50,
    discountAmount: -500,
    amount: -20000,
    taxAmount: -2600,
    totalWithTax: -22600,
    orderAmount: -22600,
    discountTotal: -500,
    actualAmount: -22100,
    cost: -15000,
    grossProfit: -7100,
    grossProfitRate: -0.3212,
    orderFee: 200,
    netProfit: -7300,
    netProfitRate: -0.3303,
    verificationAmount: -22100,
    verificationStatus: '已核销',
    createdBy: '王五',
    relatedPerson: '赵六',
    remark: '质量问题退货'
  },
  {
    id: 3,
    orderType: '销售单',
    orgName: '总部',
    customerName: '广州客户C',
    orderDate: '2023-10-08',
    orderNo: 'SO20231008001',
    productName: '半成品C',
    auxiliaryAttr: '规格C',
    unit: '套',
    unitPrice: 600,
    quantity: 80,
    discountAmount: 1200,
    amount: 48000,
    taxAmount: 6240,
    totalWithTax: 54240,
    orderAmount: 54240,
    discountTotal: 1200,
    actualAmount: 53040,
    cost: 33600,
    grossProfit: 19440,
    grossProfitRate: 0.3665,
    orderFee: 800,
    netProfit: 18640,
    netProfitRate: 0.3514,
    verificationAmount: 30000,
    verificationStatus: '部分核销',
    createdBy: '孙七',
    relatedPerson: '周八',
    remark: '分批付款'
  },
  {
    id: 4,
    orderType: '销售单',
    orgName: '分部',
    customerName: '深圳客户D',
    orderDate: '2023-10-12',
    orderNo: 'SO20231012001',
    productName: '成品D',
    auxiliaryAttr: '规格D',
    unit: '台',
    unitPrice: 1200,
    quantity: 30,
    discountAmount: 1800,
    amount: 36000,
    taxAmount: 4680,
    totalWithTax: 40680,
    orderAmount: 40680,
    discountTotal: 1800,
    actualAmount: 38880,
    cost: 25200,
    grossProfit: 13680,
    grossProfitRate: 0.3519,
    orderFee: 400,
    netProfit: 13280,
    netProfitRate: 0.3415,
    verificationAmount: 0,
    verificationStatus: '未核销',
    createdBy: '吴九',
    relatedPerson: '郑十',
    remark: '新客户订单'
  }
])

// 当前显示的表格数据
const currentTableData = ref(JSON.parse(JSON.stringify(originalTableData.value)))

// 搜索表单数据
const searchFormData = reactive<SearchFormData>({
  customer: null,
  number: '',
  startTime: '',
  endTime: '',
  user: null,
  people: null,
  orderType: '',
  showDetails: '隐藏明细'
})

// 搜索配置 - 调整为销售利润表的需求
const searchConfig = reactive<GoodSearchConfig>({
  showGoods: false,
  showNumber: true,
  showSupplier: false,
  showCustomer: true,
  showPeople: true,
  showBillDate: true,
  showArrivalDate: false,
  showUser: true,
  showExamine: false,
  showState: false,
  showRemark: false,
  inline: false,
  customFields: [
    {
      key: 'orderType',
      type: 'select',
      label: '单据类型',
      options: [
        { label: '全部', value: '' },
        { label: '销售单', value: '销售单' },
        { label: '销售退货单', value: '销售退货单' }
      ]
    },
    {
      key: 'showDetails',
      type: 'select',
      label: '显示明细',
      options: [
        { label: '隐藏明细', value: '隐藏明细' },
        { label: '显示明细', value: '显示明细' }
      ]
    }
  ]
})

// 处理搜索表单更新
const handleSearchFormUpdate = (newValue: SearchFormData) => {
  Object.assign(searchFormData, newValue)
  // 更新显示明细状态
  showDetails.value = newValue.showDetails === '显示明细'
}

// 处理搜索组件的搜索事件
const handleGoodSearch = (formData: SearchFormData) => {
  console.log('搜索条件:', formData)
  applySearchFilters(formData)
  ElMessage.success('搜索完成')
}

// 应用搜索过滤
const applySearchFilters = (formData: SearchFormData) => {
  let filteredData = JSON.parse(JSON.stringify(originalTableData.value))

  // 根据搜索条件过滤数据
  filteredData = filteredData.filter((item: any) => {
    // 过滤客户
    if (formData.customer && !item.customerName.includes(formData.customer as string)) {
      return false
    }

    // 过滤单据编号
    if (formData.number && !item.orderNo.includes(formData.number)) {
      return false
    }

    // 过滤单据类型
    if (formData.orderType && item.orderType !== formData.orderType) {
      return false
    }

    // 过滤单据日期范围
    if (formData.startTime || formData.endTime) {
      const orderDate = new Date(item.orderDate)
      const startDate = formData.startTime ? new Date(formData.startTime) : null
      const endDate = formData.endTime ? new Date(formData.endTime) : null

      if (startDate && orderDate < startDate) return false
      if (endDate && orderDate > endDate) return false
    }

    // 过滤制单人
    if (formData.user && !item.createdBy.includes(formData.user as string)) {
      return false
    }

    // 过滤关联人员
    if (formData.people && !item.relatedPerson.includes(formData.people as string)) {
      return false
    }

    return true
  })

  // 更新当前显示的数据
  currentTableData.value = filteredData
  currentPage.value = 1 // 重置到第一页
}

// 刷新功能
const handleRefresh = () => {
  // 重置搜索表单
  if (goodSearchRef.value) {
    goodSearchRef.value.resetForm()
  }

  // 重置搜索条件
  Object.assign(searchFormData, {
    customer: null,
    number: '',
    startTime: '',
    endTime: '',
    user: null,
    people: null,
    orderType: '',
    showDetails: '隐藏明细'
  })

  // 重置显示明细状态
  showDetails.value = false

  // 恢复显示所有原始数据
  currentTableData.value = JSON.parse(JSON.stringify(originalTableData.value))
  currentPage.value = 1 // 重置到第一页

  ElMessage.success('已刷新数据，显示全部记录')
}

// 导出功能实现
const handleExport = () => {
  const exportData = prepareExportData()
  ElMessage.success('正在导出数据...')
  setTimeout(() => {
    downloadCSV(exportData)
    ElMessage.success('导出成功，已开始下载文件')
  }, 1000)
}

// 准备导出数据
const prepareExportData = () => {
  return currentTableData.value.map((item: any) => {
    const exportItem: any = {
      单据类型: item.orderType,
      所属组织: item.orgName,
      客户: item.customerName,
      单据时间: item.orderDate,
      单据编号: item.orderNo
    }

    // 如果显示明细，添加明细列
    if (showDetails.value) {
      exportItem.商品名称 = item.productName
      exportItem.辅助属性 = item.auxiliaryAttr
      exportItem.单位 = item.unit
      exportItem.单价 = formatCurrency(item.unitPrice)
      exportItem.数量 = item.quantity
      exportItem.折扣额 = formatCurrency(item.discountAmount)
      exportItem.金额 = formatCurrency(item.amount)
      exportItem.税额 = formatCurrency(item.taxAmount)
      exportItem.价税合计 = formatCurrency(item.totalWithTax)
    }

    // 添加非明细列
    exportItem.单据金额 = formatCurrency(item.orderAmount)
    exportItem.优惠金额 = formatCurrency(item.discountTotal)
    exportItem.实际金额 = formatCurrency(item.actualAmount)
    exportItem.成本 = formatCurrency(item.cost)
    exportItem.毛利润 = formatCurrency(item.grossProfit)
    exportItem.毛利率 = formatPercentage(item.grossProfitRate)
    exportItem.单据费用 = formatCurrency(item.orderFee)
    exportItem.净利润 = formatCurrency(item.netProfit)
    exportItem.净利率 = formatPercentage(item.netProfitRate)
    exportItem.核销金额 = formatCurrency(item.verificationAmount)
    exportItem.核销状态 = item.verificationStatus
    exportItem.制单人 = item.createdBy
    exportItem.关联人员 = item.relatedPerson
    exportItem.备注信息 = item.remark

    return exportItem
  })
}

// 下载CSV文件
const downloadCSV = (data: any[]) => {
  if (data.length === 0) {
    ElMessage.warning('没有数据可导出')
    return
  }

  const headers = Object.keys(data[0]).join(',')
  const rows = data.map((row) =>
    Object.values(row)
      .map((value) => `"${value}"`)
      .join(',')
  )
  const csvContent = [headers, ...rows].join('\n')
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  const url = URL.createObjectURL(blob)
  link.setAttribute('href', url)
  link.setAttribute('download', `销售利润表_${new Date().toISOString().split('T')[0]}.csv`)
  link.style.visibility = 'hidden'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)

// 计算总数据条数
const total = computed(() => {
  return currentTableData.value.length
})

// 当前页数据
const currentPageData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return currentTableData.value.slice(start, end)
})

// 分页处理
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
}

// 格式化货币
const formatCurrency = (value: number) => {
  return `¥${value.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')}`
}

// 格式化百分比
const formatPercentage = (value: number) => {
  return `${(value * 100).toFixed(2)}%`
}

// 获取利润样式类
const getProfitClass = (value: number) => {
  if (value > 0) return 'profit-positive'
  if (value < 0) return 'profit-negative'
  return ''
}

// 初始化
onMounted(() => {
  // 初始化数据
  currentTableData.value = JSON.parse(JSON.stringify(originalTableData.value))
})
</script>

<style scoped>
/* 样式保持不变 */
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

.pagination-container {
  padding: 12px 16px;
  border-top: 1px solid #ebeef5;
  background: #fafafa;
  display: flex;
}

.pagination-left {
  justify-content: flex-start;
}

:deep(.el-table) {
  flex: 1;
}

/* 利润样式 */
/* .profit-positive {
  color: #f56c6c;
  font-weight: 600;
}

.profit-negative {
  color: #67c23a;
  font-weight: 600;
} */
</style>
