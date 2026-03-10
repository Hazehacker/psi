/**
 * 结账管理 API 类型定义
 * @file src/apis/advancedSettings/period/type.ts
 * @description 结账管理模块的 API 类型定义，基于 OpenAPI 规范
 */

/**
 * 结账记录
 */
export interface PeriodRecordVO {
  /** 日期 */
  date?: string
  /** 时间 */
  time?: string
  /** 用户 */
  user?: string
}

/**
 * 结账记录分页响应
 */
export interface PeriodRecordPageRespVO {
  /** 状态 */
  state?: string
  /** 数量 */
  count?: number
  /** 信息列表 */
  info?: PeriodRecordVO[]
}

/**
 * 结账响应
 */
export interface PeriodJsonVO {
  /** 状态码，10000 表示成功 */
  code: number
  /** 提示信息 */
  message: string
  /** 数据对象 */
  data?: string
}

/**
 * 保存结账请求
 */
export interface SavePeriodDTO {
  /** 数据 */
  data: string
}
