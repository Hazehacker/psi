/**
 * 搜索表单配置项类型定义
 */
export interface SearchFormData {
  /** 商品名称 */
  goods?: string
  /** 单据编号 */
  number?: string
  /** 供应商ID */
  supplier?: string | null
  /** 客户ID */
  customer?: string | null
  /** 仓库ID */
  warehouse?: string | null
  /** 资金账户ID */
  account?: string | null
  /** 关联人员ID */
  people?: string | null
  /** 制单人ID */
  user?: string | null
  /** 单据开始日期 */
  startTime?: string
  /** 单据结束日期 */
  endTime?: string
  /** 到货开始日期 */
  startArrival?: string
  /** 到货结束日期 */
  endArrival?: string
  /** 审核状态 */
  examine?: number | string
  /** 入库状态 */
  state?: number | string
  /** 备注信息 */
  data?: string
  /** 单据类型 */
  orderType?: string
  /** 显示明细 */
  showDetails?: string
  /** 其他自定义字段 */
  [key: string]: any
}

/**
 * 搜索组件配置项
 */
export interface GoodSearchConfig {
  /** 是否显示商品名称输入框 */
  showGoods?: boolean
  /** 是否显示单据编号输入框 */
  showNumber?: boolean
  /** 是否显示供应商选择 */
  showSupplier?: boolean
  /** 是否显示客户选择 */
  showCustomer?: boolean
  /** 是否显示仓库选择 */
  showWarehouse?: boolean
  /** 是否显示资金账户选择 */
  showAccount?: boolean
  /** 是否显示关联人员选择 */
  showPeople?: boolean
  /** 是否显示单据日期范围 */
  showBillDate?: boolean
  /** 是否显示到货日期范围 */
  showArrivalDate?: boolean
  /** 是否显示制单人选择 */
  showUser?: boolean
  /** 制单人字段的占位符文本 */
  userPlaceholder?: string
  /** 是否显示审核状态选择 */
  showExamine?: boolean
  /** 是否显示入库状态选择 */
  showState?: boolean
  /** 是否显示备注输入框 */
  showRemark?: boolean
  /** 自定义字段配置 */
  customFields?: CustomField[]
  /** 是否使用内联模式（不显示popover触发器） */
  inline?: boolean
  /** 搜索按钮配置 */
  searchButton?: SearchButtonConfig
  /** 默认日期范围（天数） */
  defaultDays?: number
}

/**
 * 自定义字段配置
 */
export interface CustomField {
  /** 字段key */
  key: string
  /** 字段类型 */
  type: 'input' | 'select' | 'date' | 'nodList' | 'treeSelect'
  /** 字段标签/占位符 */
  label: string
  /** 占位符文本 */
  placeholder?: string
  /** 是否可清空 */
  clearable?: boolean
  /** 下拉选项（type为select时使用） */
  options?: Array<{ label: string; value: any }>
  /** 是否多选（type为select或treeSelect时使用） */
  multiple?: boolean
  /** 是否折叠标签（多选时使用） */
  collapseTags?: boolean
  /** NodList配置（type为nodList时使用） */
  nodListConfig?: {
    action: string
    scene: string
  }
  /** 日期格式（type为date时使用） */
  valueFormat?: string
  /** 日期类型（type为date时使用） */
  dateType?: 'date' | 'daterange' | 'datetime' | 'datetimerange'
  /** 树形数据（type为treeSelect时使用） */
  treeData?: TreeData[]
  /** 是否严格选择（type为treeSelect时使用） */
  checkStrictly?: boolean
  /** 是否在展开后渲染（type为treeSelect时使用） */
  renderAfterExpand?: boolean
}

/**
 * 搜索按钮配置
 */
export interface SearchButtonConfig {
  /** 按钮图标 */
  icon?: string
  /** 按钮文本 */
  text?: string
  /** 按钮类名 */
  class?: string
}

/**
 * 树形数据结构
 */
export interface TreeData {
  value: any
  label: string
  children?: TreeData[]
  disabled?: boolean
}
