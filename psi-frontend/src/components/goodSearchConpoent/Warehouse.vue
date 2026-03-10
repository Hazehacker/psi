<!--
 * @Description: 搜索组件-获取仓库列表（条件+分页）
-->
<template>
  <div class="warehouse-search-container">
    <GoodSearchForm 
      v-model="searchForm" 
      :config="searchConfig" 
      @search="handleSearch"
      @confirm="handleConfirm"
    />
    
    <!-- 搜索结果列表 -->
    <div v-if="searchResults.length > 0" class="search-results">
      <el-table
        :data="searchResults"
        stripe
        highlight-current-row
        @row-click="handleRowClick"
        style="width: 100%; margin-top: 16px;"
        max-height="400"
      >
        <el-table-column prop="name" label="仓库名称" width="200" />
        <el-table-column prop="code" label="仓库编号" width="150" />
        <el-table-column prop="address" label="仓库地址" show-overflow-tooltip />
      </el-table>
    </div>
    
    <!-- 无搜索结果提示 -->
    <div v-if="hasSearched && searchResults.length === 0" class="no-results">
      <el-empty description="未找到匹配的仓库" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import GoodSearchForm from './GoodSearchForm.vue'
import type { SearchFormData, GoodSearchConfig } from './type'
import { WarehouseAPI } from '@/apis/basicdata/warehouse'
// import { getWarehouseList } from '@/apis/common' // 临时注释，使用本地数据
import type { Warehouse } from '@/apis/common/type'
import { filterWarehouses } from './mockData'

// 定义分页数据类型
interface PaginationModel {
  pageIndex: number
  pageSize: number
}

// 定义 props
const props = defineProps<{
  pagination?: PaginationModel
}>()

// 定义 emits
const emit = defineEmits<{
  'update:pagination': [value: PaginationModel]
  search: [params: any]
  confirm: [params: any]
}>()

/**
 * 搜索表单数据
 */
const searchForm = ref<SearchFormData>({})

/**
 * 搜索结果列表
 */
const searchResults = ref<Warehouse[]>([])

/**
 * 是否已搜索过
 */
const hasSearched = ref(false)

/**
 * 搜索配置
 */
const searchConfig = reactive<GoodSearchConfig>({
  // 使用内联模式（不显示popover触发按钮）
  inline: true,
  
  // 隐藏不需要的字段
  showSupplier: false,
  showCustomer: false,
  showWarehouse: false,
  showAccount: false,
  showGoods: false,
  showNumber: false,
  showBillDate: false,
  showArrivalDate: false,
  showExamine: false,
  showState: false,
  showPeople: false,
  showUser: false,
  showRemark: false,

  // 自定义字段
  customFields: [
    {
      key: 'warehouseName',
      label: '请输入仓库名称',
      type: 'input'
    },
    {
      key: 'warehouseCode',
      label: '请输入仓库编号',
      type: 'input'
    }
  ]
})

/**
 * 处理搜索事件
 */
async function handleSearch(formData: SearchFormData) {
  // 在这里处理搜索逻辑，比如调用API
  // 可以根据实际需求将参数转换为后端需要的格式
  const searchParams = {
    name: formData.warehouseName, // 仓库名称
    number: formData.warehouseCode, // 仓库编号
    pageIndex: props.pagination?.pageIndex || 1, // 当前页码
    pageSize: props.pagination?.pageSize || 20 // 每页数据条数
  }

  // 直接调用basicdata目录下的WarehouseAPI进行搜索
  try {
    const result = await WarehouseAPI.getWarehouseList(searchParams)
    console.log('搜索仓库结果:', result)
    // 可以根据需要处理结果
  } catch (error) {
    console.error('搜索仓库失败:', error)
  }
}

/**
 * 处理确认事件
 */
function handleConfirm(params: any) {
  // 将 confirm 事件传递给父组件
  emit('confirm', params)
}

/**
 * 处理表格行点击事件（选择仓库）
 */
function handleRowClick(row: Warehouse) {
  // 触发确认事件，传递选中的仓库信息
  emit('confirm', {
    ...row,
    // 确保这些字段存在（用于兼容不同的字段名）
    warehouseName: row.name,
    warehouseId: row.id
  })
  ElMessage.success(`已选择仓库：${row.name}`)
}

// 暴露方法给父组件
defineExpose({
  getFormData: () => searchForm.value,
  resetSearch: () => {
    searchForm.value = {}
  },
  triggerSearch: () => {
    handleSearch(searchForm.value)
  },
  searchForm
})
</script>

