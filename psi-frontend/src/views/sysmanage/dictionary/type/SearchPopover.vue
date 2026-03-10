<template>
  <GoodSearchForm v-model="searchForm" :config="searchConfig" @search="handleSearch" />
</template>

<script lang="ts" setup>
import { ref, reactive, watch } from 'vue'
import GoodSearchForm from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent/type'
import type { TypePageRequest } from '@/apis/sysmanage/dictionary/type'

// 定义组件属性
interface Props {
  modelValue: TypePageRequest
}

// 定义组件事件
interface Emits {
  (e: 'update:modelValue', value: TypePageRequest): void
  (e: 'search'): void
}

// 组件属性和事件
const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 使用自定义字段的搜索表单
const searchForm = ref<SearchFormData>({
  dictCode: props.modelValue.code || '',
  dictName: props.modelValue.name || ''
})

// 搜索配置：使用自定义字段
const searchConfig = reactive<GoodSearchConfig>({
  inline: false, // 使用 popover 模式
  // 隐藏所有默认字段
  showGoods: false,
  showNumber: false,
  showRemark: false,
  showSupplier: false,
  showCustomer: false,
  showWarehouse: false,
  showAccount: false,
  showPeople: false,
  showUser: false,
  showBillDate: false,
  showArrivalDate: false,
  showExamine: false,
  showState: false,
  // 使用自定义字段
  customFields: [
    {
      key: 'dictName',
      type: 'input',
      label: '请输入字典类型名称'
    },
    {
      key: 'dictCode',
      type: 'input',
      label: '请输入字典类型编码'
    }
  ]
})

// 搜索方法
const handleSearch = (): void => {
  const dictionaryForm: TypePageRequest = {
    code: searchForm.value.dictCode || '',
    name: searchForm.value.dictName || ''
  }

  emit('update:modelValue', dictionaryForm)
  emit('search')
}

// 监听父组件的值变化
watch(
  () => props.modelValue,
  (newValue) => {
    searchForm.value.dictCode = newValue.code || ''
    searchForm.value.dictName = newValue.name || ''
  },
  { deep: true }
)

// 暴露方法给父组件
defineExpose({
  getFormData: () => {
    return {
      code: searchForm.value.dictCode || '',
      name: searchForm.value.dictName || ''
    }
  },
  setFormData: (data: Partial<TypePageRequest>) => {
    if (data.code !== undefined) searchForm.value.dictCode = data.code
    if (data.name !== undefined) searchForm.value.dictName = data.name
  },
  // 额外提供重置方法
  resetForm: () => {
    searchForm.value.dictCode = ''
    searchForm.value.dictName = ''
  }
})
</script>
