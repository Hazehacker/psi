/**
 * 操作日志 API 类型定义
 * @file src/apis/advancedSettings/OperationLog/type.ts
 * @description 操作日志模块的 API 类型定义，基于 OpenAPI 规范
 */

/**
 * 操作日志数据传输对象
 */
export interface LogOperationDTO {
  /** 操作用户 */
  user: string
  /** 操作内容 */
  info: string
  /** 编号 */
  id?: string
  /** 操作时间 */
  time?: string
}

/**
 * 操作日志分页数据
 */
export interface LogOperationPageDTO {
  /** 页码 */
  pageIndex: number
  /** 每页大小 */
  pageSize: number
  /** 总条数 */
  total: number
  /** 总页数 */
  pages: number
  /** 数据列表 */
  rows?: LogOperationDTO[]
}

/**
 * 操作日志分页响应
 */
export interface LogOperationPageJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: LogOperationPageDTO
}

/**
 * 添加日志请求
 */
export interface LogOperationAddDTO {
  /** 操作用户 */
  user: string
  /** 操作内容 */
  info: string
}

/**
 * 操作日志响应
 */
export interface LogOperationResponseDTO {
  /** 操作时间 */
  operationTime: string
}

/**
 * 操作日志响应
 */
export interface LogOperationResponseJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: LogOperationResponseDTO
}

/**
 * 操作日志结果
 */
export interface LogOperationResultDTO {
  /** 操作时间 */
  operationTime: string
  /** 删除条数 */
  clearedCount: string
}

/**
 * 操作日志结果响应
 */
export interface LogOperationResultJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: LogOperationResultDTO
}
