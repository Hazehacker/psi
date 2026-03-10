<template>
  <div class="supplier-statement area">
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
      ref="tableRef"
      :data="tableData"
      :tree-props="{ children: 'node' }"
      row-key="key"
      height="calc(100% - 60px)"
      border
      v-loading="loading"
    >
      <!-- 明细模式的空列 -->
      <el-table-column v-if="showDetail" align="center" width="44px"></el-table-column>

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

      <!-- 明细模式才显示的列 -->
      <template v-if="showDetail">
        <!-- 名称 -->
        <el-table-column 
          prop="detail.name" 
          label="名称" 
          align="center" 
          width="120px"
        ></el-table-column>

        <!-- 属性 -->
        <el-table-column 
          prop="detail.attr" 
          label="属性" 
          align="center" 
          width="120px"
        ></el-table-column>

        <!-- 单位 -->
        <el-table-column 
          prop="detail.unit" 
          label="单位" 
          align="center" 
          width="100px"
        ></el-table-column>

        <!-- 单价 -->
        <el-table-column 
          prop="detail.price" 
          label="单价" 
          align="center" 
          width="100px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.detail?.price) }}
          </template>
        </el-table-column>

        <!-- 数量 -->
        <el-table-column 
          prop="detail.nums" 
          label="数量" 
          align="center" 
          width="100px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.detail?.nums) }}
          </template>
        </el-table-column>

        <!-- 折扣额 -->
        <el-table-column 
          prop="detail.dsc" 
          label="折扣额" 
          align="center" 
          width="100px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.detail?.dsc) }}
          </template>
        </el-table-column>

        <!-- 金额 -->
        <el-table-column 
          prop="detail.total" 
          label="金额" 
          align="center" 
          width="100px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.detail?.total) }}
          </template>
        </el-table-column>

        <!-- 税额 -->
        <el-table-column 
          prop="detail.tat" 
          label="税额" 
          align="center" 
          width="100px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.detail?.tat) }}
          </template>
        </el-table-column>

        <!-- 价税合计 -->
        <el-table-column 
          prop="detail.tpt" 
          label="价税合计" 
          align="center" 
          width="100px"
        >
          <template #default="{ row }">
            {{ formatNumber(row.detail?.tpt) }}
          </template>
        </el-table-column>
      </template>

      <!-- 单据金额 -->
      <el-table-column 
        prop="total" 
        label="单据金额" 
        align="center" 
        width="100px"
      >
        <template #default="{ row }">
          {{ formatNumber(row.total) }}
        </template>
      </el-table-column>

      <!-- 优惠金额 -->
      <el-table-column 
        prop="discount" 
        label="优惠金额" 
        align="center" 
        width="100px"
      >
        <template #default="{ row }">
          {{ formatNumber(row.discount) }}
        </template>
      </el-table-column>

      <!-- 应付金额 -->
      <el-table-column 
        prop="actual" 
        label="应付金额" 
        align="center" 
        width="100px"
      >
        <template #default="{ row }">
          {{ formatNumber(row.actual) }}
        </template>
      </el-table-column>

      <!-- 实付金额 -->
      <el-table-column 
        prop="money" 
        label="实付金额" 
        align="center" 
        width="100px"
      >
        <template #default="{ row }">
          {{ formatNumber(row.money) }}
        </template>
      </el-table-column>

      <!-- 应付款余额 -->
      <el-table-column 
        prop="balance" 
        label="应付款余额" 
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import type { ElTable } from 'element-plus'
import GoodSearchForm from '@/components/goodSearchConpoent'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent'
import axios from 'axios'

/**
 * 类型定义
 */
interface FrameData {
  name: string
}

interface DetailData {
  name: string
  attr: string
  unit: string
  price: number
  nums: number
  dsc: number
  total: number
  tat: number
  tpt: number
}

interface TableRow {
  key: string
  bill: string
  frameData: FrameData
  time: string
  number: string
  detail?: DetailData
  total: number
  discount: number
  actual: number
  money: number
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
  showSupplier: true,     // 显示供应商（必选）
  showCustomer: false,
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
      label: '显示明细',
      options: [
        { label: '隐藏明细', value: 0 },
        { label: '显示明细', value: 1 }
      ]
    }
  ]
}

/**
 * 响应式数据
 */
const searchForm = ref<SearchFormData>({
  supplier: null,
  type: 0,
  startTime: '',
  endTime: ''
})

const tableData = ref<TableRow[]>([])
const loading = ref(false)
const tableRef = ref<InstanceType<typeof ElTable>>()

/**
 * 是否显示明细
 */
const showDetail = computed(() => searchForm.value.type === 1)

/**
 * 导出相关数据
 */
const exportFileName = ref('供应商对账单')

/**
 * 初始化
 */
onMounted(() => {
  // 供应商对账单需要先选择供应商才能查询
  // 所以初始化时不自动加载数据
})

/**
 * 使用模拟数据（临时方案，等待后端接口开发）
 */
function useMockData() {
  const billTypes = ['采购单', '采购退货单', '付款单']
  const mockData: TableRow[] = []
  
  for (let i = 1; i <= 10; i++) {
    const billType = billTypes[i % billTypes.length]
    const hasDetail = showDetail.value
    const row: TableRow = {
      key: `bill_${i}`,
      bill: billType,
      frameData: { name: '总部' },
      time: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
      number: `BL${20240000 + i}`,
      total: Math.random() * 100000,
      discount: Math.random() * 5000,
      actual: Math.random() * 95000,
      money: Math.random() * 90000,
      balance: Math.random() * 50000,
      data: `备注信息${i}`
    }

    // 如果显示明细，添加商品明细子节点
    if (hasDetail) {
      const details: TableRow[] = []
      for (let j = 1; j <= 3; j++) {
        details.push({
          key: `bill_${i}_detail_${j}`,
          bill: '',
          frameData: { name: '' },
          time: '',
          number: '',
          detail: {
            name: `商品${j}`,
            attr: '默认属性',
            unit: '件',
            price: Math.random() * 1000,
            nums: Math.floor(Math.random() * 100) + 1,
            dsc: Math.random() * 500,
            total: Math.random() * 30000,
            tat: Math.random() * 3000,
            tpt: Math.random() * 33000
          },
          total: 0,
          discount: 0,
          actual: 0,
          money: 0,
          balance: 0,
          data: ''
        })
      }
      row.node = details
    }

    mockData.push(row)
  }

  tableData.value = mockData
  console.log('✅ 使用模拟数据（后端接口未开发）')
}

/**
 * 加载数据
 */
async function loadData() {
  // 检查是否选择了供应商
  if (!searchForm.value.supplier) {
    ElMessage.warning('请选择供应商后搜索!')
    return
  }

  loading.value = true

  try {
    const params = {
      supplier: searchForm.value.supplier,
      type: searchForm.value.type,
      startTime: searchForm.value.startTime,
      endTime: searchForm.value.endTime
    }

    // TODO: 替换为实际的 API 地址
    const response = await axios.post('/api/crt/cst', params)

    if (response.data.state === 'success') {
      tableData.value = response.data.info
      
      // 更新表格布局（解决动态列可能导致的布局问题）
      await nextTick()
      tableRef.value?.doLayout()
    } else if (response.data.state === 'error') {
      ElMessage.warning(response.data.info)
    }
  } catch (error: any) {
    console.error('加载数据失败:', error)
    // 使用模拟数据（接口未开发时临时使用）
    if (error.response?.status === 404) {
      useMockData()
      // 更新表格布局
      await nextTick()
      tableRef.value?.doLayout()
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
    ElMessage.success('正在导出供应商对账单...')
    
    // 使用 xlsx 库导出 Excel
    import('xlsx').then(({ utils, writeFileXLSX }) => {
      const exportData = flattenTableData(tableData.value)
      
      // 创建工作表
      const ws = utils.json_to_sheet(exportData)
      
      // 创建工作簿
      const wb = utils.book_new()
      utils.book_append_sheet(wb, ws, '供应商对账单')
      
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
 * 扁平化表格数据用于导出
 */
function flattenTableData(rows: TableRow[]): any[] {
  const result: any[] = []
  
  rows.forEach(row => {
    if (showDetail.value && row.node && row.node.length > 0) {
      // 有明细的情况
      row.node.forEach(detail => {
        result.push({
          '单据类型': row.bill,
          '所属组织': row.frameData?.name || '',
          '单据时间': row.time,
          '单据编号': row.number,
          '商品名称': detail.detail?.name || '',
          '属性': detail.detail?.attr || '',
          '单位': detail.detail?.unit || '',
          '单价': detail.detail?.price || 0,
          '数量': detail.detail?.nums || 0,
          '折扣额': detail.detail?.dsc || 0,
          '金额': detail.detail?.total || 0,
          '税额': detail.detail?.tat || 0,
          '价税合计': detail.detail?.tpt || 0,
          '单据金额': row.total || 0,
          '优惠金额': row.discount || 0,
          '应付金额': row.actual || 0,
          '实付金额': row.money || 0,
          '应付款余额': row.balance || 0,
          '备注': row.data || ''
        })
      })
    } else {
      // 无明细的情况
      result.push({
        '单据类型': row.bill,
        '所属组织': row.frameData?.name || '',
        '单据时间': row.time,
        '单据编号': row.number,
        '单据金额': row.total || 0,
        '优惠金额': row.discount || 0,
        '应付金额': row.actual || 0,
        '实付金额': row.money || 0,
        '应付款余额': row.balance || 0,
        '备注': row.data || ''
      })
    }
  })
  
  return result
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
  if (value === undefined || value === null || value === 0) return '-'
  return value.toFixed(2)
}
</script>

<style scoped lang="scss">
.supplier-statement {
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

