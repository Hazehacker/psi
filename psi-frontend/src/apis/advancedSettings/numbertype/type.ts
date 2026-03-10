/**
 * 单据编号 API 类型定义
 * @file src/apis/advancedSettings/numbertype/type.ts
 * @description 单据编号模块的 API 类型定义，基于 OpenAPI 规范
 */

/**
 * 单据编号数据传输对象
 */
export interface ReceiptgetDTO {
  /** 单据类型 */
  numberType?: string
  /** 编号 */
  number?: number
  /** 创建时间 */
  create_time?: string
}

/**
 * 单据编号分页数据
 */
export interface ReceiptPageDTO {
  /** 页码 */
  pageIndex: number
  /** 每页大小 */
  pageSize: number
  /** 总条数 */
  total: number
  /** 总页数 */
  pages: number
  /** 数据列表 */
  rows?: ReceiptgetDTO[]
}

/**
 * 单据编号分页响应
 */
export interface ReceiptPageJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: ReceiptPageDTO
}

/**
 * 单据编号响应
 */
export interface ReceiptgetJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: ReceiptgetDTO
}

/**
 * 添加单据编号请求
 */
export interface ReceiptAddNumberDTO {
  /** 单据类型 */
  NumberType: string
}

/**
 * 删除单据编号请求
 */
export interface ReceiptRemoveNumberDTO {
  /** 单据类型 */
  numberType: string
  /** 编号 */
  number: number
  /** 创建时间 */
  create_time: string
}
