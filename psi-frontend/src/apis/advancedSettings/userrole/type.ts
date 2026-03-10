/**
 * 用户角色 API 类型定义
 * @file src/apis/advancedSettings/userrole/type.ts
 * @description 用户角色模块的 API 类型定义，基于 OpenAPI 规范
 */

/**
 * 角色列表项
 */
export interface RoleListItemDTO {
  /** ID */
  id?: string
  /** 角色名称 */
  name?: string
  /** 角色备注 */
  data?: string
}

/**
 * 角色列表分页数据
 */
export interface RoleListPageDTO {
  /** 页码 */
  pageIndex: number
  /** 每页大小 */
  pageSize: number
  /** 总条数 */
  total: number
  /** 总页数 */
  pages: number
  /** 数据列表 */
  rows?: RoleListItemDTO[]
}

/**
 * 角色列表分页响应
 */
export interface RoleListPageJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: RoleListPageDTO
}

/**
 * 角色详情
 */
export interface RoleDetailDTO {
  /** ID */
  id?: string
  /** 角色名称 */
  name: string
  /** 角色备注 */
  data?: string
  /** 角色功能权限 */
  root?: string
  /** 角色数据权限 */
  auth?: string
}

/**
 * 角色详情响应
 */
export interface RoleDetailJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: RoleDetailDTO
}

/**
 * 角色名称列表节点
 */
export interface RoleNameListNodeDTO {
  /** 角色唯一标识 */
  id?: string
  /** 角色名称 */
  name?: string
}

/**
 * 角色名称列表
 */
export interface RoleNameListDTO {
  /** 角色名称列表 */
  roleNameList?: RoleNameListNodeDTO[]
}

/**
 * 角色名称列表响应
 */
export interface RoleNameListJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: RoleNameListDTO
}

/**
 * 权限树子模块节点
 */
export interface PermissionTreeSubModuleNodeDTO {
  /** 子模块ID */
  id?: string
  /** 子模块名称 */
  name?: string
  /** 权限项列表 */
  children?: PermissionTreeItemsNodeDTO[]
}

/**
 * 权限树主模块节点
 */
export interface PermissionTreeMainModuleNodeDTO {
  /** 主模块ID */
  id?: string
  /** 主模块名称 */
  name?: string
  /** 子模块 */
  children?: PermissionTreeSubModuleNodeDTO[]
}

/**
 * 权限项节点
 */
export interface PermissionTreeItemsNodeDTO {
  /** 权限项ID */
  id?: string
  /** 权限项名称 */
  name?: string
  /** 是否禁用权限 */
  is_enable?: string
}

/**
 * 角色功能权限
 */
export interface RoleFunctionPermissionseDTO {
  /** 功能权限列表 */
  functionPermissions?: PermissionTreeMainModuleNodeDTO[]
}

/**
 * 角色功能权限响应
 */
export interface RoleFunctionPermissionsJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: RoleFunctionPermissionseDTO
}

/**
 * 添加角色请求
 */
export interface RoleAddDTO {
  /** 角色名称 */
  name: string
  /** 角色备注 */
  data?: string
  /** 角色功能权限 */
  root?: string
  /** 角色数据权限 */
  auth?: string
}

/**
 * 修改角色请求
 */
export interface RoleListDTO {
  /** ID */
  id?: string
  /** 角色名称 */
  name: string
  /** 角色备注 */
  data?: string
  /** 角色功能权限 */
  root?: string
  /** 角色数据权限 */
  auth?: string
}

