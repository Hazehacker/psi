<template>
  <div class="current-account area">
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
      :tree-props="{ children: 'node' }"
      row-key="key"
      height="calc(100% - 90px)"
      border
      v-loading="loading"
    >
      <!-- 空列 -->
      <el-table-column align="center" width="44px"></el-table-column>

      <!-- 账户名称 -->
      <el-table-column 
        prop="name" 
        label="账户名称" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 单据类型 -->
      <el-table-column 
        prop="extension.type" 
        label="单据类型" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 所属组织 -->
      <el-table-column 
        prop="sourceData.frameData.name" 
        label="所属组织" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 往来单位 -->
      <el-table-column 
        prop="current.name" 
        label="往来单位" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 单据时间 -->
      <el-table-column 
        prop="sourceData.time" 
        label="单据时间" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 单据编号 -->
      <el-table-column 
        prop="sourceData.number" 
        label="单据编号" 
        align="center" 
        width="180px"
      ></el-table-column>

      <!-- 收入 -->
      <el-table-column 
        prop="in" 
        label="收入" 
        align="center" 
        width="100px"
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
        width="100px"
      >
        <template #default="{ row }">
          {{ formatNumber(row.out) }}
        </template>
      </el-table-column>

      <!-- 账户余额 -->
      <el-table-column 
        prop="balance" 
        label="账户余额" 
        align="center" 
        width="100px"
      >
        <template #default="{ row }">
          {{ formatNumber(row.balance) }}
        </template>
      </el-table-column>

      <!-- 制单人 -->
      <el-table-column 
        prop="sourceData.userData.name" 
        label="制单人" 
        align="center" 
        width="100px"
      ></el-table-column>

      <!-- 备注 -->
      <el-table-column 
        prop="sourceData.data" 
        label="备注" 
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

interface UserData {
  name: string
}

interface SourceData {
  frameData: FrameData
  time: string
  number: string
  userData: UserData
  data: string
}

interface Current {
  name: string
}

interface Extension {
  type: string
}

interface TableRow {
  key: string
  name: string
  extension: Extension
  sourceData: SourceData
  current: Current
  in: number
  out: number
  balance: number
  node?: TableRow[]  // 树形结构的子节点
}

/**
 * 搜索配置
 */
const searchConfig: GoodSearchConfig = {
  showGoods: false,
  showNumber: false,
  showSupplier: true,     // 显示供应商
  showCustomer: true,     // 显示客户
  showWarehouse: false,
  showAccount: true,      // 显示资金账户
  showBillDate: true,     // 显示单据日期范围
  showArrivalDate: false,
  showUser: true,         // 显示制单人
  showPeople: false,
  showExamine: false,
  showState: false,
  showRemark: false
}

/**
 * 响应式数据
 */
const searchForm = ref<SearchFormData>({
  customer: null,
  supplier: null,
  startTime: '',
  endTime: '',
  user: null,
  account: null
})

const tableData = ref<TableRow[]>([])
const loading = ref(false)

const pagination = reactive({
  pageIndex: 1,
  pageSize: 30,
  total: 0
})

/**
 * 导出相关数据
 */
const exportFileName = ref('现金银行报表')
const exportDataList = computed(() => {
  const flattenData = (rows: TableRow[], level = 0): any[] => {
    const result: any[] = []
    rows.forEach(row => {
      result.push({
        '账户名称': level > 0 ? `${'  '.repeat(level)}${row.name}` : row.name,
        '单据类型': row.extension?.type || '',
        '所属组织': row.sourceData?.frameData?.name || '',
        '往来单位': row.current?.name || '',
        '单据时间': row.sourceData?.time || '',
        '单据编号': row.sourceData?.number || '',
        '收入': row.in || 0,
        '支出': row.out || 0,
        '账户余额': row.balance || 0,
        '制单人': row.sourceData?.userData?.name || '',
        '备注': row.sourceData?.data || ''
      })
      if (row.node && row.node.length > 0) {
        result.push(...flattenData(row.node, level + 1))
      }
    })
    return result
  }
  return flattenData(tableData.value)
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
  const accounts = ['工商银行', '建设银行', '现金账户', '支付宝账户']
  const billTypes = ['采购付款', '销售收款', '其他收入', '其他支出', '预收款', '预付款']
  const mockData: TableRow[] = []
  
  // 为每个账户生成数据
  accounts.forEach((accountName, accountIndex) => {
    const accountKey = `account_${accountIndex}`
    const accountNode: TableRow[] = []
    
    // 生成该账户的明细数据
    for (let i = 1; i <= 5; i++) {
      const billType = billTypes[Math.floor(Math.random() * billTypes.length)]
      accountNode.push({
        key: `${accountKey}_${i}`,
        name: accountName,
        extension: { type: billType },
        sourceData: {
          frameData: { name: '总部' },
          time: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
          number: `BL${20240000 + accountIndex * 10 + i}`,
          userData: { name: `制单人${i}` },
          data: `备注信息${i}`
        },
        current: { name: `往来单位${i}` },
        in: billType.includes('收') ? Math.random() * 10000 : 0,
        out: billType.includes('付') || billType.includes('支') ? Math.random() * 8000 : 0,
        balance: 50000 + Math.random() * 50000
      })
    }
    
    // 添加账户主节点
    mockData.push({
      key: accountKey,
      name: accountName,
      extension: { type: '账户汇总' },
      sourceData: {
        frameData: { name: '' },
        time: '',
        number: '',
        userData: { name: '' },
        data: ''
      },
      current: { name: '' },
      in: accountNode.reduce((sum, item) => sum + item.in, 0),
      out: accountNode.reduce((sum, item) => sum + item.out, 0),
      balance: 50000 + Math.random() * 100000,
      node: accountNode
    })
  })

  tableData.value = mockData
  pagination.total = mockData.length
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
      customer: searchForm.value.customer,
      supplier: searchForm.value.supplier,
      startTime: searchForm.value.startTime,
      endTime: searchForm.value.endTime,
      user: searchForm.value.user,
      account: searchForm.value.account
    }

    // TODO: 替换为实际的 API 地址
    const response = await axios.post('/api/crt/cbf', params)

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
    ElMessage.success('正在导出现金银行报表...')
    
    // 使用 xlsx 库导出 Excel
    import('xlsx').then(({ utils, writeFileXLSX }) => {
      // 创建工作表
      const ws = utils.json_to_sheet(exportDataList.value)
      
      // 创建工作簿
      const wb = utils.book_new()
      utils.book_append_sheet(wb, ws, '现金银行报表')
      
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
.current-account {
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

.tablePagination {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
}
</style>

