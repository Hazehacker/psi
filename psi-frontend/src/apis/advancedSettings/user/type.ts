/**
 * 用户管理 API 类型定义
 * @file src/apis/advancedSettings/user/type.ts
 * @description 用户管理模块的 API 类型定义，基于 OpenAPI 规范
 */

/**
 * 用户数据传输对象
 */
export interface UserDTO {
  id?: string
  /** 昵称 */
  name?: string
  /** 所属组织 */
  frame?: string
  /** 所属角色 */
  role?: string
  /** 登陆账号 */
  user?: string
  /** 手机号码 */
  tel?: string
  /** 备注信息 */
  data?: string
}

/**
 * 用户分页数据
 */
export interface UserPageDTO {
  /** 页码 */
  pageIndex: number
  /** 每页大小 */
  pageSize: number
  /** 总条数 */
  total: number
  /** 总页数 */
  pages: number
  /** 数据列表 */
  rows?: UserDTO[]
}

/**
 * 用户分页响应
 */
export interface UserPageJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: UserPageDTO
}

/**
 * 用户详情
 */
export interface UserDetailDTO {
  /** 昵称 */
  name?: string
  /** 所属组织 */
  frame?: string
  /** 所属角色 */
  role?: string
  /** 登陆账号 */
  user?: string
  /** 手机号码 */
  tel?: string
  /** 备注信息 */
  data?: string
  /** 密码 */
  pwd?: string
  /** 头像 */
  img?: string
  /** 头像地址 */
  imgUrl?: string
}

/**
 * 用户详情响应
 */
export interface UserDetailJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: UserDetailDTO
}

/**
 * 用户响应
 */
export interface UserJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: string
}

/**
 * 添加用户请求
 */
export interface UserAddDTO {
  /** 昵称 */
  name: string
  /** 所属组织 */
  frame: string
  /** 所属角色 */
  role: string
  /** 登陆账号 */
  user: string
  /** 手机号码 */
  tel: string
  /** 密码 */
  pwd?: string
  /** 头像 */
  img?: string
  /** 备注信息 */
  data?: string
  /** 拼音信息 */
  py?: string
  /** 更多信息 */
  more?: string
}

/**
 * 修改用户请求
 */
export interface UserModDTO {
  /** 用户编号 */
  id: string
  /** 昵称 */
  name?: string
  /** 所属组织 */
  frame?: string
  /** 所属角色 */
  role?: string
  /** 登陆账号 */
  user?: string
  /** 手机号码 */
  tel?: string
  /** 密码 */
  pwd?: string
  /** 头像 */
  img?: string
  /** 备注信息 */
  data?: string
  /** 拼音信息 */
  py?: string
  /** 更多信息 */
  more?: string
}

