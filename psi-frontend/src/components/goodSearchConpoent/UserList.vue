<!--
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-28 08:50:05
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-10-28 23:18:46
 * @FilePath: \psi-frontend\src\components\goodSearchConpoent\UserList.vue
 * @Description: 搜索组件-获取用户列表（条件+分页）
-->
<template>
  <div class="user-search-container">
    <good-search-form v-model="searchForm" :config="searchConfig" @search="handleSearch" />
    
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
        <el-table-column prop="realName" label="用户名称" width="150" />
        <el-table-column prop="username" label="用户账号" width="150" />
        <el-table-column prop="phone" label="手机号码" width="150" />
        <el-table-column prop="deptName" label="所属部门" width="150" />
        <el-table-column prop="roleName" label="角色" width="150" />
      </el-table>
    </div>
    
    <!-- 无搜索结果提示 -->
    <div v-if="hasSearched && searchResults.length === 0" class="no-results">
      <el-empty description="未找到匹配的用户" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import GoodSearchForm from './GoodSearchForm.vue'
import type { GoodSearchConfig, SearchFormData } from './type'
import type { UserListItem } from '@/apis/common/type'
import { filterUsers } from './mockData'

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

// 搜索表单数据
const searchForm = ref<SearchFormData>({})

// 搜索结果列表
const searchResults = ref<UserListItem[]>([])

// 是否已搜索过
const hasSearched = ref(false)

// 搜索配置
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
      key: 'name',
      label: '请输入用户名称',
      type: 'input'
    },
    {
      key: 'user',
      label: '请输入用户账号',
      type: 'input'
    },
    {
      key: 'tel',
      label: '请输入手机号码',
      type: 'input'
    },
    {
      key: 'data',
      label: '请输入备注信息',
      type: 'input'
    }
  ]
})

// 处理搜索事件
function handleSearch(formData: SearchFormData) {
  try {
    hasSearched.value = true
    
    const searchParams: Record<string, any> = {
      name: formData.name,
      user: formData.user,
      tel: formData.tel,
      data: formData.data
    }

    // 临时使用本地模拟数据
    const results = filterUsers(searchParams)
    searchResults.value = results || []

    emit('search', {
      ...searchParams,
      pageIndex: props.pagination?.pageIndex || 1,
      pageSize: props.pagination?.pageSize || 20
    })

    if (results.length === 0) {
      ElMessage.warning('未找到匹配的用户')
    }
  } catch (error: any) {
    console.error('搜索用户失败:', error)
    ElMessage.error('搜索用户失败，请稍后重试')
    searchResults.value = []
  }
}

// 处理表格行点击事件（选择用户）
function handleRowClick(row: UserListItem) {
  emit('search', {
    ...row,
    name: row.realName,
    user: row.username
  })
  ElMessage.success(`已选择用户：${row.realName}`)
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
