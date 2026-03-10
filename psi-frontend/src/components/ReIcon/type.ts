/**
 * ReIcon 图标组件类型定义
 * 重新导出 src/types.ts 中的类型，并提供额外的辅助类型和工厂函数
 */

/** 重新导出核心类型 */
export type { iconType } from './src/types'

/**
 * 图标属性接口
 * 基于 Iconify 的属性定义
 * 参考：https://docs.iconify.design/icon-components/vue/#properties
 */
export interface IconProps {
  /** 图标名称或图标组件 */
  icon: string | object
  /** 是否为内联图标 */
  inline?: boolean
  /** 图标宽度 */
  width?: string | number
  /** 图标高度 */
  height?: string | number
  /** 水平翻转 */
  horizontalFlip?: boolean
  /** 垂直翻转 */
  verticalFlip?: boolean
  /** 翻转方向 */
  flip?: string
  /** 旋转角度 */
  rotate?: number | string
  /** 图标颜色 */
  color?: string
  /** 水平对齐 */
  horizontalAlign?: boolean
  /** 垂直对齐 */
  verticalAlign?: boolean
  /** 对齐方式 */
  align?: string
  /** 加载完成回调 */
  onLoad?: Function
  /** 包含函数 */
  includes?: Function
  /** 自定义样式 */
  style?: object
}

/**
 * 离线图标组件属性接口
 */
export interface IconifyIconOfflineProps extends IconProps {
  /** 图标名称（字符串格式：ep/menu）或导入的图标组件 */
  icon: string | object
}

/**
 * 在线图标组件属性接口
 */
export interface IconifyIconOnlineProps {
  /** 图标名称（字符串格式：ep:home） */
  icon: string
  /** 是否为内联图标 */
  inline?: boolean
  /** 图标宽度 */
  width?: string | number
  /** 图标高度 */
  height?: string | number
  /** 水平翻转 */
  horizontalFlip?: boolean
  /** 垂直翻转 */
  verticalFlip?: boolean
  /** 翻转方向 */
  flip?: string
  /** 旋转角度 */
  rotate?: number | string
  /** 图标颜色 */
  color?: string
  /** 水平对齐 */
  horizontalAlign?: boolean
  /** 垂直对齐 */
  verticalAlign?: boolean
  /** 对齐方式 */
  align?: string
  /** 加载完成回调 */
  onLoad?: Function
  /** 包含函数 */
  includes?: Function
  /** 自定义样式 */
  style?: object
}

/**
 * FontIcon 图标组件属性接口
 */
export interface FontIconProps {
  /** 图标名称 */
  icon: string
  /** 图标类型（uni: unicode 模式，svg: symbol 模式） */
  iconType?: 'uni' | 'svg' | ''
  /** 是否使用 unicode 模式 */
  uni?: boolean
  /** 是否使用 svg symbol 模式 */
  svg?: boolean
  /** 自定义样式 */
  style?: object
}

/**
 * useRenderIcon Hook 返回的组件类型
 */
export interface RenderIconComponent {
  /** 组件名称 */
  name: string
  /** 渲染函数 */
  render: () => any
}

/**
 * 图标尺寸预设
 */
export enum IconSize {
  /** 超小尺寸：12px */
  XS = 12,
  /** 小尺寸：16px */
  SM = 16,
  /** 中等尺寸：20px */
  MD = 20,
  /** 默认尺寸：24px */
  DEFAULT = 24,
  /** 大尺寸：32px */
  LG = 32,
  /** 超大尺寸：48px */
  XL = 48,
  /** 巨大尺寸：64px */
  XXL = 64
}

/**
 * 常用图标集枚举
 */
export enum IconSet {
  /** Element Plus 图标集 */
  EP = 'ep',
  /** Remix Icon 图标集 */
  RI = 'ri',
  /** Material Design Icons */
  MDI = 'mdi',
  /** Font Awesome */
  FA = 'fa',
  /** Ant Design Icons */
  ANT = 'ant-design',
  /** Bootstrap Icons */
  BI = 'bi',
  /** Carbon Icons */
  CARBON = 'carbon',
  /** Heroicons */
  HEROICONS = 'heroicons',
  /** Tabler Icons */
  TABLER = 'tabler'
}

/**
 * 图标使用场景枚举
 */
export enum IconUsage {
  /** 菜单图标 */
  MENU = 'menu',
  /** 按钮图标 */
  BUTTON = 'button',
  /** 表单图标 */
  FORM = 'form',
  /** 状态图标 */
  STATUS = 'status',
  /** 装饰图标 */
  DECORATION = 'decoration'
}
