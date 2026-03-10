<!--
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-29 07:56:03
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-03 21:41:03
 * @FilePath: \psi-frontend\src\views\warehouse\batch\SearchPopover.vue
 * @Description: 仓库管理-批次查询-搜索弹窗
-->
<template>
  <GoodSearchForm v-model="searchForm" :config="searchConfig" @search="handleSearch" />
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import GoodSearchForm from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import type { GoodSearchConfig, SearchFormData } from '@/components/goodSearchConpoent/type'

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

// 搜索配置 - 根据新的搜索框内容调整
const searchConfig = reactive<GoodSearchConfig>({
  // 显示需要的字段
  showGoods: true, // 显示商品名称
  showWarehouse: true, // 启用仓库高级搜索
  // 隐藏其他不需要的字段
  showNumber: false,
  showSupplier: false,
  showPeople: false,
  showBillDate: false,
  showArrivalDate: false,
  showUser: false,
  showExamine: false,
  showState: false,
  showRemark: false,

  // 自定义字段 - 根据新的搜索框内容调整
  customFields: [
    {
      key: 'number',
      label: '请输入商品编号',
      type: 'input'
    },
    {
      key: 'batch',
      label: '请输入批次号码',
      type: 'input'
    },
    {
      key: 'time',
      label: '请选择生产日期',
      type: 'date'
    },
    {
      key: 'spec',
      label: '请输入商品型号',
      type: 'input'
    },
    {
      key: 'goodsCategoryId',
      label: '请选择商品类别',
      type: 'treeSelect',
      checkStrictly: true, // 可选择任意级别
      renderAfterExpand: false,
      clearable: true,
      treeData: [
        {
          value: '0',
          label: '默认类型',
          children: [
            {
              value: '1',
              label: '电子产品',
              children: [
                {
                  value: '1-1',
                  label: '手机'
                },
                {
                  value: '1-2',
                  label: '电脑'
                }
              ]
            },
            {
              value: '2',
              label: '服装',
              children: [
                {
                  value: '2-1',
                  label: '男装'
                },
                {
                  value: '2-2',
                  label: '女装'
                }
              ]
            }
          ]
        }
      ]
    },
    {
      key: 'brand',
      label: '请选择商品品牌',
      type: 'select',
      options: [
        // 这里可以留空，由外部传入或动态加载
        // 或者根据实际情况预置一些选项
      ]
    },
    {
      key: 'code',
      label: '请输入商品条码',
      type: 'input'
    },
    // 移除原来的 warehouse 自定义字段，因为现在使用组件内置的仓库搜索
    {
      key: 'state',
      label: '查询类型',
      type: 'select',
      options: [
        { label: '常规批次', value: 0 },
        { label: '预警批次', value: 1 }
      ]
    }
  ]
})

// 处理搜索事件
function handleSearch(formData: SearchFormData) {
  const searchParams = {
    name: formData.goods, // 商品名称
    number: formData.number, // 商品编号
    batch: formData.batch, // 批次号码
    time: formData.time, // 生产日期
    spec: formData.spec, // 商品型号
    category: formData.category, // 商品类别
    brand: formData.brand, // 商品品牌
    code: formData.code, // 商品条码
    warehouse: formData.warehouse, // 仓库信息（使用组件内置的仓库搜索）
    state: formData.state, // 查询类型
    pageIndex: props.pagination?.pageIndex || 1,
    pageSize: props.pagination?.pageSize || 20
  }

  console.log('搜索参数：', searchParams)
  emit('search', searchParams)
}

// 设置品牌选项（可以从外部传入）
function setBrandOptions(brands: string[]) {
  const brandField = searchConfig.customFields?.find((field) => field.key === 'brand')
  if (brandField && brandField.type === 'select') {
    brandField.options = brands.map((brand) => ({ label: brand, value: brand }))
  }
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
  setBrandOptions,
  searchForm
})
</script>
