<template>
  <section class="table-container" :style="containerStyle">
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
    <el-table
      ref="tableRef"
      class="table-content"
      border
      :data="tabdata?.rows || []"
      :="reltabattr"
      @row-dblclick="handleRowDblclick"
      @selection-change="handleSelectionChange"
    >
      <!-- 多选列 -->
      <el-table-column v-if="istabmultiple" type="selection" width="55" />
      <!-- 自定义展开行 -->
      <el-table-column v-if="istabexpand" fixed type="expand" width="30">
        <template #default="scope">
          <!-- 声明一个具名插槽，用于支持外部自定义扩展行显示内容 -->
          <slot name="customerexpand" :row="scope.row"></slot>
        </template>
      </el-table-column>
      <!-- 序号列 -->
      <el-table-column
        v-if="istabseq"
        fixed
        type="index"
        label="序号"
        width="60"
        align="center"
        :index="index"
      />

      <!-- 支持多级表头的列定义 -->
      <template v-if="hasCustomColumns">
        <!-- 使用插槽方式定义多级表头 -->
        <slot name="columns"></slot>
      </template>
      <template v-else>
        <!-- 原有的单级表头支持 -->
        <el-table-column
          v-for="(column, index) in tabdatacolumns"
          :key="column.prop || index"
          :="getColumnProps(column)"
        >
          <template #default="scope">
            <!-- 声明一个具名插槽，用于支持外部自定义单元格显示内容 -->
            <slot
              name="customercell"
              :column="column"
              :prop="column.prop"
              :index="scope.$index"
              :row="scope.row"
            >
              <!-- 数据列 -->
              <template v-if="column.prop !== 'operate'">
                <!-- 如果有 formatter 函数，使用格式化后的值 -->
                <template v-if="column.formatter && typeof column.formatter === 'function'">
                  {{ column.formatter(scope.row) }}
                </template>
                <!-- 否则显示原始值 -->
                <template v-else>
                  {{ scope.row?.[column.prop] ?? '' }}
                </template>
              </template>
              <!-- 操作列 -->
              <template v-else>
                <el-button
                  v-for="(btn, index) in taboperbtns"
                  :key="btn.text || index"
                  :size="btn.attr?.size || tabopercolumnattr?.size"
                  :="btn.attr"
                  @click="emit('taboper-click', scope.$index, scope.row, btn.evtname)"
                >
                  {{ btn.text }}
                </el-button>
              </template>
            </slot>
          </template>
        </el-table-column>
      </template>

      <!-- 空状态 -->
      <template #empty>
        <div class="table-empty">
          <div>{{ emptyText }}</div>
        </div>
      </template>
    </el-table>

    <!-- 表格底部 -->
    <section class="table-footer">
      <!-- 分页组件 -->
      <el-pagination
        v-if="istabpage"
        class="tablePagination"
        size="small"
        layout="prev, pager, next, jumper, sizes, total"
        :page-sizes="[20, 30, 60, 90, 150, 300]"
        :total="tabdata?.total || 0"
        :current-page="tabdata?.pageIndex || 1"
        :page-size="tabdata?.pageSize || 10"
        :pager-count="pagerCount || 7"
        @size-change="handlePageSizeChange"
        @current-change="handleCurrentPageChange"
      />
      <!-- 状态栏插槽，放在分页组件右侧 -->
      <section class="status-slot">
        <slot name="status" :table="tableRef" :props="props"></slot>
      </section>
      <!-- 底部插槽，用于支持扩展底部操作栏或表格底部标注 -->
      <section class="footer-slot">
        <slot name="footer" :table="tableRef" :props="props"></slot>
      </section>
    </section>
  </section>
</template>

<script setup lang="ts" generic="T extends Object">
import { type PropType, computed, ref, useSlots } from 'vue'
import type { TableInstance } from 'element-plus'
import {
  type MyTableAttr,
  type MyTableColumn,
  type MyTableOperationsColumn,
  type MyTableOperationsBtn,
  type PageDTO,
  createMyTableOperationsColumn,
  createPageDTO
} from './type'

// el-table的引用
const tableRef = ref<TableInstance>()

// 获取插槽
const slots = useSlots()

// 检查是否有自定义列插槽
const hasCustomColumns = computed(() => {
  return !!slots.columns
})

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
  /** 表操作列按钮数据 */
  taboperbtns: {
    type: Array as PropType<MyTableOperationsBtn[]>,
    default: () => []
  },
  /** 空内容文本 */
  emptyText: {
    type: String,
    default: '暂无数据'
  },
  /** 页码数量 */
  pagerCount: {
    type: Number,
    default: 7
  },
  /** 表格容器高度，支持字符串和数字类型 */
  height: {
    type: [String, Number],
    default: 'calc(100vh - 205px)'
  },
  /** 最小高度 */
  minHeight: {
    type: [String, Number],
    default: undefined
  },
  /** 最大高度 */
  maxHeight: {
    type: [String, Number],
    default: undefined
  }
})

/**
 * 获取列属性，排除 formatter 等不支持直接传递的属性
 */
const getColumnProps = (column: MyTableColumn) => {
  const { formatter, ...columnProps } = column
  return columnProps
}

/**
 * 计算容器样式
 */
const containerStyle = computed(() => {
  const style: Record<string, string> = {
    height: typeof props.height === 'number' ? `${props.height}px` : props.height
  }

  if (props.minHeight) {
    style.minHeight = typeof props.minHeight === 'number' ? `${props.minHeight}px` : props.minHeight
  }

  if (props.maxHeight) {
    style.maxHeight = typeof props.maxHeight === 'number' ? `${props.maxHeight}px` : props.maxHeight
  }

  return style
})

/**
 * 合并表格默认属性与传入属性
 */
const reltabattr = computed<MyTableAttr>(() => {
  // 默认属性
  const defaultAttrs: MyTableAttr = {
    psize: 'default',
    border: true,
    'header-cell-style': { textAlign: 'center' },
    'cell-style': { textAlign: 'center' }
  }
  return {
    ...defaultAttrs,
    ...props.tabattr
  }
})

/**
 * 合并操作列默认属性与传入属性
 */
const tabopercolumnattr = computed<MyTableOperationsColumn>(() => {
  const defaultCol = createMyTableOperationsColumn()
  if (props.tabdatacolumns) {
    const col = props.tabdatacolumns.find((column) => column.prop === 'operate')
    if (col) {
      return { ...defaultCol, ...col }
    }
  }
  return defaultCol
})

// 序号计算
function index(val: number) {
  const pageIndex = props.tabdata?.pageIndex || 1
  const pageSize = props.tabdata?.pageSize || 10
  return (pageIndex - 1) * pageSize + val + 1
}

/**
 * 声明emit事件
 */
const emit = defineEmits<{
  /** 分页事件 */
  (event: 'page-change', data: PageDTO<T>): void
  /** 选中行事件 */
  (event: 'selection-change', data: Array<T>): void
  /** 行双击事件 */
  (event: 'row-dbclick', data: T): void
  /** 操作栏点击事件 */
  (event: 'taboper-click', index: number, row: T, evtname: string): void
  /** 更新表格数据事件 */
  (event: 'update:tabdata', data: PageDTO<T>): void
}>()

/**
 * 多选框选中行发生改变时候触发
 * @param rows 当前选择的行
 */
function handleSelectionChange(rows: T[]) {
  emit('selection-change', rows)
}

/**
 * 双击行触发事件
 * @param row 当前双击的行
 */
function handleRowDblclick(row: T) {
  emit('row-dbclick', row)
}

/**
 * 每页数据条数发生改变时候触发
 * @param val 每页数据条数
 */
const handlePageSizeChange = (val: number) => {
  const currentData = props.tabdata || createPageDTO()
  emit('update:tabdata', {
    ...currentData,
    pageSize: val,
    pageIndex: 1
  })
}

/**
 * 页码发生改变时候触发
 * @param val 当前页码
 */
const handleCurrentPageChange = (val: number) => {
  const currentData = props.tabdata || createPageDTO()
  emit('update:tabdata', {
    ...currentData,
    pageIndex: val
  })
}

// 暴露方法给父组件
defineExpose({
  tableData: () => props.tabdata?.rows || [],
  tableRef,
  // 树形表格相关方法
  toggleRowExpansion: (row: any, expanded?: boolean) => {
    tableRef.value?.toggleRowExpansion(row, expanded)
  },
  setCurrentRow: (row: any) => {
    tableRef.value?.setCurrentRow(row)
  },
  clearSelection: () => {
    tableRef.value?.clearSelection()
  },
  getSelectionRows: () => {
    return tableRef.value?.getSelectionRows() || []
  }
})
</script>

<style lang="scss" scoped>
.table-container {
  width: 100%;
  background-color: #ffffff;
  box-sizing: border-box;
  /* 确保表格容器占满剩余空间 */
  flex: 1;
  display: flex;
  flex-direction: column;
}
/* 确保el-table占满容器 */
:deep(.el-table) {
  flex: 1;
}
/* 如果表格数据很少时，确保表格仍然占满高度 */
:deep(.el-table__body-wrapper) {
  flex: 1;
}
.table-header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  .header-slot {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    align-items: center;
  }
}
:deep(.el-scrollbar__view) {
  height: 100% !important;
}
.table-footer {
  padding: 0 0.5%;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: flex-start;
  border: 1px solid #ebeef5;

  .footer-slot {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    gap: 10px;
  }
}
/* 子节点背景色 */
:deep(.el-table .el-table__row--level-1) {
  background: #fcfcfc;
}
</style>
