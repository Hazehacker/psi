<template>
  <div class="page-container">
    <DataTable 
      :data="tableData"
      :columns="recordColumns"
    />
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import DataTable, { type TableColumn } from '../table/DataTable.vue';

// 列定义
const recordColumns: TableColumn[] = [
  { title: '苹果', key: 'apple', width: 100 },
  { title: '芒果', key: 'mango', width: 100 },
  { title: '火龙果', key: 'pitaya', width: 100 },
  { title: '西瓜', key: 'watermelon', width: 100 },
];

// 表格数据
const tableData = ref([
  { apple: '红富士', mango: '贵妃芒', pitaya: '红心', watermelon: '8424' },
  { apple: '嘎啦', mango: '金煌芒', pitaya: '白心', watermelon: '麒麟' },
  { apple: '阿克苏', mango: '凯特芒', pitaya: '红心', watermelon: '甜王' },
]);
</script>

<style scoped>
.page-container {
  padding: 20px;
}
</style>