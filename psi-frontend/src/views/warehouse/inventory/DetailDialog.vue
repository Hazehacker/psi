<!--
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-29 07:56:03
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-10 20:05:29
 * @FilePath: \psi-frontend\src\views\warehouse\inventory\DetailDialog.vue
 * @Description: 仓库管理-库存详情弹窗
-->
<template>
  <div class="stockDetail">
    <el-dialog
      v-model="dialog"
      title="库存详情"
      width="900px"
      @closed="handleClosed"
      :append-to-body="true"
      v-madeDialog
    >
      <!-- 使用 NormalTable 组件 -->
      <NormalTable
        height="380px"
        :tabdata="tableData"
        :tabdatacolumns="columns"
        :emptyText="'暂无库存记录'"
        @page-change="handlePageChange"
        @update:tabdata="handleTableDataUpdate"
      />
      <!-- 底部操作区域插槽 -->
      <template #footer>
        <div class="footer-button">
          <GoodSearchForm
            ref="searchFormRef"
            v-model="searchFormData"
            :config="searchConfig"
            @search="handleSearch"
          />
          <el-button @click="handleExport">导出</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch, nextTick } from 'vue'
import NormalTable from '@/components/normaltable/NormalTable.vue'
import GoodSearchForm from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import type { PageDTO, MyTableColumn } from '@/components/normaltable/type'
import type { GoodSearchConfig, SearchFormData } from '@/components/goodSearchConpoent/type'
import { getInventoryDetailList } from '@/apis/warehouse/inventory/index'
import type { InventoryDetailDTO, InventoryDetailRequest } from '@/apis/warehouse/inventory/type'
import { formatDateTime } from '@/utils/time'

// 定义导出数据类型
interface ExportData {
  searchParams: Record<string, unknown>
  tableData: InventoryDetailDTO[]
  goodsData?: any
  formData: SearchFormData
}

// 定义props
interface Props {
  visible: boolean
  goodsData?: any // 商品数据，用于获取库存详情
}

const props = withDefaults(defineProps<Props>(), {
  visible: false,
  goodsData: () => ({})
})

// 定义emits
const emit = defineEmits<{
  (event: 'update:visible', value: boolean): void
  (event: 'closed'): void
  (event: 'export', data: ExportData): void
}>()

// 弹窗显示状态
const dialog = computed({
  get: () => props.visible,
  set: (value: boolean) => emit('update:visible', value)
})

// 搜索表单数据
const searchFormData = ref<SearchFormData>({})

// 搜索配置 - 针对库存详情调整
const searchConfig = reactive<GoodSearchConfig>({
  // 隐藏不需要的字段
  showGoods: false,
  showNumber: false,
  showSupplier: false,
  showPeople: false,
  showBillDate: false,
  showArrivalDate: false,
  showUser: false,
  showExamine: false,
  showState: false,
  showRemark: false,

  // 自定义字段 - 针对库存详情
  customFields: [
    {
      key: 'documentNumber',
      label: '请输入单据编号',
      type: 'input'
    },
    {
      key: 'documentTypes',
      label: '请选择单据类型',
      type: 'select',
      multiple: true,
      options: [
        { label: '采购单', value: '采购单' },
        { label: '采购退货单', value: '采购退货单' },
        { label: '销售单', value: '销售单' },
        { label: '销售退货单', value: '销售退货单' },
        { label: '调拨单', value: '调拨单' },
        { label: '其他入库单', value: '其他入库单' },
        { label: '其他出库单', value: '其他出库单' },
        { label: '盘盈单', value: '盘盈单' },
        { label: '盘亏单', value: '盘亏单' }
      ]
    },
    {
      key: 'startDate',
      label: '请输入开始日期',
      type: 'date'
    },
    {
      key: 'endDate',
      label: '请输入结束日期',
      type: 'date'
    }
  ],

  // 设置默认日期范围为30天
  defaultDays: 30
})

// 表格列定义 - 根据新的接口结构调整
const columns: MyTableColumn[] = [
  {
    prop: 'frameName',
    label: '所属组织',
    align: 'center',
    minWidth: '120'
  },
  {
    prop: 'time',
    label: '操作时间',
    align: 'center',
    minWidth: '150',
    formatter: (row: InventoryDetailDTO) => {
      if (!row.time) return '-'
      const time = typeof row.time === 'string' ? new Date(row.time) : row.time
      return formatDateTime(time) || '-'
    }
  },
  {
    prop: 'type',
    label: '单据类型',
    align: 'center',
    minWidth: '120'
  },
  {
    prop: 'number',
    label: '单据编号',
    align: 'center',
    minWidth: '200'
  },
  {
    prop: 'direction',
    label: '操作类型',
    align: 'center',
    minWidth: '100',
    formatter: (row: InventoryDetailDTO) => {
      return row.direction === 1 ? '入库' : '出库'
    }
  },
  {
    prop: 'nums',
    label: '操作数量',
    align: 'center',
    minWidth: '100',
    formatter: (row: InventoryDetailDTO) => {
      const num = row.nums || 0
      const direction = row.direction
      const prefix = direction === 1 ? '+' : '-'
      return `${prefix}${num}`
    }
  }
]

// 表格数据
const tableData = reactive<PageDTO<InventoryDetailDTO>>({
  rows: [],
  total: 0,
  pageIndex: 1,
  pageSize: 20
})

// 搜索表单引用
const searchFormRef = ref()

// 搜索参数
const searchParams = ref<Record<string, any>>({})

// 监听弹窗打开
watch(dialog, (newVal) => {
  if (newVal) {
    // 弹窗打开时重置数据并加载
    resetTableData()
    nextTick(() => {
      loadTableData()
    })
  }
})

// 重置表格数据
function resetTableData() {
  tableData.pageIndex = 1
  tableData.pageSize = 20
  tableData.total = 0
  tableData.rows = []
  searchParams.value = {}

  // 重置搜索表单
  if (searchFormRef.value) {
    searchFormRef.value.resetForm()
  }
}

// 处理搜索
function handleSearch(formData: SearchFormData) {
  // 转换表单数据为搜索参数
  searchParams.value = {
    documentNumber: formData.documentNumber,
    documentTypes: formData.documentTypes,
    startDate: formData.startTime ? new Date(formData.startTime) : undefined,
    endDate: formData.endTime ? new Date(formData.endTime) : undefined
  }

  tableData.pageIndex = 1 // 搜索时重置到第一页
  loadTableData()
}

// 处理表格数据更新
function handleTableDataUpdate(newData: PageDTO<InventoryDetailDTO>) {
  Object.assign(tableData, newData)
  loadTableData()
}

// 处理页面变化
function handlePageChange(pageData: PageDTO<InventoryDetailDTO>) {
  Object.assign(tableData, pageData)
  loadTableData()
}

// 加载表格数据
async function loadTableData() {
  try {
    // 构建请求参数
    const requestParams: InventoryDetailRequest = {
      goodsId: props.goodsData?.id || '', // 从商品数据获取商品ID
      attrName: props.goodsData?.attrName, // 如果有辅助属性
      pageIndex: tableData.pageIndex,
      pageSize: tableData.pageSize,
      ...searchParams.value
    }

    console.log('加载库存详情数据:', requestParams)

    // 使用新的API调用方式
    await new Promise((resolve, reject) => {
      getInventoryDetailList(
        requestParams,
        (data) => {
          console.log('库存详情API响应数据:', data)
          tableData.rows = data.rows || []
          tableData.total = data.total || 0
          resolve(data)
        },
        (error) => {
          console.error('获取库存详情失败:', error)
          tableData.rows = []
          tableData.total = 0
          reject(error)
        }
      )
    })
  } catch (error) {
    console.error('加载库存详情失败:', error)
    tableData.rows = []
    tableData.total = 0
  }
}

// 处理导出
function handleExport() {
  const exportData: ExportData = {
    searchParams: searchParams.value,
    tableData: tableData.rows || [],
    goodsData: props.goodsData,
    formData: searchFormData.value
  }

  console.log('导出数据:', exportData)
  emit('export', exportData)

  // 实际导出逻辑
  // 1. 可以调用后端导出接口
  // 2. 或者前端生成Excel文件
}

// 处理弹窗关闭
function handleClosed() {
  emit('closed')
  resetTableData()
}

// 暴露方法给父组件
defineExpose({
  refresh: () => {
    loadTableData()
  },
  getTableData: () => tableData,
  getSearchParams: () => searchParams.value,
  getFormData: () => searchFormData.value
})
</script>

<style lang="scss" scoped>
/* 操作按钮浮动 */
.dialog-footer > span {
  float: left;
}
.footer-button {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
