<template>
  <div class="cct area">
    <!-- 使用 NormalTable 组件 -->
    <NormalTable
      ref="normalTableRef"
      :tabdata="treeData"
      :tabdatacolumns="tableColumns"
      :tabattr="tableAttr"
      @page-change="handlePageChange"
    >
      <!-- 自定义表格内容 -->
      <template #customercell="{ column, row }">
        <template v-if="column.prop === 'name' && row.level > 0">
          <span style="margin-left: 20px">↳ {{ row.name }}</span>
        </template>
        <template v-else-if="column.prop !== 'operate'">
          {{ row[column.prop] }}
        </template>
      </template>
    </NormalTable>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import NormalTable from '@/components/normaltable/NormalTable.vue'
import { createPageDTO, type MyTableColumn, type PageDTO } from '@/components/normaltable/type'

// 响应式数据
const searchFrom = reactive({
  customer: null,
  type: 0,
  startTime: '',
  endTime: ''
})

const normalTableRef = ref()
// 表格数据
const treeData = ref(
  createPageDTO({
    rows: [],
    total: 0,
    pageIndex: 1,
    pageSize: 20,
    pages: 0
  })
)
const tableType = ref(0)

// 计算属性：动态列配置
const tableColumns = computed<MyTableColumn[]>(() => {
  const baseColumns: MyTableColumn[] = [
    { prop: 'expand', label: '', align: 'center', width: '48' }, // 树形展开列
    { prop: 'bill', label: '单据类型', align: 'center', width: '120' },
    { prop: 'frameData', label: '所属组织', align: 'center', width: '120' },
    { prop: 'time', label: '单据时间', align: 'center', width: '120' },
    { prop: 'number', label: '单据编号', align: 'center', width: '180' },
    { prop: 'total', label: '单据金额', align: 'center', width: '100' },
    { prop: 'discount', label: '优惠金额', align: 'center', width: '100' },
    { prop: 'actual', label: '应收金额', align: 'center', width: '100' },
    { prop: 'money', label: '实收金额', align: 'center', width: '100' },
    { prop: 'balance', label: '应收款余额', align: 'center', width: '100' },
    { prop: 'data', label: '备注', align: 'center', width: '200' }
  ]
  return baseColumns
})

// 表格属性配置
const tableAttr = computed(() => ({
  'row-key': 'key',
  'tree-props': {
    children: 'children'
  },
  'default-expand-all': false,
  // 保留原有的树形表格样式
  'row-class-name': ({ row }: { row: any }) => {
    return row.level === 1 ? 'el-table__row--level-1' : ''
  }
}))

// 初始化日期
const init = () => {
  const today = new Date()
  const startDate = new Date()
  startDate.setDate(today.getDate() - 30) // 默认30天前

  searchFrom.startTime = formatDate(startDate)
  searchFrom.endTime = formatDate(today)
}

// 日期格式化函数
const formatDate = (date: Date) => {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

// 生成树形结构数据
const generateTreeData = () => {
  const data = []
  let keyCounter = 1

  // 生成20个主单据
  for (let i = 0; i < 20; i++) {
    const mainDoc = {
      key: keyCounter++,
      id: keyCounter,
      bill: `采购订单(主)`,
      frameData: `组织${i + 1}`,
      time: `2024-10-${String((i % 30) + 1).padStart(2, '0')}`,
      number: `CGDD2410241254${i.toString().padStart(3, '0')}`,
      data: `主单据备注${i + 1}`,
      level: 0,
      children: [] as any[]
    }

    // 添加子节点
    mainDoc.children = Array.from({ length: 3 }, (_, subIndex) => ({
      key: keyCounter++,
      id: keyCounter,
      bill: '',
      frameData: '',
      time: '',
      number: '',
      total: (Math.random() * 10000 + 1000).toFixed(2),
      discount: (Math.random() * 500).toFixed(2),
      actual: (Math.random() * 9500 + 1000).toFixed(2),
      money: (Math.random() * 9000 + 1000).toFixed(2),
      balance: (Math.random() * 1000).toFixed(2),
      data: '',
      name: `商品${subIndex + 1}`,
      price: (Math.random() * 100 + 10).toFixed(2),
      nums: Math.floor(Math.random() * 10 + 1),
      dsc: (Math.random() * 50).toFixed(2),
      level: 1
    }))

    data.push(mainDoc)
  }

  return data
}

// 获取数据
const fetchData = async (pageInfo: any = {}) => {
  try {
    // 模拟网络延迟
    await new Promise((resolve) => setTimeout(resolve, 500))

    const treeContent = generateTreeData()

    // 更新表格数据
    const pageData = createPageDTO({
      pageIndex: pageInfo.pageIndex || treeData.value.pageIndex,
      pageSize: pageInfo.pageSize || treeData.value.pageSize,
      total: treeContent.length,
      rows: treeContent
    })
    treeData.value = pageData as any

    // 更新表格类型
    tableType.value = searchFrom.type
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage({ type: 'error', message: '获取数据失败!' })
  }
}

// 分页变化处理
const handlePageChange = async (pageData: any) => {
  console.log('分页变化:', pageData)
  await fetchData(pageData)
}

// 组件挂载
onMounted(() => {
  init()
  // 组件挂载后自动加载数据
  fetchData()
})
</script>
