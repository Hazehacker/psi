<template>
  <div class="datatable-demo">
    
    
    <DataTable 
      :tabattr="tableAttrs"
      :tabdata="tableData"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import DataTable from '@/components/datatable/DataTable.vue'

// 表格属性
const tableAttrs = {
  border: true,
  stripe: true,
  highlightCurrentRow: true
}

// 表格数据（PageDTO格式）
const tableData = ref({
  rows: [
    {
      id: 1,
      account: '现金账户',
      amount: '1000.00',
      number: 'JS001',
      remark: '第一笔结算'
    },
    {
      id: 2,
      account: '银行账户',
      amount: '2000.00',
      number: 'JS002',
      remark: '第二笔结算'
    },
    {
      id: 3,
      account: '支付宝账户',
      amount: '3000.00',
      number: 'JS003',
      remark: '第三笔结算'
    }
  ],
  pageIndex: 1,
  pageSize: 10,
  total: 3
})
</script>

<style scoped>
.datatable-demo {
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