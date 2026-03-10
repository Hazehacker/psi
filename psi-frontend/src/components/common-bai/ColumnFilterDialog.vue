<template>
  <el-dialog 
    title="表格字段筛选" 
    v-model="dialogVisible" 
    width="390px"
    :before-close="handleClose"
  >
    <el-table 
      ref="filterTableRef"
      :data="filterOptions" 
      border  
      max-height="500"
      @selection-change="handleSelectionChange"
      :row-key="row => row.key"
    >
      <el-table-column 
        type="selection" 
        width="50"
        :reserve-selection="false"
      />
      <el-table-column 
        prop="title" 
        label="选项名" 
        align="center"
        min-width="120"
      />
    </el-table>
    
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="confirmSelection">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, watch, nextTick } from "vue";
import { ElTable, ElMessage } from 'element-plus';

// 定义列类型接口
export interface TableColumn {
  title: string;
  key: string;
  width: number;
}

// 定义props
const props = defineProps<{
  modelValue: boolean; // 控制弹窗显示
  allColumns: TableColumn[]; // 所有列
  selectedColumns: TableColumn[]; // 当前选中的列
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void;
  (e: 'confirm', selectedColumns: TableColumn[]): void;
}>();

const dialogVisible = ref(false);
const filterOptions = ref<Array<{ title: string; key: string }>>([]);
const filterTableRef = ref<InstanceType<typeof ElTable>>();
const currentSelection = ref<Array<{ title: string; key: string }>>([]);

// 初始化筛选选项
const initFilterOptions = () => {
  filterOptions.value = props.allColumns.map((item) => ({
    title: item.title,
    key: item.key
  }));
};

// 根据当前选中的列设置弹窗选择状态
const setSelectionFromSelectedColumns = () => {
  if (!filterTableRef.value) return;
  
  // 清空所有选择
  filterTableRef.value.clearSelection();
  
  // 获取当前选中列的key
  const selectedKeys = props.selectedColumns.map(col => col.key);
  
  // 选中对应的行
  nextTick(() => {
    filterOptions.value.forEach(row => {
      if (selectedKeys.includes(row.key)) {
        filterTableRef.value?.toggleRowSelection(row, true);
      }
    });
  });
  
  // 更新当前选择状态
  currentSelection.value = filterOptions.value.filter(row => 
    selectedKeys.includes(row.key)
  );
};

// 确认选择
const confirmSelection = () => {
  if (currentSelection.value.length === 0) {
    ElMessage.warning('请至少选择一个字段');
    return;
  }
  
  const selectedColumns = props.allColumns.filter(item => 
    currentSelection.value.some(selected => selected.key === item.key)
  );
  
  emit('confirm', selectedColumns);
  closeDialog();
};

// 监听选择变化
const handleSelectionChange = (selection: Array<{ title: string; key: string }>) => {
  currentSelection.value = selection;
};

// 关闭弹窗
const handleClose = () => {
  closeDialog();
};

const closeDialog = () => {
  emit('update:modelValue', false);
};

// 监听modelValue变化，同步dialogVisible
watch(() => props.modelValue, (val) => {
  dialogVisible.value = val;
  if (val) {
    initFilterOptions();
    nextTick(() => {
      setSelectionFromSelectedColumns();
    });
  }
});

// 监听所有列的变化
watch(() => props.allColumns, () => {
  if (dialogVisible.value) {
    initFilterOptions();
    nextTick(() => {
      setSelectionFromSelectedColumns();
    });
  }
}, { deep: true });

// 监听选中列的变化，如果弹窗打开则更新选择状态
watch(() => props.selectedColumns, () => {
  if (dialogVisible.value) {
    nextTick(() => {
      setSelectionFromSelectedColumns();
    });
  }
}, { deep: true });

// 初始化dialogVisible
watch(() => props.modelValue, (val) => {
  dialogVisible.value = val;
}, { immediate: true });
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>