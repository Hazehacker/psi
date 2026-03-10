<!--
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-28 08:50:05
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-10-28 21:06:36
 * @FilePath: \psi-frontend\src\components\goodSearchConpoent\Supplier.vue
 * @Description: 搜索组件-获取供应商列表（条件+分页）
-->
<template>
  <div class="supplier-search-container">
    <GoodSearchForm v-model="searchForm" :config="searchConfig" @search="handleSearch" />
    
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
        <el-table-column prop="name" label="供应商名称" width="200" />
        <el-table-column prop="code" label="供应商编号" width="150" />
        <el-table-column prop="category" label="类别" width="120" />
        <el-table-column prop="contact" label="联系人" width="120" />
        <el-table-column prop="phone" label="联系电话" width="150" />
      </el-table>
    </div>
    
    <!-- 无搜索结果提示 -->
    <div v-if="hasSearched && searchResults.length === 0" class="no-results">
      <el-empty description="未找到匹配的供应商" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import GoodSearchForm from './GoodSearchForm.vue'
import type { SearchFormData, GoodSearchConfig } from './type'
import { SupplierAPI } from '@/apis/basicdata/supplier'
import type { SupplierListItem } from '@/apis/common/type'
import { filterSuppliers } from './mockData'

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
}>()

/**
 * 搜索表单数据
 */
const searchForm = ref<SearchFormData>({})

/**
 * 搜索结果列表
 */
const searchResults = ref<SupplierListItem[]>([])

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
  showUser: true,  // 启用用户搜索（所属用户）
  userPlaceholder: '请选择所属用户',  // 自定义用户字段的占位符
  showRemark: false,

  // 自定义字段
  customFields: [
    {
      key: 'supplierName',
      label: '请输入供应商名称',
      type: 'input'
    },
    {
      key: 'supplierCode',
      label: '请输入供应商编号',
      type: 'input'
    },
    {
      key: 'supplierCategory',
      label: '请选择供应商类别',
      type: 'select',
      options: [
        { label: '常规类别', value: '常规类别' },
        { label: '本地', value: '本地' },
        { label: '电商', value: '电商' },
        { label: '成都', value: '成都' }
      ]
    },
    {
      key: 'remark',
      label: '请输入备注信息',
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
    name: formData.supplierName, // 供应商名称
    code: formData.supplierCode, // 供应商编号（使用API期望的字段名）
    category: formData.supplierCategory, // 供应商类别
    belongUser: formData.user ?? undefined, // 所属用户（使用API期望的字段名，将null转换为undefined）
    remark: formData.remark, // 备注信息（使用API期望的字段名）
    pageIndex: props.pagination?.pageIndex || 1, // 当前页码
    pageSize: props.pagination?.pageSize || 20 // 每页数据条数
  }

  // 直接调用basicdata目录下的SupplierAPI进行搜索
  try {
    const result = await SupplierAPI.getSupplierList(searchParams)
    console.log('搜索供应商结果:', result)
    // 可以根据需要处理结果
  } catch (error) {
    console.error('搜索供应商失败:', error)
  }
}

/**
 * 处理表格行点击事件（选择供应商）
 */
function handleRowClick(row: SupplierListItem) {
  // 触发搜索事件，传递选中的供应商信息
  emit('search', {
    ...row,
    supplierName: row.name,
    supplierId: row.id
  })
  ElMessage.success(`已选择供应商：${row.name}`)
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
