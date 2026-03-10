<template>
  <div class="inventory-receipt-summary area">
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
      <!-- 商品名称 -->
      <el-table-column 
        prop="goodsData.name" 
        label="商品名称" 
        align="center" 
        width="160px"
        fixed="left"
      ></el-table-column>

      <!-- 仓库 -->
      <el-table-column 
        prop="warehouseData.name" 
        label="仓库" 
        align="center" 
        width="100px"
        fixed="left"
      ></el-table-column>

      <!-- 商品编号 -->
      <el-table-column 
        prop="goodsData.number" 
        label="商品编号" 
        align="center" 
        width="120px"
      ></el-table-column>

      <!-- 规格型号 -->
      <el-table-column 
        prop="goodsData.spce" 
        label="规格型号" 
        align="center" 
        width="90px"
      ></el-table-column>

      <!-- 单位 -->
      <el-table-column 
        prop="unit" 
        label="单位" 
        align="center" 
        width="90px"
      ></el-table-column>

      <!-- 期初 -->
      <el-table-column label="期初" align="center">
        <el-table-column 
          prop="state.uct" 
          label="成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.state?.uct) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="state.uns" 
          label="数量" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.state?.uns) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="state.bct" 
          label="总成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.state?.bct) }}
          </template>
        </el-table-column>
      </el-table-column>

      <!-- 采购单 -->
      <el-table-column label="采购单" align="center">
        <el-table-column 
          prop="buy.uct" 
          label="成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.buy?.uct) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="buy.uns" 
          label="数量" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.buy?.uns) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="buy.bct" 
          label="总成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.buy?.bct) }}
          </template>
        </el-table-column>
      </el-table-column>

      <!-- 采购退货单 -->
      <el-table-column label="采购退货单" align="center">
        <el-table-column 
          prop="bre.uct" 
          label="成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.bre?.uct) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="bre.uns" 
          label="数量" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.bre?.uns) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="bre.bct" 
          label="总成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.bre?.bct) }}
          </template>
        </el-table-column>
      </el-table-column>

      <!-- 销售单 -->
      <el-table-column label="销售单" align="center">
        <el-table-column 
          prop="sell.uct" 
          label="成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.sell?.uct) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="sell.uns" 
          label="数量" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.sell?.uns) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="sell.bct" 
          label="总成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.sell?.bct) }}
          </template>
        </el-table-column>
      </el-table-column>

      <!-- 销售退货单 -->
      <el-table-column label="销售退货单" align="center">
        <el-table-column 
          prop="sre.uct" 
          label="成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.sre?.uct) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="sre.uns" 
          label="数量" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.sre?.uns) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="sre.bct" 
          label="总成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.sre?.bct) }}
          </template>
        </el-table-column>
      </el-table-column>

      <!-- 调拨单-出 -->
      <el-table-column label="调拨单-出" align="center">
        <el-table-column 
          prop="swapOut.uct" 
          label="成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.swapOut?.uct) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="swapOut.uns" 
          label="数量" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.swapOut?.uns) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="swapOut.bct" 
          label="总成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.swapOut?.bct) }}
          </template>
        </el-table-column>
      </el-table-column>

      <!-- 调拨单-入 -->
      <el-table-column label="调拨单-入" align="center">
        <el-table-column 
          prop="swapEnter.uct" 
          label="成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.swapEnter?.uct) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="swapEnter.uns" 
          label="数量" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.swapEnter?.uns) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="swapEnter.bct" 
          label="总成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.swapEnter?.bct) }}
          </template>
        </el-table-column>
      </el-table-column>

      <!-- 其它入库单 -->
      <el-table-column label="其它入库单" align="center">
        <el-table-column 
          prop="entry.uct" 
          label="成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.entry?.uct) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="entry.uns" 
          label="数量" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.entry?.uns) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="entry.bct" 
          label="总成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.entry?.bct) }}
          </template>
        </el-table-column>
      </el-table-column>

      <!-- 其它出库单 -->
      <el-table-column label="其它出库单" align="center">
        <el-table-column 
          prop="extry.uct" 
          label="成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.extry?.uct) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="extry.uns" 
          label="数量" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.extry?.uns) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="extry.bct" 
          label="总成本" 
          align="center" 
          width="90px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.extry?.bct) }}
          </template>
        </el-table-column>
      </el-table-column>

      <!-- 汇总 -->
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
  number: string
  spce: string
}

interface WarehouseData {
  name: string
}

interface BillData {
  uct: number  // 单位成本
  uns: number  // 数量
  bct: number  // 总成本
}

interface TableRow {
  goodsData: GoodsData
  warehouseData: WarehouseData
  unit: string
  state: BillData      // 期初
  buy: BillData        // 采购单
  bre: BillData        // 采购退货单
  sell: BillData       // 销售单
  sre: BillData        // 销售退货单
  swapOut: BillData    // 调拨单-出
  swapEnter: BillData  // 调拨单-入
  entry: BillData      // 其它入库单
  extry: BillData      // 其它出库单
  balance: BillData    // 汇总
}

/**
 * 搜索配置
 */
const searchConfig: GoodSearchConfig = {
  showGoods: true,
  showNumber: false,
  showSupplier: false,
  showCustomer: false,
  showWarehouse: true,    // 显示仓库字段（多选）
  showBillDate: true,     // 显示单据日期范围
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
  goods: '',
  warehouse: null,
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
const exportFileName = ref('商品收发汇总表')
const exportDataList = computed(() => {
  return tableData.value.map(row => ({
    '商品名称': row.goodsData?.name || '',
    '仓库': row.warehouseData?.name || '',
    '商品编号': row.goodsData?.number || '',
    '规格型号': row.goodsData?.spce || '',
    '单位': row.unit || '',
    '期初-成本': row.state?.uct || 0,
    '期初-数量': row.state?.uns || 0,
    '期初-总成本': row.state?.bct || 0,
    '采购单-成本': row.buy?.uct || 0,
    '采购单-数量': row.buy?.uns || 0,
    '采购单-总成本': row.buy?.bct || 0,
    '采购退货单-成本': row.bre?.uct || 0,
    '采购退货单-数量': row.bre?.uns || 0,
    '采购退货单-总成本': row.bre?.bct || 0,
    '销售单-成本': row.sell?.uct || 0,
    '销售单-数量': row.sell?.uns || 0,
    '销售单-总成本': row.sell?.bct || 0,
    '销售退货单-成本': row.sre?.uct || 0,
    '销售退货单-数量': row.sre?.uns || 0,
    '销售退货单-总成本': row.sre?.bct || 0,
    '调拨单-出-成本': row.swapOut?.uct || 0,
    '调拨单-出-数量': row.swapOut?.uns || 0,
    '调拨单-出-总成本': row.swapOut?.bct || 0,
    '调拨单-入-成本': row.swapEnter?.uct || 0,
    '调拨单-入-数量': row.swapEnter?.uns || 0,
    '调拨单-入-总成本': row.swapEnter?.bct || 0,
    '其它入库单-成本': row.entry?.uct || 0,
    '其它入库单-数量': row.entry?.uns || 0,
    '其它入库单-总成本': row.entry?.bct || 0,
    '其它出库单-成本': row.extry?.uct || 0,
    '其它出库单-数量': row.extry?.uns || 0,
    '其它出库单-总成本': row.extry?.bct || 0,
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
  const mockData = []
  
  for (let i = 1; i <= 12; i++) {
    mockData.push({
      goodsData: {
        name: `测试商品${i}`,
        number: `SN${1000 + i}`,
        spce: '标准规格'
      },
      warehouseData: { name: ['北京仓库', '上海仓库', '广州仓库'][i % 3] },
      unit: '件',
      state: {
        uct: Math.random() * 50,
        uns: Math.random() * 100,
        bct: Math.random() * 5000
      },
      buy: {
        uct: Math.random() * 50,
        uns: Math.random() * 100,
        bct: Math.random() * 5000
      },
      bre: {
        uct: Math.random() * 50,
        uns: Math.random() * 10,
        bct: Math.random() * 500
      },
      sell: {
        uct: Math.random() * 50,
        uns: Math.random() * 80,
        bct: Math.random() * 4000
      },
      sre: {
        uct: Math.random() * 50,
        uns: Math.random() * 10,
        bct: Math.random() * 500
      },
      swapOut: {
        uct: Math.random() * 50,
        uns: Math.random() * 20,
        bct: Math.random() * 1000
      },
      swapEnter: {
        uct: Math.random() * 50,
        uns: Math.random() * 20,
        bct: Math.random() * 1000
      },
      entry: {
        uct: Math.random() * 50,
        uns: Math.random() * 30,
        bct: Math.random() * 1500
      },
      extry: {
        uct: Math.random() * 50,
        uns: Math.random() * 30,
        bct: Math.random() * 1500
      },
      balance: {
        uct: Math.random() * 50,
        uns: Math.random() * 150,
        bct: Math.random() * 7500
      }
    })
  }

  tableData.value = mockData
  pagination.total = 80
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
      endTime: searchForm.value.endTime
    }

    // TODO: 替换为实际的 API 地址
    const response = await axios.post('/api/wrf/wss', params)

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
    ElMessage.success('正在导出商品收发汇总表...')
    
    // 使用 xlsx 库导出 Excel
    import('xlsx').then(({ utils, writeFileXLSX }) => {
      // 创建工作表
      const ws = utils.json_to_sheet(exportDataList.value)
      
      // 创建工作簿
      const wb = utils.book_new()
      utils.book_append_sheet(wb, ws, '商品收发汇总表')
      
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
.inventory-receipt-summary {
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

