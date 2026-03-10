<template>
  <div class="debt-age-analysis area">
    <div class="layout">
      <!-- 使用 GoodSearchForm 组件 -->
      <GoodSearchForm 
        v-model="searchForm"
        :config="searchConfig"
        @search="handleSearch"
      />
      
      <!-- 操作按钮组 -->
      <div class="operation-buttons">
        <el-button class="white-button" @click="handleExport">导出</el-button>
        <el-button class="white-button" @click="handleReload">刷新</el-button>
      </div>
    </div>

    <el-divider></el-divider>

    <el-table
      :data="tableData"
      height="calc(100% - 90px)"
      border
      v-loading="loading"
    >
      <!-- 单位类型 -->
      <el-table-column 
        prop="mold" 
        label="单位类型" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 单位名称 -->
      <el-table-column 
        prop="name" 
        label="单位名称" 
        align="center" 
        width="160px"
      ></el-table-column>

      <!-- 单位编号 -->
      <el-table-column 
        prop="number" 
        label="单位编号" 
        align="center" 
        width="160px"
      ></el-table-column>

      <!-- 应收款余额 -->
      <el-table-column 
        prop="collection" 
        label="应收款余额" 
        align="center" 
        width="120px"
      >
        <template #default="{ row }">
          {{ formatNumber(row.collection) }}
        </template>
      </el-table-column>

      <!-- 应付款余额 -->
      <el-table-column 
        prop="payment" 
        label="应付款余额" 
        align="center" 
        width="120px"
      >
        <template #default="{ row }">
          {{ formatNumber(row.payment) }}
        </template>
      </el-table-column>

      <!-- 备注信息 -->
      <el-table-column 
        prop="data" 
        label="备注信息" 
        align="center" 
        width="160px"
      ></el-table-column>
    </el-table>

    <el-pagination
      class="tablePagination"
      v-model:current-page="pagination.pageIndex"
      v-model:page-size="pagination.pageSize"
      :total="pagination.total"
      :page-sizes="[30, 60, 90, 150, 300]"
      :pager-count="5"
      layout="prev, pager, next, jumper, sizes, total"
      @size-change="handlePageSizeChange"
      @current-change="handlePageChange"
    >
    </el-pagination>

    <!-- 底部统计信息 -->
    <div class="page-status">
      <span class="status-item">总应收款余额：{{ formatNumber(totalCollection) }}</span>
      <span class="status-item">总应付款余额：{{ formatNumber(totalPayment) }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import GoodSearchForm from '@/components/goodSearchConpoent'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent'
import axios from 'axios'

/**
 * 类型定义
 */
interface TableRow {
  mold: string
  name: string
  number: string
  collection: number
  payment: number
  data: string
}

/**
 * 搜索配置
 */
const searchConfig: GoodSearchConfig = {
  showGoods: false,
  showNumber: false,
  showSupplier: false,
  showCustomer: false,
  showWarehouse: false,
  showAccount: false,
  showBillDate: false,
  showArrivalDate: false,
  showUser: false,
  showPeople: false,
  showExamine: false,
  showState: false,
  showRemark: false,
  customFields: [
    {
      key: 'name',
      type: 'input',
      label: '请输入单位名称'
    },
    {
      key: 'number',
      type: 'input',
      label: '请输入单位编号'
    },
    {
      key: 'mold',
      type: 'select',
      label: '单位类型',
      options: [
        { label: '客户', value: 'customer' },
        { label: '供应商', value: 'supplier' }
      ]
    },
    {
      key: 'data',
      type: 'input',
      label: '请输入备注信息'
    }
  ]
}

/**
 * 响应式数据
 */
const searchForm = ref<SearchFormData>({
  name: '',
  number: '',
  mold: [],
  data: ''
})

const tableData = ref<TableRow[]>([])
const loading = ref(false)

const pagination = reactive({
  pageIndex: 1,
  pageSize: 30,
  total: 0
})

/**
 * 计算总应收款余额
 */
const totalCollection = computed(() => {
  return tableData.value.reduce((sum, row) => sum + (row.collection || 0), 0)
})

/**
 * 计算总应付款余额
 */
const totalPayment = computed(() => {
  return tableData.value.reduce((sum, row) => sum + (row.payment || 0), 0)
})

/**
 * 导出相关数据
 */
const exportFileName = ref('往来单位欠款表')
const exportDataList = computed(() => {
  return tableData.value.map(row => ({
    '单位类型': row.mold === 'customer' ? '客户' : row.mold === 'supplier' ? '供应商' : row.mold,
    '单位名称': row.name || '',
    '单位编号': row.number || '',
    '应收款余额': row.collection || 0,
    '应付款余额': row.payment || 0,
    '备注信息': row.data || ''
  }))
})

/**
 * 初始化
 */
onMounted(() => {
  loadData(1)
})

/**
 * 使用模拟数据（临时方案，等待后端接口开发）
 */
function useMockData() {
  const unitTypes = ['customer', 'supplier']
  const unitTypeNames = { customer: '客户', supplier: '供应商' }
  const mockData: TableRow[] = []
  
  for (let i = 1; i <= 20; i++) {
    const mold = unitTypes[i % 2]
    mockData.push({
      mold: unitTypeNames[mold as keyof typeof unitTypeNames],
      name: `${unitTypeNames[mold as keyof typeof unitTypeNames]}${i}`,
      number: `UNIT${1000 + i}`,
      collection: Math.random() * 100000,
      payment: Math.random() * 80000,
      data: `备注信息${i}`
    })
  }

  tableData.value = mockData
  pagination.total = 100
  console.log('✅ 使用模拟数据（后端接口未开发）')
}

/**
 * 加载数据
 */
async function loadData(page?: number) {
  if (page !== undefined) {
    pagination.pageIndex = page
  }

  loading.value = true

  try {
    const params = {
      page: pagination.pageIndex,
      limit: pagination.pageSize,
      name: searchForm.value.name,
      number: searchForm.value.number,
      mold: searchForm.value.mold,
      data: searchForm.value.data
    }

    // TODO: 替换为实际的 API 地址
    const response = await axios.post('/api/crt/cds', params)

    if (response.data.state === 'success') {
      tableData.value = response.data.info
      pagination.total = response.data.count
    } else if (response.data.state === 'error') {
      ElMessage.warning(response.data.info)
    }
  } catch (error: any) {
    console.error('加载数据失败:', error)
    // 使用模拟数据（接口未开发时临时使用）
    if (error.response?.status === 404) {
      useMockData()
    }
  } finally {
    loading.value = false
  }
}

/**
 * 处理搜索
 */
function handleSearch(formData: SearchFormData) {
  searchForm.value = formData
  loadData(1)
}

/**
 * 处理页码变化
 */
function handlePageChange(page: number) {
  loadData(page)
}

/**
 * 处理每页条数变化
 */
function handlePageSizeChange() {
  loadData(1)
}

/**
 * 导出数据
 */
function handleExport() {
  try {
    ElMessage.success('正在导出往来单位欠款表...')
    
    // 使用 xlsx 库导出 Excel
    import('xlsx').then(({ utils, writeFileXLSX }) => {
      // 创建工作表
      const ws = utils.json_to_sheet(exportDataList.value)
      
      // 创建工作簿
      const wb = utils.book_new()
      utils.book_append_sheet(wb, ws, '往来单位欠款表')
      
      // 导出文件
      const fileName = `${exportFileName.value}_${getCurrentDate()}.xlsx`
      writeFileXLSX(wb, fileName)
      
      ElMessage.success('导出成功！')
    })
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败，请稍后重试')
  }
}

/**
 * 刷新页面
 */
function handleReload() {
  loadData(1)
  ElMessage.success('页面刷新成功')
}

/**
 * 获取当前日期
 */
function getCurrentDate(): string {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

/**
 * 格式化数字
 */
function formatNumber(value: number | undefined | null): string {
  if (value === undefined || value === null) return '-'
  return value.toFixed(2)
}
</script>

<style scoped lang="scss">
.debt-age-analysis {
  height: 100%;
  display: flex;
  flex-direction: column;

  .layout {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .operation-buttons {
    display: flex;
    align-items: center;
    gap: 8px;

    .white-button {
      background-color: #ffffff;
      border-color: #dcdfe6;
      color: #606266;

      &:hover {
        background-color: #f5f7fa;
        border-color: #c0c4cc;
        color: #409eff;
      }

      &:active {
        background-color: #ecf5ff;
        border-color: #409eff;
        color: #409eff;
      }
    }
  }

  .page-status {
    margin-top: 10px;
    padding: 10px;
    background-color: #f5f7fa;
    border-radius: 4px;
    display: flex;
    gap: 30px;
    justify-content: center;
    font-size: 14px;
    color: #606266;

    .status-item {
      font-weight: 500;
      
      &:first-child {
        color: #67c23a;
      }
      
      &:last-child {
        color: #e6a23c;
      }
    }
  }
}

.area {
  padding: 20px;
  box-sizing: border-box;
}

.tablePagination {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
}
</style>

