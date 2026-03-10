<template>
  <div class="other-receipt-detail area">
    <div class="layout">
      <!-- 使用 GoodSearchForm 组件 -->
      <GoodSearchForm 
        v-model="searchForm"
        :config="searchConfig"
        :default-days="0"
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
      <!-- 单据类型 -->
      <el-table-column 
        prop="name" 
        label="单据类型" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 所属组织 -->
      <el-table-column 
        prop="class.frameData.name" 
        label="所属组织" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 往来单位 -->
      <el-table-column 
        prop="current.name" 
        label="往来单位" 
        align="center" 
        width="160px"
      ></el-table-column>

      <!-- 单据时间 -->
      <el-table-column 
        prop="class.time" 
        label="单据时间" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 单据编号 -->
      <el-table-column 
        prop="class.number" 
        label="单据编号" 
        align="center" 
        width="180px"
      ></el-table-column>

      <!-- 收支类别 -->
      <el-table-column 
        prop="info.ietData.name" 
        label="收支类别" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 收入 -->
      <el-table-column 
        prop="in" 
        label="收入" 
        align="center" 
        width="90px"
      >
        <template #default="{ row }">
          {{ formatNumber(row.in) }}
        </template>
      </el-table-column>

      <!-- 支出 -->
      <el-table-column 
        prop="out" 
        label="支出" 
        align="center" 
        width="90px"
      >
        <template #default="{ row }">
          {{ formatNumber(row.out) }}
        </template>
      </el-table-column>

      <!-- 结算账户 -->
      <el-table-column 
        prop="class.accountData.name" 
        label="结算账户" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 备注信息 -->
      <el-table-column 
        prop="class.data" 
        label="备注信息" 
        align="center" 
        width="200px"
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
      <span class="status-item income">总收入：{{ formatNumber(totalIncome) }}</span>
      <span class="status-item expense">总支出：{{ formatNumber(totalExpense) }}</span>
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
interface FrameData {
  name: string
}

interface AccountData {
  name: string
}

interface ClassData {
  frameData: FrameData
  time: string
  number: string
  accountData: AccountData
  data: string
}

interface IetData {
  name: string
}

interface InfoData {
  ietData: IetData
}

interface Current {
  name: string
}

interface TableRow {
  name: string
  class: ClassData
  current: Current
  info: InfoData
  in: number
  out: number
}

/**
 * 搜索配置
 */
const searchConfig: GoodSearchConfig = {
  showGoods: false,
  showNumber: true,       // 显示单据编号
  showSupplier: false,
  showCustomer: false,
  showWarehouse: false,
  showAccount: true,      // 显示资金账户
  showBillDate: true,     // 显示单据日期范围
  showArrivalDate: false,
  showUser: false,
  showPeople: false,
  showExamine: false,
  showState: false,
  showRemark: true,       // 显示备注信息
  customFields: [
    {
      key: 'mold',
      type: 'select',
      label: '单据类型',
      options: [
        { label: '其它收入单', value: 'ice' },
        { label: '其它支出单', value: 'oce' }
      ]
    },
    {
      key: 'iet',
      type: 'input',
      label: '请选择收支类别'
    }
  ]
}

/**
 * 响应式数据
 */
const searchForm = ref<SearchFormData>({
  mold: [],
  number: '',
  startTime: '',
  endTime: '',
  iet: null,
  account: null,
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
 * 计算总收入
 */
const totalIncome = computed(() => {
  return tableData.value.reduce((sum, row) => sum + (row.in || 0), 0)
})

/**
 * 计算总支出
 */
const totalExpense = computed(() => {
  return tableData.value.reduce((sum, row) => sum + (row.out || 0), 0)
})

/**
 * 导出相关数据
 */
const exportFileName = ref('其他收支明细表')
const exportDataList = computed(() => {
  return tableData.value.map(row => ({
    '单据类型': row.name || '',
    '所属组织': row.class?.frameData?.name || '',
    '往来单位': row.current?.name || '',
    '单据时间': row.class?.time || '',
    '单据编号': row.class?.number || '',
    '收支类别': row.info?.ietData?.name || '',
    '收入': row.in || 0,
    '支出': row.out || 0,
    '结算账户': row.class?.accountData?.name || '',
    '备注信息': row.class?.data || ''
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
  const billTypes = ['其它收入单', '其它支出单']
  const categories = ['利息收入', '政府补贴', '捐赠支出', '罚款支出', '其他收入', '其他支出']
  const accounts = ['工商银行', '建设银行', '现金账户']
  const mockData: TableRow[] = []
  
  for (let i = 1; i <= 20; i++) {
    const isIncome = i % 2 === 0
    mockData.push({
      name: billTypes[isIncome ? 0 : 1],
      class: {
        frameData: { name: '总部' },
        time: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
        number: `OS${20240000 + i}`,
        accountData: { name: accounts[i % 3] },
        data: `备注信息${i}`
      },
      current: { name: `往来单位${i}` },
      info: {
        ietData: { name: categories[Math.floor(Math.random() * categories.length)] }
      },
      in: isIncome ? Math.random() * 50000 : 0,
      out: !isIncome ? Math.random() * 30000 : 0
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
      mold: searchForm.value.mold,
      number: searchForm.value.number,
      startTime: searchForm.value.startTime,
      endTime: searchForm.value.endTime,
      iet: searchForm.value.iet,
      account: searchForm.value.account,
      data: searchForm.value.data
    }

    // TODO: 替换为实际的 API 地址
    const response = await axios.post('/api/crt/cos', params)

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
    ElMessage.success('正在导出其他收支明细表...')
    
    // 使用 xlsx 库导出 Excel
    import('xlsx').then(({ utils, writeFileXLSX }) => {
      // 创建工作表
      const ws = utils.json_to_sheet(exportDataList.value)
      
      // 创建工作簿
      const wb = utils.book_new()
      utils.book_append_sheet(wb, ws, '其他收支明细表')
      
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
  if (value === undefined || value === null || value === 0) return '-'
  return value.toFixed(2)
}
</script>

<style scoped lang="scss">
.other-receipt-detail {
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
      
      &.income {
        color: #67c23a;
      }
      
      &.expense {
        color: #f56c6c;
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

