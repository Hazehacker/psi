/**
 * ReIcon 图标组件导出入口
 * 提供离线图标、在线图标、iconfont 图标组件及相关工具
 */

/** 导入离线图标预加载（副作用导入） */
import './src/offlineIcon'

/** 导入组件 */
import iconifyIconOffline from './src/iconifyIconOffline'
import iconifyIconOnline from './src/iconifyIconOnline'
import fontIcon from './src/iconfont'

/** 导出组件 */
export const IconifyIconOffline = iconifyIconOffline
export const IconifyIconOnline = iconifyIconOnline
export const FontIcon = fontIcon

/** 导出 Hook 函数 */
export * from './src/hooks'

/** 导出类型定义 */
export * from './src/types'
export * from './type'
