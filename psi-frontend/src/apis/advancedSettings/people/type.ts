/**
 * 人员管理 API 类型定义
 * @file src/apis/advancedSettings/people/type.ts
 * @description 人员管理模块的 API 类型定义，基于 OpenAPI 规范
 */

/**
 * 人员列表项
 */
export interface PersonnelListDTO {
  /** ID */
  id?: string
  /** 所属组织 */
  frame?: string
  /** 人员名称 */
  name?: string
  /** 人员编号 */
  number?: string
  /** 人员性别 */
  sex?: number
  /** 联系电话 */
  tel?: string
  /** 联系地址 */
  add?: string
  /** 身份证号 */
  card?: string
  /** 备注信息 */
  data?: string
}

/**
 * 人员列表分页数据
 */
export interface PersonnelListPageDTO {
  /** 页码 */
  pageIndex: number
  /** 每页大小 */
  pageSize: number
  /** 总条数 */
  total: number
  /** 总页数 */
  pages: number
  /** 数据列表 */
  rows?: PersonnelListDTO[]
}

/**
 * 人员列表分页响应
 */
export interface PersonnelListPageJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: PersonnelListPageDTO
}

/**
 * 人员详情
 */
export interface PersonnelDetailDTO {
  /** ID */
  id?: string
  /** 所属组织 */
  frame?: string
  /** 人员名称 */
  name?: string
  /** 人员编号 */
  number?: string
  /** 人员性别 */
  sex?: number
  /** 联系电话 */
  tel?: string
  /** 联系地址 */
  add?: string
  /** 身份证号 */
  card?: string
  /** 备注信息 */
  data?: string
}

/**
 * 人员详情响应
 */
export interface PersonnelDetailJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: PersonnelDetailDTO
}

/**
 * 人员响应
 */
export interface PersonnelJsonVO {
  /** 状态码 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: string
}

/**
 * 添加人员请求
 */
export interface PersonnelAddDTO {
  /** 人员名称 */
  name?: string
  /** 人员编号 */
  number?: string
  /** 所属组织 */
  frame?: string
  /** 人员性别 */
  sex?: string
  /** 联系电话 */
  tel?: string
  /** 联系地址 */
  add?: string
  /** 身份证号 */
  card?: string
  /** 备注信息 */
  data?: string
}

/**
 * 编辑人员请求
 */
export interface PersonnelEditDTO {
  /** 人员编号 */
  id?: string
  /** 人员名称 */
  name?: string
  /** 人员编号 */
  number?: string
  /** 所属组织 */
  frame?: string
  /** 人员性别 */
  sex?: string
  /** 联系电话 */
  tel?: string
  /** 联系地址 */
  add?: string
  /** 身份证号 */
  card?: string
  /** 备注信息 */
  data?: string
}

/**
 * 删除人员详情
 */
export interface PeopleDeleteDetailDTO {
  /** 删除人员id */
  personId?: string
  /** 删除是否成功 */
  success?: boolean
  /** 删除信息 */
  message?: string
}

/**
 * 人员删除报告
 */
export interface PeopleDeleteReportDTO {
  /** 删除条数 */
  total?: number
  /** 删除成功条数 */
  succnt?: number
  /** 删除失败条数 */
  failed?: number
  /** 具体删除信息 */
  details?: PeopleDeleteDetailDTO[]
}

/**
 * 人员删除响应
 */
export interface PersonnelDeleteJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: PeopleDeleteReportDTO
}

/**
 * 人员导入错误
 */
export interface PeopleImportErrorDTO {
  /** 错误行 */
  row?: number
  /** 错误参数 */
  field?: string
  /** 错误值 */
  value?: string
  /** 错误信息 */
  message?: string
}

/**
 * 人员导入报告
 */
export interface PeopleImportReportDTO {
  /** 导入条数 */
  total?: number
  /** 成功条数 */
  success?: number
  /** 失败条数 */
  failed?: number
  /** 具体错误信息 */
  errors?: PeopleImportErrorDTO[]
}

/**
 * 人员导入响应
 */
export interface PersonnelImportJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: PeopleImportReportDTO
}

/**
 * 人员导出请求
 */
export interface PersonnelExportReqDTO {
  /** 导出人员的编号 */
  ids: string[]
}
