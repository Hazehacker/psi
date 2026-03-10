/**
 * 授权登录响应数据对象
 */
export interface Oauth2TokenDTO {
  /** 访问令牌 */
  token: string
  /** 刷新令牌 */
  refreshToken: string
  /** 访问令牌头前缀 */
  tokenHead: string
}

/**
 * 用户登录传输数据
 */
export interface LoginDTO {
  /** 用户名 */
  username: string
  /** 密码 */
  password: string
  /** 验证码 */
  code?: string
}

/**
 * 用户信息类型定义
 */
export interface UserInfo {
  /** 唯一id */
  id: string
  /** 用户名 */
  username: string
  /** 头像 */
  avatar?: string
  /** 角色 */
  roles?: Array<string>
}

/**
 * 后端返回的菜单树节点类型（MenuTreeVO）
 */
export interface MenuTreeVO {
  /** 菜单ID */
  id: string
  /** 菜单名称 */
  name: string
  /** 菜单路径 */
  href?: string
  /** 是否有报表功能（字符串 "true"/"false" 或布尔值） */
  hasReport?: string | boolean
  /** 报表页面路径（如果提供，将优先使用此路径而不是拼接 -report） */
  reportHref?: string
  /** 子菜单节点 */
  children?: Array<MenuTreeVO>
  /** 功能节点 */
  fun?: Array<any>
  /** 其他可能的字段 */
  [key: string]: any
}

/**
 * 菜单类型定义（前端使用）
 */
export interface Menu {
  /** 菜单唯一标识 */
  id: string
  /** 菜单名称 */
  text: string

  /**
   * 菜单图标
   * @description
   * 一律使用 iconify 的图标标签，格式为 `图标集:图标名` ，**必须满足** 该格式。
   * 如果没有提供图标，请使用空字符串。
   */
  icon: string
  /** 菜单路径 */
  href?: string
  /** 菜单父节点 */
  pid?: string
  /** 子菜单 */
  children?: Array<Menu>
  /** 是否隐藏报表按钮 */
  hideReport?: boolean
  /** 是否有报表功能 */
  hasReport?: boolean
  /** 报表页面路径（如果提供，将优先使用此路径） */
  reportHref?: string
}
