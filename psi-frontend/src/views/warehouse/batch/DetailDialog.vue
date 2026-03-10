<!--
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-29 07:56:03
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-10-30 09:24:28
 * @FilePath: \psi-frontend\src\views\warehouse\batch\DetailDialog.vue
 * @Description: 仓库管理-批次查询-详情弹窗
-->
<template>
  <div class="stockDetail">
    <el-dialog
      v-model="dialog"
      title="批次详情"
      width="900px"
      @closed="handleClosed"
      :append-to-body="true"
      v-madeDialog
    >
      <!-- 使用 NormalTable 组件 -->
      <NormalTable
        height="320px"
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
            placement="top-start"
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
import { getBatchDetailList } from '@/apis/warehouse/batch/index'
import type { BatchDetailRequest } from '@/apis/warehouse/batch/type'
import type { BatchDetailDTO } from '@/apis/warehouse/batch/type'
import { formatDateTime } from '@/utils/time'

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
  (event: 'export', data: any): void
}>()

// 弹窗显示状态
const dialog = computed({
  get: () => props.visible,
  set: (value: boolean) => emit('update:visible', value)
})

// 搜索表单数据
const searchFormData = ref<SearchFormData>({
  number: '',
  type: [],
  startTime: '',
  endTime: ''
})

// 搜索配置 - 根据要求的搜索框参数调整
const searchConfig = reactive<GoodSearchConfig>({
  // 隐藏所有默认字段，使用自定义字段
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

  // 自定义字段 - 根据要求的搜索框参数
  customFields: [
    {
      key: 'number',
      label: '请输入单据编号',
      type: 'input'
    },
    {
      key: 'type',
      label: '请选择单据类型',
      type: 'select',
      multiple: true, // 启用多选
      collapseTags: true, // 启用标签折叠
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
    },
    {
      key: 'startTime',
      label: '请输入开始日期',
      type: 'date'
    },
    {
      key: 'endTime',
      label: '请输入结束日期',
      type: 'date'
    }
  ],

  // 设置默认日期范围为30天
  defaultDays: 30,

  // 搜索按钮配置
  searchButton: {
    icon: 'el-icon-search',
    text: '',
    class: 'searchBtn'
  }
})

// 表格列定义 - 根据mock数据调整
const columns: MyTableColumn[] = [
  {
    prop: 'frame',
    label: '所属组织',
    align: 'center',
    width: '120px',
    formatter: (row: BatchDetailDTO) => {
      return row.frame || '-'
    }
  },
  {
    prop: 'time',
    label: '操作时间',
    align: 'center',
    width: '120px',
    formatter: (row: BatchDetailDTO) => {
      if (!row.time) return '-'
      const time = typeof row.time === 'string' ? new Date(row.time) : row.time
      return formatDateTime(time) || '-'
    }
  },
  {
    prop: 'type',
    label: '单据类型',
    align: 'center',
    width: '120px',
    formatter: (row: BatchDetailDTO) => {
      const typeMap: Record<string, string> = {
        buy: '采购单',
        bre: '采购退货单',
        sell: '销售单',
        sre: '销售退货单',
        swapOut: '调拨单-出',
        swapEnter: '调拨单-入',
        entry: '其它入库单',
        extry: '其它出库单'
      }
      return typeMap[row.type || ''] || row.type || '-'
    }
  },
  {
    prop: 'number',
    label: '单据编号',
    align: 'center',
    minWidth: '200px',
    formatter: (row: BatchDetailDTO) => {
      return row.number || '-'
    }
  },
  {
    prop: 'info',
    label: '操作类型',
    align: 'center',
    width: '100px',
    formatter: (row: BatchDetailDTO) => {
      return row.info || '-'
    }
  },
  {
    prop: 'nums',
    label: '操作数量',
    align: 'center',
    width: '100px',
    formatter: (row: BatchDetailDTO) => {
      const num = row.nums || 0
      // 根据正负号判断显示
      if (num > 0) {
        return `+${num}`
      } else if (num < 0) {
        return `${num}`
      }
      return num.toString()
    }
  }
]

// 表格数据
const tableData = reactive<PageDTO<any>>({
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
    number: formData.number,
    type: formData.type,
    startTime: formData.startTime,
    endTime: formData.endTime
  }

  tableData.pageIndex = 1 // 搜索时重置到第一页
  loadTableData()
}

// 处理表格数据更新
function handleTableDataUpdate(newData: PageDTO<any>) {
  Object.assign(tableData, newData)
  loadTableData()
}

// 处理页面变化
function handlePageChange(pageData: PageDTO<any>) {
  Object.assign(tableData, pageData)
  loadTableData()
}

// 加载表格数据
async function loadTableData() {
  try {
    // 构建请求参数 - 根据mock数据结构调整
    const requestParams: BatchDetailRequest = {
      pid: props.goodsData?.id?.toString() || '', // 批次ID作为pid
      pageIndex: tableData.pageIndex,
      pageSize: tableData.pageSize,
      warehouseIds: [], // 添加必需的 warehouseIds 字段
      ...searchParams.value
    }

    console.log('加载批次详情数据:', requestParams)

    // 调用API接口（使用回调模式）
    await getBatchDetailList(
      requestParams,
      (data) => {
        tableData.rows = data.rows || []
        tableData.total = data.total || 0
      },
      (error) => {
        console.error('获取批次详情失败:', error)
        tableData.rows = []
        tableData.total = 0
      }
    )
  } catch (error) {
    console.error('加载批次详情失败:', error)
    tableData.rows = []
    tableData.total = 0
  }
}

// 处理导出
function handleExport() {
  const exportData = {
    searchParams: searchParams.value,
    tableData: tableData.rows,
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
