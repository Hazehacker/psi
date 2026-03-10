export interface TableColumn {
  /** 列的属性名 */
  prop: string
  /** 列的标题 */
  label: string
  /** 列的宽度 */
  width?: string | number
  /** 列的最小宽度 */
  minWidth?: string | number
  /** 列是否固定 */
  fixed?: boolean | 'left' | 'right'
  /** 列是否可排序 */
  sortable?: boolean | 'custom'
  /** 列的对齐方式 */
  align?: 'left' | 'center' | 'right'
  /** 表头的对齐方式 */
  headerAlign?: 'left' | 'center' | 'right'
  /** 是否显示溢出提示 */
  showOverflowTooltip?: boolean
  /** 自定义插槽名称 */
  slot?: string
  /** 格式化函数 */
  formatter?: (row: any, column: TableColumn, cellValue: any, index: number) => string
  /** 列的类名 */
  className?: string
  /** 表头的类名 */
  headerClassName?: string
  /** 是否可选择的列 */
  selectable?: boolean
  /** 选择项列表 */
  options?: Array<{ label: string, value: any }>
}

export interface TableProps {
  /** 表格数据 */
  data?: any[]
  /** 列配置 */
  columns?: TableColumn[]
  /** 是否显示边框 */
  border?: boolean
  /** 是否显示斑马纹 */
  stripe?: boolean
  /** 表格尺寸 */
  size?: 'large' | 'default' | 'small'
  /** 表格高度 */
  height?: string | number
  /** 表格最大高度 */
  maxHeight?: string | number
  /** 是否显示表头 */
  showHeader?: boolean
  /** 是否高亮当前行 */
  highlightCurrentRow?: boolean
  /** 是否显示多选 */
  selection?: boolean
  /** 是否显示序号 */
  showIndex?: boolean
  /** 序号生成方法 */
  indexMethod?: (index: number) => number
  /** 操作列宽度 */
  operationWidth?: string | number
  /** 操作列是否固定 */
  operationFixed?: boolean | 'left' | 'right'
  /** 是否显示底部 */
  showFooter?: boolean
  /** 是否显示分页 */
  showPagination?: boolean
  /** 总数据量 */
  total?: number
  /** 当前页码 */
  currentPage?: number
  /** 每页显示条数 */
  pageSize?: number
  /** 每页显示条数选择器的选项 */
  pageSizes?: number[]
  /** 分页组件布局 */
  paginationLayout?: string
  /** 分页组件是否显示背景色 */
  paginationBackground?: boolean
  /** 行的类名（与Element Plus签名一致） */
  rowClassName?: string | ((data: { row: any; rowIndex: number }) => string)
  /** 单元格的类名（与Element Plus签名一致） */
  cellClassName?: string | ((data: { row: any; column: any; rowIndex: number; columnIndex: number }) => string)
  /** 表头单元格的类名（与Element Plus签名一致） */
  headerCellClassName?: string | ((data: { row: any; column: any; rowIndex: number; columnIndex: number }) => string)
}

export interface TableEmits {
  /** 多选变化事件 */
  'selection-change': (selection: any[]) => void
  /** 当前行变化事件 */
  'current-change': (currentRow: any, oldCurrentRow: any) => void
  /** 行点击事件 */
  'row-click': (row: any, column: TableColumn, event: Event) => void
  /** 行双击事件 */
  'row-dblclick': (row: any, column: TableColumn, event: Event) => void
  /** 排序变化事件 */
  'sort-change': (data: { column: TableColumn; prop: string; order: string }) => void
  /** 每页条数变化事件 */
  'size-change': (size: number) => void
  /** 页码变化事件 */
  'page-change': (page: number) => void
  /** 更新当前页码 */
  'update:currentPage': (page: number) => void
  /** 更新每页条数 */
  'update:pageSize': (size: number) => void
}

export interface TableSlots {
  /** 操作列插槽 */
  operation?: (props: { row: any; $index: number }) => any
  /** 底部左侧插槽 */
  'footer-left'?: () => any
  /** 底部右侧插槽 */
  'footer-right'?: () => any
  /** 动态列插槽 */
  [key: string]: ((props: { row: any; column: TableColumn; $index: number }) => any) | undefined
}