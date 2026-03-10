import type { PageQuery } from '../type'

/**
 * 常用功能相关类型定义
 */

/**
 * 菜单列表数据对象
 */
export interface MenuItem {
  /** 菜单ID */
  id: string
  /** 父级菜单ID */
  pid: number
  /** 菜单名称 */
  name: string
  /** 菜单标识 */
  key: string
  /** 资源路径 */
  resource: string
  /** 图标 */
  ico: string
  /** 所属分组 */
  root: string
  /** 额外描述 */
  data: string
}

/**
 * 用户已设置的常用功能数据对象
 */
export interface UserConfiguredMenu {
  /** 用户ID */
  userId: string
  /** 用户标记的常用功能菜单列表 */
  menus: MenuItem[]
}

/**
 * 保存用户常用功能的请求数据对象
 */
export interface SaveOftenMenusRequest {
  /** 用户ID */
  userId: string
  /** 选中的菜单 key 列表 */
  menuKeys: string[]
}

/**
 * 保存用户常用功能的响应数据对象
 */
export interface SaveOftenMenusResponse {
  /** 操作是否成功 */
  success: boolean
  /** 提示信息 */
  message: string
}

/**
 * 分页查询参数
 */
export interface OftenMenuQuery extends PageQuery {
  /** 用户ID */
  userId: string
}
