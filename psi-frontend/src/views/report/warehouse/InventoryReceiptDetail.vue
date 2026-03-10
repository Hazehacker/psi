<template>
  <div class="inventory-receipt-detail area">
    <div class="layout">
      <!-- 使用 GoodSearchForm 组件 -->
      <GoodSearchForm 
        v-model="searchForm"
        :config="searchConfig"
        :default-days="30"
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
        prop="extension.type" 
        label="单据类型" 
        align="center" 
        width="90px"
      ></el-table-column>

      <!-- 所属组织 -->
      <el-table-column 
        prop="sourceData.frameData.name" 
        label="所属组织" 
        align="center" 
        width="90px"
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

      <!-- 商品名称 -->
      <el-table-column 
        prop="goodsData.name" 
        label="商品名称" 
        align="center" 
        width="160px"
      ></el-table-column>

      <!-- 辅助属性 -->
      <el-table-column 
        prop="attr" 
        label="辅助属性" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 仓库 -->
      <el-table-column 
        prop="warehouseData.name" 
        label="仓库" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 单位 -->
      <el-table-column 
        prop="unit" 
        label="单位" 
        align="center" 
        width="90px"
      ></el-table-column>

      <!-- 入库信息 -->
      <el-table-column label="入库" align="center">
        <el-table-column 
          prop="in.uct" 
          label="成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.in?.uct) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="in.uns" 
          label="数量" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.in?.uns) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="in.bct" 
          label="总成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.in?.bct) }}
          </template>
        </el-table-column>
      </el-table-column>

      <!-- 出库信息 -->
      <el-table-column label="出库" align="center">
        <el-table-column 
          prop="out.uct" 
          label="成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.out?.uct) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="out.uns" 
          label="数量" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.out?.uns) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="out.bct" 
          label="总成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.out?.bct) }}
          </template>
        </el-table-column>
      </el-table-column>

      <!-- 汇总信息 -->
      <el-table-column label="汇总" align="center">
        <el-table-column 
          prop="balance.uct" 
          label="成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.balance?.uct) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="balance.uns" 
          label="数量" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.balance?.uns) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="balance.bct" 
          label="总成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.balance?.bct) }}
          </template>
        </el-table-column>
      </el-table-column>
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
interface GoodsData {
  name: string
}

interface WarehouseData {
  name: string
}

interface SourceData {
  frameData: {
    name: string
  }
  time: string
  number: string
}

interface Current {
  name: string
}

interface Extension {
  type: string
}

interface InOutBalance {
  uct: number  // 单位成本
  uns: number  // 数量
  bct: number  // 总成本
}

interface TableRow {
  extension: Extension
  sourceData: SourceData
  current: Current
  goodsData: GoodsData
  attr: string
  warehouseData: WarehouseData
  unit: string
  in: InOutBalance
  out: InOutBalance
  balance: InOutBalance
}

/**
 * 搜索配置
 */
const searchConfig: GoodSearchConfig = {
  showGoods: true,
  showNumber: false,
  showSupplier: false,
  showCustomer: false,
  showWarehouse: true,    // 显示仓库字段
  showBillDate: true,     // 显示单据日期
  showArrivalDate: false,
  showUser: false,
  showPeople: false,
  showExamine: false,
  showState: false,
  showRemark: false,
  customFields: [
    {
      key: 'mold',
      type: 'select',
      label: '单据类型',
      options: [
        { label: '采购单', value: 'buy' },
        { label: '采购退货单', value: 'bre' },
        { label: '销售单', value: 'sell' },
        { label: '销售退货单', value: 'sre' },
        { label: '调拨单-出', value: 'swapOut' },
        { label: '调拨单-入', value: 'swapEnter' },
        { label: '其它入库单', value: 'entry' },
        { label: '其它出库单', value: 'extry' }
      ]
    }
  ]
}

/**
 * 响应式数据
 */
const searchForm = ref<SearchFormData>({
  goods: '',
  warehouse: null,
  startTime: '',
  endTime: '',
  mold: []
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
const exportFileName = ref('商品收发明细表')
const exportDataList = computed(() => {
  return tableData.value.map(row => ({
    '单据类型': row.extension?.type || '',
    '所属组织': row.sourceData?.frameData?.name || '',
    '往来单位': row.current?.name || '',
    '单据时间': row.sourceData?.time || '',
    '单据编号': row.sourceData?.number || '',
    '商品名称': row.goodsData?.name || '',
    '辅助属性': row.attr || '',
    '仓库': row.warehouseData?.name || '',
    '单位': row.unit || '',
    '入库-成本': row.in?.uct || 0,
    '入库-数量': row.in?.uns || 0,
    '入库-总成本': row.in?.bct || 0,
    '出库-成本': row.out?.uct || 0,
    '出库-数量': row.out?.uns || 0,
    '出库-总成本': row.out?.bct || 0,
    '汇总-成本': row.balance?.uct || 0,
    '汇总-数量': row.balance?.uns || 0,
    '汇总-总成本': row.balance?.bct || 0
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
  const billTypes = ['采购单', '采购退货单', '销售单', '销售退货单', '调拨单-出', '调拨单-入', '其它入库单', '其它出库单']
  const mockData = []
  
  for (let i = 1; i <= 15; i++) {
    const billType = billTypes[Math.floor(Math.random() * billTypes.length)]
    mockData.push({
      extension: { type: billType },
      sourceData: {
        frameData: { name: '总部' },
        time: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
        number: `BL${20240000 + i}`
      },
      current: { name: `往来单位${i}` },
      goodsData: { name: `测试商品${i}` },
      attr: '默认属性',
      warehouseData: { name: ['北京仓库', '上海仓库', '广州仓库'][Math.floor(Math.random() * 3)] },
      unit: '件',
      in: {
        uct: Math.random() * 100,
        uns: Math.random() * 50,
        bct: Math.random() * 5000
      },
      out: {
        uct: Math.random() * 100,
        uns: Math.random() * 50,
        bct: Math.random() * 5000
      },
      balance: {
        uct: Math.random() * 100,
        uns: Math.random() * 50,
        bct: Math.random() * 5000
      }
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
      goods: searchForm.value.goods,
      warehouse: searchForm.value.warehouse,
      startTime: searchForm.value.startTime,
      endTime: searchForm.value.endTime,
      mold: searchForm.value.mold
    }

    // TODO: 替换为实际的 API 地址
    const response = await axios.post('/api/wrf/wds', params)

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
    ElMessage.success('正在导出商品收发明细表...')
    
    // 使用 xlsx 库导出 Excel
    import('xlsx').then(({ utils, writeFileXLSX }) => {
      // 创建工作表
      const ws = utils.json_to_sheet(exportDataList.value)
      
      // 创建工作簿
      const wb = utils.book_new()
      utils.book_append_sheet(wb, ws, '商品收发明细表')
      
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
  // 重置搜索表单（保留日期范围）
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
.inventory-receipt-detail {
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

