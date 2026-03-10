<template>
  <div class="report-demo">
    <h2>报表按钮表格组件演示</h2>
    <ReportTable
      title="销售单据"
      :columns="salesColumns"
      :data="salesData"
      :total="salesTotal"
      :loading="loading"
      :summary-data="summaryData"
      :row-logs="getRowLogs"
      :current-page="currentPage"
      :page-size="pageSize"
      @search="handleSalesSearch"
      @view="handleViewDetail"
      @delete="handleDeleteSales"
      @edit="handleEditSales"
      @page-change="handleSalesPageChange"
      @selection-change="handleSelectionChange"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useOperationLogsStore } from '@/stores/operationLogs'
import ReportTable from '@/components/report/reportButtonTable.vue'

// 表格列配置
const salesColumns = [
  { prop: 'org', label: '所属组织', width: '120' },
  { prop: 'supplier', label: '供应商', width: '200' },
  { prop: 'billDate', label: '单据时间', width: '120', sortable: true },
  { prop: 'billCode', label: '单据编号', width: '180', sortable: true },
  { prop: 'billAmount', label: '单据金额', width: '120', align: 'right' as const, sortable: true },
  { prop: 'actualAmount', label: '实际金额', width: '120', align: 'right' as const, sortable: true },
  { prop: 'arrivalDate', label: '到货日期', width: '120', sortable: true }
]

// 响应式数据
const salesData = ref([
  {
    org: '默认组织',
    supplier: '小皓',
    billDate: '2025-10-18',
    billCode: 'CGDD2510182115205',
    billAmount: '207.5',
    actualAmount: '200',
    arrivalDate: '2025-10-19'
  },
  {
    org: '默认组织',
    supplier: '郑州东方之花医药股份有限公司',
    billDate: '2025-10-18',
    billCode: 'ZCGDD2510181650136',
    billAmount: '142',
    actualAmount: '140',
    arrivalDate: '2025-10-17'
  },
  {
    org: '默认组织',
    supplier: 'aly供应商1',
    billDate: '2025-10-16',
    billCode: 'CGDD2510161430221',
    billAmount: '13.5',
    actualAmount: '1',
    arrivalDate: '2025-10-17'
  },
  {
    org: '默认组织',
    supplier: '小皓',
    billDate: '2025-10-16',
    billCode: 'CGDD2510161428553',
    billAmount: '2880.5',
    actualAmount: '2880',
    arrivalDate: '2025-10-17'
  },
  {
    org: '默认组织',
    supplier: '郑州东方之花医药股份有限公司',
    billDate: '2025-10-15',
    billCode: 'CGDD2510150912345',
    billAmount: '500',
    actualAmount: '500',
    arrivalDate: '2025-10-16'
  },
  {
    org: '默认组织',
    supplier: 'aly供应商1',
    billDate: '2025-10-15',
    billCode: 'CGDD2510150912346',
    billAmount: '1200',
    actualAmount: '1200',
    arrivalDate: '2025-10-16'
  },
  {
    org: '默认组织',
    supplier: '小皓',
    billDate: '2025-10-14',
    billCode: 'CGDD2510140912347',
    billAmount: '800',
    actualAmount: '800',
    arrivalDate: '2025-10-15'
  },
  {
    org: '默认组织',
    supplier: '郑州东方之花医药股份有限公司',
    billDate: '2025-10-14',
    billCode: 'CGDD2510140912348',
    billAmount: '1500',
    actualAmount: '1500',
    arrivalDate: '2025-10-15'
  },
  {
    org: '默认组织',
    supplier: 'aly供应商1',
    billDate: '2025-10-13',
    billCode: 'CGDD2510130912349',
    billAmount: '900',
    actualAmount: '900',
    arrivalDate: '2025-10-14'
  },
  {
    org: '默认组织',
    supplier: '小皓',
    billDate: '2025-10-13',
    billCode: 'CGDD2510130912350',
    billAmount: '600',
    actualAmount: '600',
    arrivalDate: '2025-10-14'
  },
  {
    org: '默认组织',
    supplier: '郑州东方之花医药股份有限公司',
    billDate: '2025-10-12',
    billCode: 'CGDD2510120912351',
    billAmount: '1100',
    actualAmount: '1100',
    arrivalDate: '2025-10-13'
  },
  {
    org: '默认组织',
    supplier: 'aly供应商1',
    billDate: '2025-10-12',
    billCode: 'CGDD2510120912352',
    billAmount: '750',
    actualAmount: '750',
    arrivalDate: '2025-10-13'
  },
  {
    org: '默认组织',
    supplier: '小皓',
    billDate: '2025-10-11',
    billCode: 'CGDD2510110912353',
    billAmount: '1200',
    actualAmount: '1200',
    arrivalDate: '2025-10-12'
  },
  {
    org: '默认组织',
    supplier: '郑州东方之花医药股份有限公司',
    billDate: '2025-10-11',
    billCode: 'CGDD2510110912354',
    billAmount: '800',
    actualAmount: '800',
    arrivalDate: '2025-10-12'
  },
  {
    org: '默认组织',
    supplier: 'aly供应商1',
    billDate: '2025-10-10',
    billCode: 'CGDD2510100912355',
    billAmount: '1500',
    actualAmount: '1500',
    arrivalDate: '2025-10-11'
  },
  {
    org: '默认组织',
    supplier: '小皓',
    billDate: '2025-10-10',
    billCode: 'CGDD2510100912356',
    billAmount: '900',
    actualAmount: '900',
    arrivalDate: '2025-10-11'
  },
  {
    org: '默认组织',
    supplier: '郑州东方之花医药股份有限公司',
    billDate: '2025-10-09',
    billCode: 'CGDD2510090912357',
    billAmount: '1100',
    actualAmount: '1100',
    arrivalDate: '2025-10-10'
  },
  {
    org: '默认组织',
    supplier: 'aly供应商1',
    billDate: '2025-10-09',
    billCode: 'CGDD2510090912358',
    billAmount: '600',
    actualAmount: '600',
    arrivalDate: '2025-10-10'
  },
  {
    org: '默认组织',
    supplier: '小皓',
    billDate: '2025-09-28',
    billCode: 'CGDD2509280912380',
    billAmount: '1150',
    actualAmount: '1150',
    arrivalDate: '2025-09-29'
  }
])

const salesTotal = computed(() => salesData.value.length)
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(30)
const router = useRouter()

// 使用操作日志管理器
const operationLogsStore = useOperationLogsStore()

// 汇总数据 - 基于实际数据计算
const summaryData = computed(() => {
  const totalBillAmount = salesData.value.reduce((sum, item) => sum + parseFloat(String(item.billAmount || 0)), 0)
  const totalActualAmount = salesData.value.reduce((sum, item) => sum + parseFloat(String(item.actualAmount || 0)), 0)
  
  return [
    { label: '总单据金额', value: totalBillAmount.toFixed(2) },
    { label: '总实际金额', value: totalActualAmount.toFixed(1) },
  ]
})

// 方法
const handleSalesSearch = (keyword: string, dateRange: any) => {
  console.log('搜索销售单据:', keyword, dateRange)
  // 调用 API 搜索
}

const handleViewDetail = (row: any) => {
  // 记录查看操作日志
  operationLogsStore.addLog(row.billCode, '查看详情')
  
  console.log('查看销售单据详情:', row)
  ElMessage.info(`查看单据 ${row.billCode} 的详情`)
  
  // 跳转到详情页面
  router.push({ name: 'sampleReportDetail', query: { code: row.billCode } })
}

const handleDeleteSales = (rows: any[]) => {
  console.log('删除销售单据被调用:', rows)
  console.log('当前数据总数:', salesData.value.length)
  
  // 从数据中删除选中的行
  if (rows && rows.length > 0) {
    console.log('开始删除操作...')
    
    // 记录删除操作日志
    rows.forEach(row => {
      operationLogsStore.addLog(row.billCode, '删除单据')
    })
    
    // 获取要删除的行的索引
    const indicesToDelete = rows.map(row => {
      const index = salesData.value.findIndex(item => item.billCode === row.billCode)
      console.log(`查找行 ${row.billCode} 的索引:`, index)
      return index
    }).filter(index => index !== -1)
    
    console.log('要删除的索引:', indicesToDelete)
    
    // 从后往前删除，避免索引变化问题
    indicesToDelete.sort((a, b) => b - a).forEach(index => {
      console.log(`删除索引 ${index} 的数据:`, salesData.value[index])
      salesData.value.splice(index, 1)
    })
    
    console.log(`删除后数据总数:`, salesData.value.length)
    console.log(`成功删除 ${rows.length} 条数据`)
    ElMessage.success(`成功删除 ${rows.length} 条数据`)
  } else {
    console.log('没有选中任何数据')
    ElMessage.warning('请选择要删除的数据')
  }
}

const handleEditSales = (row: any) => {
  console.log('编辑销售单据:', row)
  
  // 记录编辑操作日志
  operationLogsStore.addLog(row.billCode, '编辑单据')
  
  // 模拟编辑操作 - 随机修改一些数据
  const randomAmount = (Math.random() * 1000 + 100).toFixed(2)
  const randomActualAmount = (Math.random() * 1000 + 100).toFixed(1)
  
  const index = salesData.value.findIndex(item => item.billCode === row.billCode)
  if (index !== -1) {
    salesData.value[index].billAmount = randomAmount
    salesData.value[index].actualAmount = randomActualAmount
    ElMessage.success('编辑成功')
  }
}

const handleSalesPageChange = (page: number, size: number) => {
  console.log('分页改变:', page, size)
  currentPage.value = page
  pageSize.value = size
  // 在实际应用中，这里应该调用 API 获取新页数据
}

const handleSelectionChange = (selection: any[]) => {
  console.log('选择改变:', selection)
}


// 获取行操作日志
const getRowLogs = (row: any) => {
  return operationLogsStore.getLogs(row.billCode)
}

// 初始化数据时添加默认日志
const initializeLogs = () => {
  salesData.value.forEach(item => {
    operationLogsStore.initializeBillLogs(item.billCode)
  })
}

// 在组件挂载时初始化日志
onMounted(() => {
  initializeLogs()
})
</script>

<style scoped>
.report-demo {
  padding: 20px;
}

.report-demo h2 {
  margin-bottom: 20px;
  color: #303133;
  font-size: 20px;
  font-weight: 600;
}
</style>