<!--
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-24 13:30:12
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-07 11:32:04
 * @FilePath: \src\views\sysmanage\dictionary\SearchPopover.vue
 * @Description: 系统配置-字典管理-搜索弹出框组件
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
import type { DictPageRequest, DictionaryTypeItem } from '@/apis/sysmanage/dictionary/type'

// 定义组件属性
interface Props {
  modelValue: DictPageRequest
  dictionaryTypes?: DictionaryTypeItem[] // 新增：字典类型列表
}

// 定义组件事件
interface Emits {
  (e: 'update:modelValue', value: DictPageRequest): void
  (e: 'search'): void
  (e: 'reset'): void
}

// 组件属性和事件
const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// GoodSearchForm 引用
const goodSearchFormRef = ref()

// 搜索配置 - 使用自定义字段来适配字典搜索需求
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
      key: 'name',
      label: '请输入字典名称',
      type: 'input'
    },
    {
      key: 'tid',
      label: '请选择字典类型',
      type: 'select',
      options:
        props.dictionaryTypes?.map((type) => ({
          label: type.name || '',
          value: type.id || ''
        })) || []
    },
    {
      key: 'value',
      label: '请输入字典值',
      type: 'input'
    }
  ]
}))

// 本地搜索表单数据 - 适配 GoodSearchForm 的数据结构
const searchForm = ref<SearchFormData>({
  name: '',
  tid: '',
  value: ''
})

// 监听父组件数据变化
watch(
  () => props.modelValue,
  (newValue) => {
    if (newValue) {
      searchForm.value = {
        ...searchForm.value,
        name: newValue.name || '',
        tid: newValue.tid || '',
        value: newValue.value || ''
      }
    }
  },
  { deep: true, immediate: true }
)

// 监听字典类型变化，更新搜索配置
watch(
  () => props.dictionaryTypes,
  () => {
    // 当字典类型变化时，GoodSearchForm 会自动通过 computed 的 searchConfig 更新
  },
  { deep: true }
)

// 处理表单数据更新
const handleFormUpdate = (value: SearchFormData) => {
  // 只提取字典相关的字段传递给父组件
  const dictionaryForm: DictPageRequest = {
    name: value.name || '',
    tid: value.tid || '', // 这里传递的是字典类型的ID
    value: value.value || ''
  }
  emit('update:modelValue', dictionaryForm)
}

// 搜索方法
const handleSearch = (): void => {
  // 搜索时已经通过 handleFormUpdate 更新了数据，直接触发搜索事件
  emit('search')
}

// 重置方法
const handleReset = (): void => {
  if (goodSearchFormRef.value) {
    goodSearchFormRef.value.resetForm()
  }

  const resetForm: DictPageRequest = {
    name: '',
    tid: '',
    value: ''
  }

  emit('update:modelValue', resetForm)
  emit('reset')
}

// 暴露方法给父组件
defineExpose({
  getFormData: () => {
    const formData = goodSearchFormRef.value?.formData || searchForm.value
    return {
      name: formData.name || '',
      tid: formData.tid || '',
      value: formData.value || ''
    }
  },
  setFormData: (data: Partial<DictPageRequest>) => {
    if (goodSearchFormRef.value) {
      goodSearchFormRef.value.setFormData?.(data)
    } else {
      searchForm.value = { ...searchForm.value, ...data }
    }
  },
  resetForm: handleReset
})
</script>
