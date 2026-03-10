<!--
 * @Description: 搜索组件-获取人员列表（条件+分页）
-->
<template>
  <div class="people-search-container">
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
        <el-table-column prop="name" label="人员名称" width="150" />
        <el-table-column prop="employeeNo" label="人员编号" width="150" />
        <el-table-column prop="gender" label="性别" width="100" />
        <el-table-column prop="phone" label="联系电话" width="150" />
        <el-table-column prop="deptName" label="所属部门" width="150" />
        <el-table-column prop="position" label="岗位" width="150" />
      </el-table>
    </div>
    
    <!-- 无搜索结果提示 -->
    <div v-if="hasSearched && searchResults.length === 0" class="no-results">
      <el-empty description="未找到匹配的人员" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import GoodSearchForm from './GoodSearchForm.vue'
import type { SearchFormData, GoodSearchConfig } from './type'
import type { PersonnelListItem } from '@/apis/common/type'
import { filterPersonnel } from './mockData'

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
const searchResults = ref<PersonnelListItem[]>([])

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
      key: 'peopleName',
      label: '请输入人员名称',
      type: 'input'
    },
    {
      key: 'peopleCode',
      label: '请输入人员编号',
      type: 'input'
    },
    {
      key: 'gender',
      label: '请选择人员性别',
      type: 'select',
      options: [
        { label: '男', value: '男' },
        { label: '女', value: '女' }
      ]
    },
    {
      key: 'contactPhone',
      label: '请输入联系电话',
      type: 'input'
    },
    {
      key: 'contactAddress',
      label: '请输入联系地址',
      type: 'input'
    },
    {
      key: 'idCard',
      label: '请输入身份证号',
      type: 'input'
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
function handleSearch(formData: SearchFormData) {
  try {
    hasSearched.value = true
    
    const searchParams: Record<string, any> = {
      name: formData.peopleName, // 人员名称
      number: formData.peopleCode, // 人员编号
      gender: formData.gender, // 性别
      tel: formData.contactPhone, // 联系电话
      address: formData.contactAddress, // 联系地址
      idCard: formData.idCard, // 身份证号
      data: formData.remark // 备注信息
    }

    // 临时使用本地模拟数据
    const results = filterPersonnel(searchParams)
    searchResults.value = results || []

    emit('search', {
      ...searchParams,
      pageIndex: props.pagination?.pageIndex || 1,
      pageSize: props.pagination?.pageSize || 20
    })

    if (results.length === 0) {
      ElMessage.warning('未找到匹配的人员')
    }
  } catch (error: any) {
    console.error('搜索人员失败:', error)
    ElMessage.error('搜索人员失败，请稍后重试')
    searchResults.value = []
  }
}

/**
 * 处理表格行点击事件（选择人员）
 */
function handleRowClick(row: PersonnelListItem) {
  emit('search', {
    ...row,
    id: row.id, // 传递人员ID
    name: row.name, // 保留name用于显示
    number: row.employeeNo
  })
  ElMessage.success(`已选择人员：${row.name}`)
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

