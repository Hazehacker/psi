<template>
  <div class="mydatatable-demo">
    <MyDataTable 
      :tabtitle="'商品数据表'"
      :tabdatacolumns="columns"
      :tabdata="tableData"
      :tabattr="tableAttrs"
      :istabseq="true"
      :istabmultiple="false"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import MyDataTable from '@/components/mydatatable/MyDataTable.vue'
import type { MyTableColumn } from '@/components/mydatatable/type'

// 表格列配置
const columns: MyTableColumn[] = [
  { prop: 'operate', label: '操作', width: '60', fixed: 'left' },
  { prop: 'name', label: '商品名称', width: '180' },
  { prop: 'code', label: '商品编码', width: '120' },
  { prop: 'model', label: '规格型号', width: '120' },
  { prop: 'unit', label: '单位', width: '80' },
  { prop: 'price', label: '仓库', width: '100' },
  { prop: 'quantity', label: '单价', width: '100' },
  { prop: 'discount', label: '折扣(%)', width: '100' },
  { prop: 'tax', label: '税率(%)', width: '100' },
  { prop: 'taxAmount', label: '税额', width: '100' },
  { prop: 'amount', label: '金额', width: '100' },
  { prop: 'discountAmount', label: '折扣金额(%)', width: '100' },
  { prop: 'totalAmount', label: '价税合计', width: '120' }
]

// 表格属性
const tableAttrs = {
  border: true,
  stripe: true,
  'highlight-current-row': true
}

// 表格数据
const tableData = ref({
  rows: [
    {
      name: '零星采购',
      code: '0082',
      model: '',
      unit: '件',
      price: '仓库名称',
      quantity: 130,
      discount: 1,
      tax: 0,
      taxAmount: 0,
      amount: 100,
      discountAmount: 0,
      totalAmount: 100
    },
    {
      name: '',
      code: '',
      model: '',
      unit: '',
      price: '',
      quantity: '',
      discount: '',
      tax: '',
      taxAmount: '',
      amount: '',
      discountAmount: '',
      totalAmount: ''
    }
  ],
  pageIndex: 1,
  pageSize: 10,
  total: 2
})
</script>

<style scoped>
.mydatatable-demo {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

h2 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #409EFF;
}

p {
  margin-bottom: 20px;
  color: #606266;
}
</style>