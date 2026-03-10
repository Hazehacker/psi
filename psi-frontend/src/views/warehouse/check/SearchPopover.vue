<!--
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-29 07:56:03
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-03 21:39:21
 * @FilePath: \psi-frontend\src\views\warehouse\check\SearchPopover.vue
 * @Description: 仓库管理-库存盘点-搜索弹窗
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

// 搜索配置
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

  // 自定义字段
  customFields: [
    {
      key: 'goodsNumber',
      label: '请输入商品编号',
      type: 'input'
    },
    {
      key: 'goodsSpec',
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
      key: 'goodsBrand',
      label: '请选择商品品牌',
      type: 'select',
      options: [
        { label: '品牌A', value: 'A' },
        { label: '品牌B', value: 'B' },
        { label: '品牌C', value: 'C' }
        // 可根据实际业务补充品牌选项
      ]
    },
    {
      key: 'goodsCode',
      label: '请输入商品条码',
      type: 'input'
    },
    // 移除原来的 warehouseId 自定义字段，因为现在使用组件内置的仓库搜索
    {
      key: 'goodsRemark',
      label: '请输入商品备注',
      type: 'input'
    }
  ]
})

// 处理搜索事件
function handleSearch(formData: SearchFormData) {
  const searchParams = {
    goodsName: formData.goods, // 注意：组件内置的商品名称字段是 goods
    goodsNumber: formData.goodsNumber,
    goodsSpec: formData.goodsSpec,
    goodsCategoryId: formData.goodsCategoryId,
    goodsBrand: formData.goodsBrand,
    goodsCode: formData.goodsCode,
    warehouseName: formData.warehouse, // 使用组件内置的仓库字段
    goodsRemark: formData.goodsRemark, // 商品备注
    pageIndex: props.pagination?.pageIndex || 1,
    pageSize: props.pagination?.pageSize || 20
  }

  console.log('搜索参数：', searchParams)
  emit('search', searchParams)
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
