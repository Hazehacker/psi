<template>
  <div class="profit-statement area">
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
      height="calc(100% - 60px)"
      border
      v-loading="loading"
    >
      <!-- 序号 -->
      <el-table-column 
        type="index" 
        align="center" 
        width="50px"
      ></el-table-column>

      <!-- 项目 -->
      <el-table-column 
        prop="name" 
        label="项目" 
        width="200px"
      ></el-table-column>

      <!-- 金额 -->
      <el-table-column 
        prop="money" 
        label="金额" 
        align="center" 
        width="120px"
      >
        <template #default="{ row }">
          {{ formatNumber(row.money) }}
        </template>
      </el-table-column>
    </el-table>
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
  name: string
  money: number
}

/**
 * 搜索配置 - 只显示日期范围
 */
const searchConfig: GoodSearchConfig = {
  showGoods: false,
  showNumber: false,
  showSupplier: false,
  showCustomer: false,
  showWarehouse: false,
  showAccount: false,
  showBillDate: true,      // 只显示单据日期范围
  showArrivalDate: false,
  showUser: false,
  showPeople: false,
  showExamine: false,
  showState: false,
  showRemark: false
}

/**
 * 响应式数据
 */
const searchForm = ref<SearchFormData>({
  startTime: '',
  endTime: ''
})

const tableData = ref<TableRow[]>([])
const loading = ref(false)

/**
 * 导出相关数据
 */
const exportFileName = ref('利润表')
const exportDataList = computed(() => {
  return tableData.value.map(row => ({
    '项目': row.name || '',
    '金额': row.money || 0
  }))
})

/**
 * 初始化
 */
onMounted(() => {
  loadData()
})

/**
 * 使用模拟数据（临时方案，等待后端接口开发）
 */
function useMockData() {
  const mockData: TableRow[] = [
    { name: '一、营业收入', money: 1500000 },
    { name: '减：营业成本', money: 800000 },
    { name: '    税金及附加', money: 15000 },
    { name: '    销售费用', money: 120000 },
    { name: '    管理费用', money: 180000 },
    { name: '    财务费用', money: 25000 },
    { name: '二、营业利润', money: 360000 },
    { name: '加：营业外收入', money: 50000 },
    { name: '减：营业外支出', money: 10000 },
    { name: '三、利润总额', money: 400000 },
    { name: '减：所得税费用', money: 100000 },
    { name: '四、净利润', money: 300000 }
  ]

  tableData.value = mockData
  console.log('✅ 使用模拟数据（后端接口未开发）')
}

/**
 * 加载数据
 */
async function loadData() {
  loading.value = true

  try {
    const params = {
      startTime: searchForm.value.startTime,
      endTime: searchForm.value.endTime
    }

    // TODO: 替换为实际的 API 地址
    const response = await axios.post('/api/crt/cit', params)

    if (response.data.state === 'success') {
      tableData.value = response.data.info
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
  loadData()
}

/**
 * 导出数据
 */
function handleExport() {
  try {
    ElMessage.success('正在导出利润表...')
    
    // 使用 xlsx 库导出 Excel
    import('xlsx').then(({ utils, writeFileXLSX }) => {
      // 创建工作表
      const ws = utils.json_to_sheet(exportDataList.value)
      
      // 创建工作簿
      const wb = utils.book_new()
      utils.book_append_sheet(wb, ws, '利润表')
      
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
  loadData()
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
.profit-statement {
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

  // 树形表格样式 - 二级行背景色
  :deep(.el-table__row--level-1) {
    background: #fcfcfc;
  }
}

.area {
  padding: 20px;
  box-sizing: border-box;
}
</style>

