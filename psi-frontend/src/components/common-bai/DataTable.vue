<template>
  <div class="data-table-container">
    <!-- 筛选按钮（放在表格内部） -->
    <div class="table-header">
      <el-button 
        type="primary" 
        @click="openFilterModal" 
        size="small"
        class="filter-btn"
      >
        <el-icon style="margin-right: 4px;">
          <Filter />
        </el-icon>
        <span>列筛选</span>
      </el-button>
    </div>

    <!-- 主表格 -->
    <el-table 
      :data="props.data" 
      border 
      :loading="loading" 
      max-height="500"
      style="width: 100%"
    >
      <template v-for="col in visibleColumns" :key="col.key">
        <el-table-column
          :prop="col.key"
          :label="col.title"
          :width="col.width"
          :show-overflow-tooltip="true"
        />
      </template>
    </el-table>

    <!-- 筛选弹窗组件 -->
    <ColumnFilterDialog 
      v-model="filterModal"
      :all-columns="props.columns"
      :selected-columns="visibleColumns"
      @confirm="handleColumnFilter"
    />
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";
import { Filter } from '@element-plus/icons-vue';
import { ElButton, ElIcon, ElTable, ElTableColumn } from 'element-plus';
import ColumnFilterDialog, { type TableColumn as FilterTableColumn } from './ColumnFilterDialog.vue';

// 定义列类型接口，与ColumnFilterDialog中的TableColumn一致
export interface TableColumn {
  title: string;
  key: string;
  width: number;
}

// 接收父组件属性
interface TableData {
  apple: string;
  mango: string;
  pitaya: string;
  watermelon: string;
  // 可以扩展其他字段
}

const props = defineProps<{
  data: TableData[];
  columns: TableColumn[]; // 所有可用的列配置
}>();

const loading = ref(false);
const filterModal = ref(false);
const visibleColumns = ref<TableColumn[]>([...props.columns]); // 当前显示的列

// 打开筛选弹窗
const openFilterModal = () => {
  filterModal.value = true;
};

// 处理列筛选确认
const handleColumnFilter = (selectedColumns: TableColumn[]) => {
  visibleColumns.value = selectedColumns;
};

// 组件挂载时初始化可见列
onMounted(() => {
  visibleColumns.value = [...props.columns];
});
</script>

<style scoped>
.data-table-container {
  padding: 20px;
}

.table-header {
  margin-bottom: 16px;
  display: flex;
  justify-content: flex-start;
}

.filter-btn {
  margin-bottom: 8px;
}
</style>