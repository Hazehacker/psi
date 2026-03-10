<template>
  <GoodSearchForm
    :model-value="searchModel"
    :config="searchConfig"
    :default-days="defaultDays"
    @update:model-value="handleModelValueUpdate"
    @search="handleSearch"
    ref="goodSearchRef"
  />
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'
import GoodSearchForm from './GoodSearchForm.vue'
import type { SearchFormData, GoodSearchConfig } from './type'
import { PurchaseOrderAPI, type PurchaseOrderListQuery, type PurchaseOrderListItem } from '@/apis/purchase/PurchaseBooking/order'
import type { ApiResponse, PageDTO } from '@/apis/type'

/**
 * 组件属性
 */
interface Props {
  /** 分页页码 */
  pageIndex?: number
  /** 每页条数 */
  pageSize?: number
  /** 默认日期范围（天数） */
  defaultDays?: number
}

/**
 * 组件事件
 */
interface Emits {
  /** 搜索事件：返回查询参数和结果 */
  (e: 'search', query: PurchaseOrderListQuery, result: PageDTO<PurchaseOrderListItem>): void
  /** 查询参数变化事件 */
  (e: 'query-change', query: PurchaseOrderListQuery): void
  /** 加载状态变化事件 */
  (e: 'loading-change', loading: boolean): void
}

const props = withDefaults(defineProps<Props>(), {
  pageIndex: 1,
  pageSize: 10,
  defaultDays: 30
})

const emit = defineEmits<Emits>()

// 搜索表单引用
const goodSearchRef = ref<InstanceType<typeof GoodSearchForm> | null>(null)

// 搜索表单数据
const searchModel = reactive<SearchFormData>({})

// 搜索配置：只显示采购订单需要的字段
const searchConfig: GoodSearchConfig = {
  inline: false, // 使用 Popover 模式
  showGoods: true, // 商品名称
  showNumber: true, // 单据编号
  showSupplier: true, // 供应商
  showCustomer: false, // 不显示客户
  showWarehouse: false, // 不显示仓库
  showAccount: false, // 不显示资金账户
  showPeople: true, // 关联人员
  showUser: true, // 制单人
  showBillDate: true, // 单据日期范围
  showArrivalDate: true, // 到货日期范围
  showExamine: true, // 审核状态
  showState: true, // 入库状态
  showRemark: true // 备注信息
}

// 加载状态
const loading = ref(false)
// 标记是否已经初始化过搜索
const hasInitialized = ref(false)

/**
 * 将对象值转换为文本
 */
const toText = (v: any): string => {
  if (v == null) return ''
  if (typeof v === 'object') {
    return v.label || v.name || v.text || String(v.id ?? '')
  }
  return String(v)
}

/**
 * 审核状态映射：数字 -> 文本
 */
const examineMap: Record<string | number, string> = {
  1: '未审核',
  2: '已审核'
}

/**
 * 入库状态映射：数字 -> 文本
 */
const stateMap: Record<string | number, string> = {
  1: '未入库',
  2: '部分入库',
  3: '已入库',
  4: '关闭'
}

/**
 * 将 SearchFormData 转换为 PurchaseOrderListQuery
 */
const convertSearchToQuery = (cond: SearchFormData): PurchaseOrderListQuery => {
  const query: PurchaseOrderListQuery = {
    pageIndex: props.pageIndex,
    pageSize: props.pageSize
  }

  // 商品名称 -> name
  if (cond.goods) {
    query.name = cond.goods
  }

  // 单据编号 -> number
  if (cond.number) {
    query.number = cond.number
  }

  // 供应商（对象转文本）
  if (cond.supplier) {
    query.supplier = toText(cond.supplier)
  }

  // 关联人员（对象转文本）
  if (cond.people) {
    query.people = toText(cond.people)
  }

  // 制单人（对象转文本）
  if (cond.user) {
    query.user = toText(cond.user)
  }

  // 审核状态（数字转文本）
  if (cond.examine !== undefined && cond.examine !== null && cond.examine !== '') {
    query.examine = examineMap[cond.examine] || String(cond.examine)
  }

  // 入库状态（数字转文本）
  if (cond.state !== undefined && cond.state !== null && cond.state !== '') {
    query.state = stateMap[cond.state] || String(cond.state)
  }

  // 单据时间范围
  if (cond.startTime) {
    query.documentStartTime = cond.startTime
  }
  if (cond.endTime) {
    query.documentEndTime = cond.endTime
  }

  // 到货日期范围
  if (cond.startArrival) {
    query.arrivalStartTime = cond.startArrival
  }
  if (cond.endArrival) {
    query.arrivalEndTime = cond.endArrival
  }

  // 备注信息
  if (cond.data) {
    query.data = cond.data
  }

  return query
}

/**
 * 处理 model-value 更新
 */
const handleModelValueUpdate = (val: SearchFormData) => {
  Object.assign(searchModel, val)
}

/**
 * 处理搜索事件
 */
const handleSearch = async (formData?: SearchFormData) => {
  try {
    loading.value = true
    emit('loading-change', true)

    // 使用传入的 formData 或当前的 searchModel
    const searchData = formData || searchModel
    
    // 更新 searchModel（如果传入了新的 formData）
    if (formData) {
      Object.assign(searchModel, formData)
    }

    // 转换搜索条件
    const query = convertSearchToQuery(searchData)
    
    console.log('采购订单搜索，查询参数:', query)
    
    // 调用接口
    const response = await PurchaseOrderAPI.getPurchaseOrderList(query)
    
    if (response.code === 10000 && response.data) {
      console.log('获取采购订单列表成功，共', response.data.total || 0, '条')
      
      // 标记已初始化
      hasInitialized.value = true
      
      // 触发搜索事件，传递查询参数和结果
      emit('search', query, response.data)
      emit('query-change', query)
    } else {
      ElMessage.error(response.message || '获取采购订单列表失败')
      // 即使失败也触发事件，传递空结果
      emit('search', query, { 
        pageIndex: props.pageIndex, 
        pageSize: props.pageSize, 
        rows: [], 
        total: 0 
      })
      emit('query-change', query)
    }
  } catch (error: any) {
    console.error('加载采购订单列表失败:', error)
    const errorMessage = error?.response?.data?.message || error?.message || '获取采购订单列表失败，请稍后重试'
    ElMessage.error(errorMessage)
    
    // 即使失败也触发事件，传递空结果
    const query = convertSearchToQuery(formData || searchModel)
    emit('search', query, { 
      pageIndex: props.pageIndex, 
      pageSize: props.pageSize, 
      rows: [], 
      total: 0 
    })
    emit('query-change', query)
  } finally {
    loading.value = false
    emit('loading-change', false)
  }
}

/**
 * 监听分页参数变化，自动触发搜索
 */
watch(
  () => [props.pageIndex, props.pageSize],
  () => {
    // 如果已经初始化过且不在加载中，分页变化时自动触发搜索
    if (hasInitialized.value && !loading.value) {
      handleSearch(searchModel)
    }
  }
)

/**
 * 重置表单
 */
const resetForm = () => {
  if (goodSearchRef.value?.resetForm) {
    goodSearchRef.value.resetForm()
  }
}

/**
 * 暴露方法给父组件
 */
defineExpose({
  resetForm,
  search: handleSearch
})
</script>

