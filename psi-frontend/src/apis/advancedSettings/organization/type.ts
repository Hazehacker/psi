/**
 * 组织结构 API 类型定义
 * @file src/apis/advancedSettings/organization/type.ts
 * @description 组织结构模块的 API 类型定义，基于 OpenAPI 规范
 */

/**
 * 组织列表项
 */
export interface OrganizationListDTO {
  /** ID */
  id: string
  /** 组织名称 */
  name: string
  /** 组织排序 */
  sort: number
  /** 备注信息 */
  remark?: string
  /** 组织结构 */
  children?: OrganizationListDTO[]
}

/**
 * 组织列表响应
 */
export interface OrganizationListJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: OrganizationListDTO[]
}

/**
 * 组织树节点
 */
export interface OrganizationTreeDTO {
  /** ID */
  id?: string
  /** 组织名称 */
  name?: string
  /** 组织结构 */
  children?: OrganizationTreeDTO[]
}

/**
 * 组织树响应
 */
export interface OrganizationTreeJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: OrganizationTreeDTO[]
}

/**
 * 组织详情
 */
export interface OrganizationDetailDTO {
  /** ID */
  id: string
  /** 组织名称 */
  name: string
  /** 所属ID */
  pid: string
  /** 组织排序 */
  sort: number
  /** 备注信息 */
  remark?: string
  /** 组织结构 */
  children?: OrganizationTreeDTO[]
}

/**
 * 组织详情响应
 */
export interface OrganizationDetailJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: OrganizationDetailDTO
}

/**
 * 添加组织请求
 */
export interface OrganizationAddDTO {
  /** 组织名称 */
  name: string
  /** 所属ID */
  pid: string
  /** 组织排序 */
  sort: number
  /** 备注信息 */
  remark?: string
}

/**
 * 修改组织请求
 */
export interface OrganizationUpdateDTO {
  /** 组织名称 */
  name: string
  /** 所属ID */
  pid: string
  /** 组织排序 */
  sort: number
  /** 备注信息 */
  remark?: string
}

/**
 * 布尔值响应
 */
export interface BooleanJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: boolean
}

