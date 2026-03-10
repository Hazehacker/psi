<!--
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-24 13:30:12
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-08 17:01:18
 * @FilePath: \src\views\template\SearchPopover.vue
 * @Description: 模板管理-搜索弹出框组件
-->
<template>
  <GoodSearchForm
    ref="goodSearchFormRef"
    v-model="searchForm"
    :config="searchConfig"
    @search="handleSearch"
    @update:modelValue="handleFormUpdate"
  />
</template>

<script lang="ts" setup>
import { ref, watch, computed } from 'vue'
import GoodSearchForm from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent/type'
import type { PageRequest } from '@/apis/sysmanage/import/type'

// 定义组件属性
interface Props {
  modelValue: PageRequest
}

// 定义组件事件
interface Emits {
  (e: 'update:modelValue', value: PageRequest): void
  (e: 'search'): void
  (e: 'reset'): void
}

// 组件属性和事件
const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// GoodSearchForm 引用
const goodSearchFormRef = ref()

// 搜索配置 - 使用自定义字段来适配模板搜索需求
const searchConfig = computed<GoodSearchConfig>(() => ({
  inline: false, // 使用 popover 模式
  // 隐藏不需要的字段
  showGoods: false,
  showNumber: false,
  showSupplier: false,
  showCustomer: false,
  showWarehouse: false,
  showAccount: false,
  showPeople: false,
  showBillDate: false,
  showArrivalDate: false,
  showUser: false,
  showExamine: false,
  showState: false,
  showRemark: false,
  // 自定义字段配置
  customFields: [
    {
      key: 'templateName',
      label: '请输入模板名称',
      type: 'input'
    },
    {
      key: 'saveType',
      label: '请输入文件存储方式',
      type: 'input'
    },
    {
      key: 'status',
      label: '请选择状态',
      type: 'select',
      options: [
        { label: '未使用', value: 0 },
        { label: '使用中', value: 1 }
      ]
    }
  ]
}))

// 本地搜索表单数据 - 适配 GoodSearchForm 的数据结构
const searchForm = ref<SearchFormData>({
  templateName: '',
  saveType: '',
  status: '',
  // 初始化其他字段为空
  goods: '',
  number: '',
  supplier: null,
  customer: null,
  warehouse: null,
  account: null,
  people: null,
  user: null,
  startTime: '',
  endTime: '',
  startArrival: '',
  endArrival: '',
  examine: '',
  state: '',
  data: ''
})

// 监听父组件数据变化
watch(
  () => props.modelValue,
  (newValue) => {
    if (newValue) {
      searchForm.value = {
        ...searchForm.value,
        templateName: newValue.templateName || '',
        saveType: newValue.saveType || '',
        status: newValue.status || ''
      }
    }
  },
  { deep: true, immediate: true }
)

// 处理表单数据更新
const handleFormUpdate = (value: SearchFormData) => {
  // 只提取模板相关的字段传递给父组件
  const templateForm: PageRequest = {
    templateName: value.templateName,
    saveType: value.saveType,
    status: value.status ? Number(value.status) : undefined
  }
  emit('update:modelValue', templateForm)
}

// 搜索方法
const handleSearch = (): void => {
  // 搜索时已经通过 handleFormUpdate 更新了数据，直接触发搜索事件
  emit('search')
}

// 暴露方法给父组件
defineExpose({
  getFormData: () => {
    const formData = goodSearchFormRef.value?.formData || searchForm.value
    return {
      templateName: formData.templateName,
      saveType: formData.saveType,
      status: formData.status ? Number(formData.status) : undefined
    }
  },
  setFormData: (data: Partial<PageRequest>) => {
    if (goodSearchFormRef.value) {
      goodSearchFormRef.value.setFormData?.(data)
    } else {
      searchForm.value = { ...searchForm.value, ...data }
    }
  }
})
</script>
