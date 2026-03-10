<template>
  <div class="receivable-detail area">
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

      <!-- 客户 -->
      <el-table-column 
        prop="name" 
        label="客户" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 单据类型 -->
      <el-table-column 
        prop="bill" 
        label="单据类型" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 所属组织 -->
      <el-table-column 
        prop="frameData.name" 
        label="所属组织" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 单据时间 -->
      <el-table-column 
        prop="time" 
        label="单据时间" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 单据编号 -->
      <el-table-column 
        prop="number" 
        label="单据编号" 
        align="center" 
        width="180px"
      ></el-table-column>

      <!-- 增加应收款 -->
      <el-table-column 
        prop="cw" 
        label="增加应收款" 
        align="center" 
        width="100px"
      >
        <template #default="{ row }">
          {{ formatNumber(row.cw) }}
        </template>
      </el-table-column>

      <!-- 增加预收款 -->
      <el-table-column 
        prop="pia" 
        label="增加预收款" 
        align="center" 
        width="100px"
      >
        <template #default="{ row }">
          {{ formatNumber(row.pia) }}
        </template>
      </el-table-column>

      <!-- 应收款余额 -->
      <el-table-column 
        prop="balance" 
        label="应收款余额" 
        align="center" 
        width="100px"
      >
        <template #default="{ row }">
          {{ formatNumber(row.balance) }}
        </template>
      </el-table-column>

      <!-- 备注 -->
      <el-table-column 
        prop="data" 
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

interface TableRow {
  key: string
  name: string
  bill: string
  frameData: FrameData
  time: string
  number: string
  cw: number
  pia: number
  balance: number
  data: string
  node?: TableRow[]  // 树形结构的子节点
}

/**
 * 搜索配置
 */
const searchConfig: GoodSearchConfig = {
  showGoods: false,
  showNumber: false,
  showSupplier: false,
  showCustomer: true,     // 显示客户
  showWarehouse: false,
  showAccount: false,
  showBillDate: true,     // 显示单据日期范围
  showArrivalDate: false,
  showUser: false,
  showPeople: false,
  showExamine: false,
  showState: false,
  showRemark: false,
  customFields: [
    {
      key: 'type',
      type: 'select',
      label: '数据类型',
      options: [
        { label: '全部客户', value: 0 },
        { label: '欠款客户', value: 1 }
      ]
    }
  ]
}

/**
 * 响应式数据
 */
const searchForm = ref<SearchFormData>({
  customer: null,
  type: 0,
  startTime: '',
  endTime: ''
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
const exportFileName = ref('应收款明细表')
const exportDataList = computed(() => {
  const flattenData = (rows: TableRow[], level = 0): any[] => {
    const result: any[] = []
    rows.forEach(row => {
      result.push({
        '客户': level > 0 ? `${'  '.repeat(level)}${row.name}` : row.name,
        '单据类型': row.bill || '',
        '所属组织': row.frameData?.name || '',
        '单据时间': row.time || '',
        '单据编号': row.number || '',
        '增加应收款': row.cw || 0,
        '增加预收款': row.pia || 0,
        '应收款余额': row.balance || 0,
        '备注': row.data || ''
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
  const customers = ['客户A', '客户B', '客户C', '客户D']
  const billTypes = ['销售单', '销售退货单', '收款单', '预收款单']
  const mockData: TableRow[] = []
  
  // 为每个客户生成数据
  customers.forEach((customerName, customerIndex) => {
    const customerKey = `customer_${customerIndex}`
    const customerNode: TableRow[] = []
    
    // 生成该客户的明细数据
    for (let i = 1; i <= 4; i++) {
      const billType = billTypes[i % billTypes.length]
      customerNode.push({
        key: `${customerKey}_${i}`,
        name: customerName,
        bill: billType,
        frameData: { name: '总部' },
        time: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
        number: `BL${20240000 + customerIndex * 10 + i}`,
        cw: billType === '销售单' ? Math.random() * 80000 : 0,
        pia: billType === '预收款单' ? Math.random() * 30000 : 0,
        balance: 50000 + Math.random() * 150000,
        data: `备注信息${i}`
      })
    }
    
    // 添加客户主节点
    mockData.push({
      key: customerKey,
      name: customerName,
      bill: '客户汇总',
      frameData: { name: '' },
      time: '',
      number: '',
      cw: customerNode.reduce((sum, item) => sum + item.cw, 0),
      pia: customerNode.reduce((sum, item) => sum + item.pia, 0),
      balance: 80000 + Math.random() * 200000,
      data: '',
      node: customerNode
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
      type: searchForm.value.type,
      startTime: searchForm.value.startTime,
      endTime: searchForm.value.endTime
    }

    // TODO: 替换为实际的 API 地址
    const response = await axios.post('/api/crt/crs', params)

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
    ElMessage.success('正在导出应收款明细表...')
    
    // 使用 xlsx 库导出 Excel
    import('xlsx').then(({ utils, writeFileXLSX }) => {
      // 创建工作表
      const ws = utils.json_to_sheet(exportDataList.value)
      
      // 创建工作簿
      const wb = utils.book_new()
      utils.book_append_sheet(wb, ws, '应收款明细表')
      
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
.receivable-detail {
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

