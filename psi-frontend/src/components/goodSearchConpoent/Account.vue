<!--
 * @Description: 搜索组件-账户搜索（条件+分页）
-->
<template>
  <div class="account-search-container">
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
        <el-table-column prop="name" label="账户名称" width="200" />
        <el-table-column prop="accountNo" label="账户编号" width="200" />
        <el-table-column prop="bank" label="开户银行" width="150" />
        <el-table-column prop="type" label="账户类型" width="120" />
        <el-table-column prop="balance" label="账户余额" width="150">
          <template #default="{ row }">
            ¥{{ row.balance?.toLocaleString() || '0.00' }}
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 无搜索结果提示 -->
    <div v-if="hasSearched && searchResults.length === 0" class="no-results">
      <el-empty description="未找到匹配的账户" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import GoodSearchForm from './GoodSearchForm.vue'
import type { SearchFormData, GoodSearchConfig } from './type'
import type { AccountListItem } from '@/apis/common/type'
import { filterAccounts } from './mockData'
import { AccountAPI } from '@/apis/basicdata/account'

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
const searchResults = ref<AccountListItem[]>([])

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
  showAccount: false,  // 防止嵌套账户搜索
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
    { key: 'accountName', label: '请输入账户名称', type: 'input' },
    { key: 'accountCode', label: '请输入账户编号', type: 'input' }
  ]
})

/**
 * 处理搜索事件
 */
async function handleSearch(formData: SearchFormData) {
  const searchParams = {
    name: formData.accountName, // 账户名称
    number: formData.accountCode, // 账户编号
    pageIndex: props.pagination?.pageIndex || 1,
    pageSize: props.pagination?.pageSize || 20
  }

  // 直接调用basicdata目录下的AccountAPI进行搜索
  try {
    const result = await AccountAPI.getAccountList(searchParams)
    console.log('搜索账户结果:', result)
    // 可以根据需要处理结果
  } catch (error) {
    console.error('搜索账户失败:', error)
  }
}

/**
 * 处理表格行点击事件（选择账户）
 */
function handleRowClick(row: AccountListItem) {
  // 触发搜索事件，传递选中的账户信息
  emit('search', {
    ...row,
    accountName: row.name,
    accountId: row.id
  })
  ElMessage.success(`已选择账户：${row.name}`)
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


