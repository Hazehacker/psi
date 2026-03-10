<template>
  <div class="settlement-table-container">
    <el-table
      ref="tableRef"
      class="settlement-table"
      :data="localRows"
      style="width: 100%"
      v-bind="props.tabattr"
      @row-dblclick="handleRowDblclick"
      border
    >
      <!-- 序号列 -->
      <el-table-column type="index" width="50" align="center" label="#">
        <template #header>
          <div class="clickable-header" @click="openFieldConfig">
            <el-icon><Setting /></el-icon>
          </div>
        </template>
        <template #default="scope">
          {{ scope.row.isSecondRow ? scope.$index + 1 : scope.$index + 1 }}
        </template>
      </el-table-column>
      
      <!-- 操作列 -->
      <el-table-column label="操作" width="60" align="center">
        <template #default="scope">
          <el-button v-if="!scope.row.isSecondRow" type="primary" size="small" circle plain @click="showSecondRow(scope.row)">
            <el-icon><CirclePlus /></el-icon>
          </el-button>
          <el-button v-else type="primary" size="small" circle plain>
            <el-icon><Document /></el-icon>
          </el-button>
        </template>
      </el-table-column>
      
      <!-- 结算账户列 -->
      <el-table-column v-if="columnVisibles.account" prop="account" label="结算账户" min-width="120" align="center">
        <template #default="scope">
          <div>
            <span v-if="!scope.row.account" class="clickable-text">点击选择</span>
            <span v-else>{{ scope.row.account }}</span>
          </div>
        </template>
      </el-table-column>
      
      <!-- 结算金额列 -->
      <el-table-column v-if="columnVisibles.amount" prop="amount" label="结算金额" min-width="120" align="center" />
      
      <!-- 结算号列 -->
      <el-table-column v-if="columnVisibles.number" prop="number" label="结算号" min-width="120" align="center" />
      
      <!-- 备注信息列 -->
      <el-table-column v-if="columnVisibles.remark" prop="remark" label="备注信息" min-width="150" align="center" />
    </el-table>
    
    <!-- 展开的第二行 -->
    <div v-for="(row, index) in localRows" :key="index + '-second'" v-show="row.isSecondRow" class="second-row-container">
      <div class="second-row-content">
        <div class="second-row-index">{{ index + 2 }}</div>
        <div class="second-row-icon">
          <el-icon><Document /></el-icon>
        </div>
        <div class="second-row-text">点击选择</div>
      </div>
    </div>
    
    <!-- 空数据提示 -->
    <div v-if="!localRows || localRows.length === 0" class="empty-data">
      暂无数据
    </div>
    
    <!-- 底部统计信息 -->
    <div class="table-footer">
      <div class="footer-left">
        <span>共 {{ totalCount }} 条</span>
      </div>
      <div class="footer-right">
        <span>总金额：{{ totalAmount }}</span>
      </div>
    </div>
    
    <!-- 字段配置弹窗 -->
    <el-dialog
      v-model="configVisible"
      title="字段配置"
      width="400px"
      :close-on-click-modal="true"
      :show-close="true"
    >
      <div class="field-config">
        <div class="field-header">
          <div class="field-title">名称</div>
          <div class="field-title">显示</div>
        </div>
        <div v-for="(field, key) in fieldConfig" :key="key" class="field-row">
          <div class="field-name">{{ field.label }}</div>
          <div class="field-switch">
            <el-switch v-model="columnVisibles[key]" />
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { type PropType, computed, ref, watch, onMounted, onBeforeUnmount } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElIcon, ElDialog } from 'element-plus'
import type { TableInstance } from 'element-plus'
import { CirclePlus, Setting, Document } from '@element-plus/icons-vue'
import {
  type TableProps,
  type TableColumn
} from './type'

// 定义行数据接口
interface RowData {
  id: number;
  account: string;
  amount: string;
  number: string;
  remark: string;
  isSecondRow?: boolean;
}

// 创建分页数据对象
interface PageDTO<T> {
  rows: T[];
  pageIndex: number;
  pageSize: number;
  total: number;
}

// el-table的引用
const tableRef = ref<TableInstance>()

// 字段配置弹窗控制
const configVisible = ref(false)
const columnVisibles = ref({
  account: true,
  amount: true,
  number: true,
  remark: true
})

// 字段配置定义
const fieldConfig = {
  account: { label: '结算账户' },
  amount: { label: '结算金额' },
  number: { label: '结算号' },
  remark: { label: '备注信息' }
}

// 生命周期钩子 - 确保弹窗状态正确初始化
onMounted(() => {
  configVisible.value = false
})

onBeforeUnmount(() => {
  configVisible.value = false
})

// 组件属性定义
const props = defineProps({
  /** 表格标题, 默认无标题 */
  tabtitle: String,
  /** 表格属性 */
  tabattr: Object as PropType<TableProps>,
  /** 表格列数据 */
  columns: Array as PropType<TableColumn[]>,
  /** 表格数据 */
  tabdata: {
    type: Object as PropType<PageDTO<RowData>>,
    default: () => ({
      rows: [],
      pageIndex: 1,
      pageSize: 10,
      total: 0
    })
  }
})

// 本地行数据
const localRows = computed(() => {
  return props.tabdata?.rows || []
})

// 计算总条数
const totalCount = computed(() => {
  return props.tabdata?.total || 0
})

// 计算总金额
const totalAmount = computed(() => {
  if (!localRows.value || localRows.value.length === 0) return '0';
  
  const total = localRows.value.reduce((sum, item: any) => {
    const amount = parseFloat(item.amount || '0');
    return sum + (isNaN(amount) ? 0 : amount);
  }, 0);
  
  return total.toFixed(2);
})

// 定义事件
const emit = defineEmits<{
  (e: 'selection-change', selection: RowData[]): void
  (e: 'row-dblclick', row: RowData, column: any, event: Event): void
}>()

// 打开字段配置弹窗
const openFieldConfig = () => {
  configVisible.value = true
}

// 添加新行
const showSecondRow = (row: any) => {
  // 获取当前行的索引
  const index = localRows.value.findIndex((item: any) => item.id === row.id);
  if (index !== -1) {
    // 创建新行数据
    const newRow = {
      id: Date.now(), // 使用时间戳作为唯一ID
      account: '点击选择',
      amount: '',
      number: '',
      remark: '',
      isSecondRow: true // 标记为第二行
    };
    // 在当前行后插入新行
    localRows.value.splice(index + 1, 0, newRow);
  }
}

// 处理选择按钮点击
const handleSelect = (index: number, row: RowData) => {
  emit('selection-change', [row]);
}

// 处理双击行
const handleRowDblclick = (row: RowData, column: any, event: Event) => {
  emit('row-dblclick', row, column, event);
}

// 处理选择变更
const handleSelectionChange = (selection: RowData[]) => {
  emit('selection-change', selection)
}
</script>

<style scoped>
.settlement-table-container {
  width: 100%;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}

.settlement-table {
  width: 100%;
}

.settlement-table :deep(.el-table__header) {
  background-color: #f5f7fa;
}

.settlement-table :deep(.el-table__row:hover) {
  background-color: #f5f7fa;
}

.clickable-text {
  color: #409eff;
  cursor: pointer;
}

.clickable-text:hover {
  text-decoration: underline;
}

.table-footer {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 12px 20px;
  background-color: #fff;
  border-top: 1px solid #ebeef5;
}

.footer-stat {
  font-size: 14px;
  color: #606266;
}

.empty-data {
  padding: 40px 0;
  text-align: center;
  color: #909399;
  font-size: 14px;
}

/* 字段配置弹窗样式 */
.field-config {
  padding: 16px 0;
}

.field-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px 12px;
  border-bottom: 1px solid #ebeef5;
  font-weight: bold;
}

.field-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.field-row:last-child {
  border-bottom: none;
}

.field-name {
  font-size: 14px;
  color: #303133;
}

.field-title {
  font-size: 14px;
  color: #606266;
}

.field-switch {
  display: flex;
  align-items: center;
}

.second-row-container {
  display: flex;
  border: 1px solid #dfe6ec;
  border-top: none;
  background-color: #fff;
}

.second-row-content {
  display: flex;
  align-items: center;
  width: 100%;
  height: 40px;
}

.second-row-index {
  width: 50px;
  text-align: center;
}

.second-row-icon {
  width: 60px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.second-row-text {
  flex: 1;
  padding-left: 10px;
  color: #409eff;
  cursor: pointer;
}
</style>
