<template>
  <section class="table-container">
    <!-- 表格顶部 -->
    <section class="table-header">
      
      <h3 v-if="tabtitle">{{ tabtitle }}</h3>
      <h3 v-else></h3>
      <!-- 顶部插槽，用于支持扩展顶部操作栏 -->
      <section class="header-slot">
        <slot name="header" :table="tableRef" :props="props"></slot>
      </section>
    </section>
    <!-- 表格主体 -->
    <el-table ref="tableRef" :data="localRows" v-bind="reltabattr" @row-dblclick="handleRowDblclick"
      @selection-change="handleSelectionChange">
      <!-- 多选列 -->
      <el-table-column v-if="istabmultiple" type="selection" width="55" />
      <!-- 自定义展开行 -->
      <el-table-column v-if="istabexpand" fixed type="expand" width="30">
        <template #default="scope">
          <slot name="customerexpand" :row="scope.row"></slot>
        </template>
      </el-table-column>
      <!-- 序号列 -->
      <el-table-column v-if="istabseq" fixed="left" type="index" width="60" align="center" :index="(index: number) => ++index">
        <template #header>
          <span class="th-gear" @click="configVisible = true">⚙</span>
        </template>
      </el-table-column>
      <!-- 操作图标列 -->
      <!-- 操作列：空行显示“选择商品”图标，非空行显示“清空行”图标 -->
      <el-table-column fixed="left" label="操作" width="60" align="center">
        <template #header>
          <span class="th-gear" @click="configVisible = true">操作</span>
        </template>
        <template #default="scope">
          <el-icon v-if="isRowEmpty(scope.row)" class="op-icon op-icon--doc" @click="openProductDialog(scope.$index)">
            <Document />
          </el-icon>
          <el-icon v-else class="op-icon op-icon--delete" @click="clearRow(scope.$index)">
            <Delete />
          </el-icon>
        </template>
      </el-table-column>
      
      <!-- 数据列 -->
      <el-table-column v-for="(column, index) in displayColumns" :key="column.prop || index" v-bind="column">
        <template #header>
          <template v-if="column.prop === 'name' || (column.label && column.label.includes('商品名称'))">
            <div class="th-label-with-switch">
              <span>{{ column.label }}</span>
              <el-tooltip :content="modeLabel" placement="top" effect="light" popper-class="th-tooltip">
                <span class="th-switch" :class="{ 'is-on': isScanMode }" @click="isScanMode = !isScanMode">
                  <i class="th-switch-dot"></i>
                </span>
              </el-tooltip>
            </div>
          </template>
          <template v-else>
            {{ column.label }}
          </template>
        </template>
        <template #default="scope">
          <slot name="customercell" :column="column" :prop="column.prop" :index="scope.$index" :row="scope.row">
            <!-- 数据列 -->
            <template v-if="column.prop !== 'operate'">
              {{ scope.row[column.prop] }}
            </template>
          </slot>
        </template>
      </el-table-column>
    </el-table>
    <!-- 字段配置弹窗 -->
    <el-dialog v-model="configVisible" title="字段配置" width="420px" :close-on-click-modal="false">
      <div class="field-config" v-if="props.tabdatacolumns">
        <div class="field-row" v-for="col in props.tabdatacolumns.filter(c => c.prop !== 'operate')" :key="col.prop">
          <span class="field-name">{{ col.label }}</span>
          <el-switch v-model="columnVisibles[col.prop]" inline-prompt active-text="显示" inactive-text="" />
        </div>
      </div>
    </el-dialog>
    <!-- 商品列表弹窗 -->
    <el-dialog v-model="productDialogVisible" title="商品列表" width="800px" :close-on-click-modal="false">
      <el-table :data="prodTabData.rows" height="420" @selection-change="handleProductSelChange">
        <el-table-column type="selection" width="40" />
        <el-table-column prop="name" label="商品名称" />
        <el-table-column prop="code" label="商品编号" />
        <el-table-column prop="model" label="规格型号" />
        <el-table-column prop="category" label="商品分类" />
      </el-table>
      <template #footer>
        <div style="display:flex;justify-content:flex-end;gap:8px;">
          <el-button @click="productDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmProductSelection">选择</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 表格底部 -->
    <section class="table-footer">
      <section class="footer-slot">
        <slot name="footer" :table="tableRef" :props="props"></slot>
        <section class="footer-stat">总条数: {{ totalCount }} | 总合计: {{ sumTotal }}</section>
      </section>
      <!-- 分页组件 -->
      <el-pagination v-if="istabpage" layout="sizes, prev, pager, next, total" hide-on-single-page
        :size="reltabattr.psize" :page-sizes="[10, 20, 50]" :total="tabdata.total" :current-page="tabdata.pageIndex"
        @size-change="handlePageSizeChange" @current-change="handleCurrentPageChange" />
    </section>
  </section>
</template>

<script setup lang="ts" generic="T extends Object">
 // MyDataTable 组件逻辑：封装表格行为、列显隐、分页与商品选择
import { type PropType, computed, ref, watch } from 'vue'
import type { TableInstance } from 'element-plus'
import { Document, Delete } from '@element-plus/icons-vue'
import {
  type MyTableAttr,
  type MyTableColumn,
  type PageDTO,
  createPageDTO
} from './type'

// el-table的引用
const tableRef = ref<TableInstance>()

// 组件属性定义
const props = defineProps({
  /** 表格标题, 默认无标题 */
  tabtitle: String,
  /** 是否显示自定义展开行，默认不显示 */
  istabexpand: {
    type: Boolean,
    default: false
  },
  /** 是否显示序号列，默认不显示 */
  istabseq: {
    type: Boolean,
    default: false
  },
  /** 是否开启多选，默认不开启 */
  istabmultiple: {
    type: Boolean,
    default: false
  },
  /** 是否开启分页，默认开启 */
  istabpage: {
    type: Boolean,
    default: true
  },
  /** 表格属性 */
  tabattr: Object as PropType<MyTableAttr>,
  /** 表格列数据 */
  tabdatacolumns: Array as PropType<MyTableColumn[]>,
  /** 表格数据 */
  tabdata: {
    type: Object as PropType<PageDTO<T>>,
    default: () => createPageDTO()
  },
  /** 商品弹窗表数据 */
  producttabdata: Object as PropType<PageDTO<any>>
})

// 商品名称列头模式开关（常规/扫码）
const isScanMode = ref(false)
const modeLabel = computed(() => (isScanMode.value ? '扫码模式' : '常规模式'))

// 字段配置弹窗与可见性映射
const configVisible = ref(false)
const columnVisibles = ref<Record<string, boolean>>({})
// 根据 columnVisibles 映射动态过滤显示列
const displayColumns = computed<MyTableColumn[]>(() => {
  const cols = props.tabdatacolumns || []
  // 排除旧的 operate 操作列
  return cols.filter((c) => c.prop !== 'operate' && columnVisibles.value[c.prop] !== false)
})
watch(
  () => props.tabdatacolumns,
  // 初始化/同步字段可见性（默认全部显示）
  (cols) => {
    if (cols) {
      cols.forEach((c) => {
        if (columnVisibles.value[c.prop] === undefined) {
          columnVisibles.value[c.prop] = true
        }
      })
    }
  },
  { immediate: true }
)
/** 合并表格默认属性与传入属性 */
const reltabattr = computed<MyTableAttr>(() => {
  const defaultAttrs: MyTableAttr = {
    psize: 'default',
    border: true,
    stripe: true,
    'highlight-current-row': true
  }
  return {
    ...defaultAttrs,
    ...props.tabattr
  }
})


/** 声明emit事件 */
const emit = defineEmits<{
  (event: 'page-change', data: PageDTO<T>): void
  (event: 'selection-change', data: Array<T>): void
  (event: 'row-dbclick', data: T): void
  (event: 'product-open', index: number): void
  (event: 'row-clear', index: number): void
  (event: 'row-fill', index: number, row: any, product: any): void
}>()

/** 多选选中行发生改变 */
function handleSelectionChange(rows: T[]) {
  emit('selection-change', rows)
}

/** 双击行事件 */
function handleRowDblclick(row: T) {
  emit('row-dbclick', row)
}

/** 每页数据条数改变 */
const handlePageSizeChange = (val: number) => {
  emit('page-change', createPageDTO({ pageSize: val, pageIndex: 1 }))
}
/** 页码改变 */
const handleCurrentPageChange = (val: number) => {
  emit('page-change', createPageDTO({ pageSize: props.tabdata.pageSize, pageIndex: val }))
}

// 本地行数据，避免直接修改 props
const localRows = ref<Array<any>>([])
watch(
  () => props.tabdata?.rows,
  (rows) => {
    localRows.value = Array.isArray(rows) ? JSON.parse(JSON.stringify(rows)) : []
    ensureTrailingEmptyRow()
  },
  { immediate: true }
)

function isRowEmpty(row: any) {
  return !row || (!row.name && !row.code && !row.model && !row.unit && !row.price && !row.quantity)
}

function ensureTrailingEmptyRow() {
  const rows = localRows.value
  const last = rows[rows.length - 1]
  if (!last || !isRowEmpty(last)) {
    rows.push({})
  }
  while (rows.length > 1 && isRowEmpty(rows[rows.length - 1]) && isRowEmpty(rows[rows.length - 2])) {
    rows.pop()
  }
}

const productDialogVisible = ref(false)
const currentOpRowIndex = ref<number | null>(null)
const prodTabData = ref<PageDTO<any>>(createPageDTO())
watch(
  () => props.producttabdata,
  (val) => { if (val) prodTabData.value = val },
  { immediate: true }
)
const productSelectedRows = ref<any[]>([])
function handleProductSelChange(rows: any[]) { productSelectedRows.value = rows }
function openProductDialog(index: number) {
  currentOpRowIndex.value = index
  productDialogVisible.value = true
  emit('product-open', index)
}
function clearRow(index: number) {
  // 清空当前行数据
  localRows.value[index] = {}
  ensureTrailingEmptyRow()
  emit('row-clear', index)
}
function confirmProductSelection() {
  if (currentOpRowIndex.value == null || productSelectedRows.value.length === 0) return
  const prod = productSelectedRows.value[0]
  const tgt = localRows.value[currentOpRowIndex.value] || {}
  tgt.name = prod.name
  tgt.code = prod.code
  tgt.model = prod.model
  tgt.unit = prod.unit
  tgt.price = prod.price ?? 0
  if (!tgt.quantity) tgt.quantity = 1
  calculateAmounts(tgt)
  localRows.value[currentOpRowIndex.value] = tgt
  ensureTrailingEmptyRow()
  productDialogVisible.value = false
  emit('row-fill', currentOpRowIndex.value, tgt, prod)
}

function calculateAmounts(row: any) {
  const qty = Number(row.quantity || 0)
  const price = Number(row.price || 0)
  const rate = Number(row.taxRate || 0)
  row.amount = Number((qty * price).toFixed(2))
  row.tax = Number(((row.amount * rate) / 100).toFixed(2))
  row.total = Number((row.amount + row.tax).toFixed(2))
}

// 统计信息（总条数与总合计）
const nonEmptyCount = computed(() => localRows.value.filter((r) => !isRowEmpty(r)).length)
const totalCount = computed(() => props.tabdata?.total ?? nonEmptyCount.value)
const sumTotal = computed(() => {
  return localRows.value.reduce((acc, r) => acc + Number(r.total || 0), 0)
})

 </script>

<style scoped>
.table-container {
  width: 100%;
  background-color: #fff;
  border-radius: 5px;
  box-sizing: border-box;
  padding: 10px;
}

/* Header area */
.table-header {
  margin: 0.8% 0.5%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}

.header-slot {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  align-items: center;
}

/* Footer area */
.table-footer {
  margin: 1% 0.5%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: flex-start;
}

.footer-slot {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 10px;
}

/* Compact table styles to match screenshot colors */
:deep(.el-table) {
  --table-border-color: #e9edf1;
  /* 更浅的边线 */
  --table-header-bg-color: #f5f7f7;
  /* 表头背景颜色按要求调整 */
  --el-table-header-bg-color: #f5f7f7;
  /* 固定列也使用该色 */
  --el-table-border-color: #e9edf1;
  /* 统一 el-table 外边框与分隔线颜色 */
  background-color: #fff;
}

/* 表头整体底色 */
:deep(.el-table__header),
:deep(.el-table__header th) {
  background: var(--el-table-header-bg-color);
}

/* 兼容固定列（左/右）头部底色一致 */
:deep(.el-table__fixed-header-wrapper),
:deep(.el-table__fixed .el-table__header th),
:deep(.el-table__fixed-left .el-table__header th),
:deep(.el-table__fixed-right .el-table__header th) {
  background: var(--el-table-header-bg-color);
}

/* 固定列表头文字也统一样式 */
:deep(.el-table__fixed .el-table__header th .cell),
:deep(.el-table__fixed-left .el-table__header th .cell),
:deep(.el-table__fixed-right .el-table__header th .cell) {
  color: #8c9399;
  font-weight: 500;
  font-size: 12px;
  text-align: center;
}

/* 表头文字颜色和大小 */
:deep(.el-table__header th .cell) {
  color: #8c9399;
  font-weight: 500;
  font-size: 12px;
  padding: 6px 8px;
  text-align: center;
}

/* 单元格文字颜色与间距 */
:deep(.el-table__row .cell) {
  color: #666;
  font-size: 12px;
  padding: 6px 8px;
}

/* 垂直与水平分隔线颜色一致 */
:deep(.el-table__header th),
:deep(.el-table__body td) {
  border-right: 1px solid var(--table-border-color);
}

:deep(.el-table .el-table__cell) {
  border-bottom: 1px solid var(--table-border-color);
}

/* 悬浮与斑马纹颜色更柔和 */
:deep(.el-table__body tr:hover>td) {
  background: #f7f9fc;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background: #fafbfd;
}


/* Index column center alignment tweak */
:deep(.el-table .el-table__row .el-table__cell.is-index .cell) {
  text-align: center;
}

/* Reduce overall table header height and cell spacing to be compact */
:deep(.el-table .el-table__header th) {
  padding: 0;
  height: 32px;
}

:deep(.el-table .el-table__body td) {
  padding: 0;
  height: 34px;
}

/* 特殊列头样式：齿轮图标与灰色开关视觉 */
.th-gear {
  display: inline-flex;
  width: 100%;
  justify-content: center;
  align-items: center;
  color: #8c9399;
  font-size: 16px;
  cursor: pointer;
}

.th-label-with-switch {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.th-switch {
  position: relative;
  display: inline-block;
  width: 30px;
  height: 16px;
  background: #e6eaef;
  border-radius: 16px;
  vertical-align: middle;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.th-switch.is-on {
  background: #d4d8de;
}

.th-switch-dot {
  position: absolute;
  top: 2px;
  left: 2px;
  width: 12px;
  height: 12px;
  background: #f7f8fa;
  border: 1px solid #dfe3e8;
  border-radius: 50%;
  transition: left 0.2s ease;
}

.th-switch.is-on .th-switch-dot {
  left: 16px;
}

/* 轻主题提示气泡样式，贴近截图的白底+黑边 */
:deep(.th-tooltip.is-light) {
  background-color: #fff;
  color: #606266;
  border: 1px solid #dfe3e8;
  box-shadow: 0 2px 8px rgba(31, 45, 61, 0.06);
  font-size: 12px;
  padding: 6px 10px;
}

:deep(.th-tooltip.is-light .el-popper__arrow::before) {
  background-color: #fff;
  border: 1px solid #dfe3e8;
}

/* 操作图标样式 */
.op-icon {
  cursor: pointer;
  font-size: 16px;
}

.op-icon--doc {
  color: #8c9399;
}

.op-icon--delete {
  color: #333;
}

/* 启用边框时的外框样式 */
:deep(.el-table--border) {
  border: 1px solid var(--table-border-color);
}

/* 顶部齿轮与底部统计样式 */
.header-gear { display:inline-flex; align-items:center; color:#8c9399; font-size:18px; margin-right:8px; cursor:pointer; }
.footer-stat { color:#666; font-size:12px; }
</style>