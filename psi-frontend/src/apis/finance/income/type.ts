/**
 * 其它收入单相关类型定义
 */

import type { PageDTO, PageQuery } from '@/apis/type'

/**
 * 新增其他收入单数据对象
 */
export interface AddOtherIncomeDTO {
  /** 结算账户 */
  account: string
  /** 实际金额 */
  actual?: number
  /** 客户 */
  customer?: string
  /** 备注信息 */
  data?: string
  /** 单据附件 */
  file?: string
  /** 所属组织 */
  frame?: string
  /** 收入单id(修改时使用) */
  id?: string
  /** 收入详情列表 */
  info: OtherIncomeDetailInfo[]
  /** 实付金额/单据收款 */
  money?: number
  /** 扩展信息 */
  more?: string
  /** 单据编号 */
  number?: string
  /** 关联人员 */
  people?: string
  /** 单据时间 */
  time?: string
  /** 单据金额 */
  total: number
  /** 制单人 */
  user?: string
}

/**
 * 其他收入单详情信息
 */
export interface OtherIncomeDetailInfo {
  /** 备注信息 */
  data?: string
  /** 收支类型 */
  iet: string
  /** 结算金额 */
  money: number
}

/**
 * 其他收入单数据对象
 */
export interface OtherIncomeItem {
  /** 结算账户 */
  account: string
  /** 应收金额 */
  actual: number
  /** 客户 */
  customer: string
  /** 备注信息 */
  data: string
  /** 审核状态 */
  examine: number
  /** 所属组织 */
  frame: string
  /** 收入单ID */
  id: string
  /** 实收金额 */
  money: number
  /** 核销状态 */
  nucleus: number
  /** 单据编号 */
  number: string
  /** 核销记录 */
  otherIncomeBillDTO?: OtherIncomeBillDTO
  /** 详细信息 */
  otherIncomeInfoDTO?: OtherIncomeInfoDTO
  /** 关联人员 */
  people: string
  /** 单据时间 */
  time: string
  /** 单据金额 */
  total: number
  /** 制单人 */
  user: string
  /** 核销金额 */
  writeOffAmount: number
  /** 核对状态 - 新增字段 */
  checkStatus?: string
}

/**
 * 其他收入单核销记录
 */
export interface OtherIncomeBillDTO {
  /** 核销金额 */
  money: number
  /** 所属单据 */
  pid: string
  /** 关联单据 */
  source: string
  /** 单据时间 */
  time: string
  /** 核销类型 */
  type: string
  /** 核销记录ID */
  uuid: string
}

/**
 * 其他收入单详情信息
 */
export interface OtherIncomeInfoDTO {
  /** 备注信息 */
  data: string
  /** 收支类型 */
  iet: string
  /** 收支类别 */
  ietData: IncomeExpenseType
  /** 结算金额 */
  money: number
  /** 对应收入单的ID */
  pid: string
  /** 收入单详情信息ID */
  uuid: string
}

/**
 * 收支类别
 */
export interface IncomeExpenseType {
  /** 备注信息 */
  data: string
  /** 收支类别名称 */
  name: string
  /** 类别排序 */
  sort: number
  /** 收支类型 */
  type: number
  /** 收支类型id */
  uuid: string
}

/**
 * 其他收入单详细信息
 */
export interface OtherIncomeDetail {
  /** 结算账户 */
  account: string
  /** 应收金额 */
  actual: number
  /** 收入核销列表 */
  bill: OtherIncomeBillDTO[]
  /** 客户 */
  customer: string
  /** 备注信息 */
  data: string
  /** 审核状态 */
  examine: number
  /** 单据附件 */
  file: string
  /** 所属组织 */
  frame: string
  /** 收入单ID */
  id: string
  /** 收入详情列表 */
  info: OtherIncomeInfoDTO[]
  /** 实收金额 */
  money: number
  /** 扩展信息 */
  more: string
  /** 核销状态 */
  nucleus: number
  /** 单据编号 */
  number: string
  /** 关联人员 */
  people: string
  /** 单据时间 */
  time: string
  /** 单据金额 */
  total: number
  /** 制单人 */
  user: string
}

/**
 * 其他收入单查询参数
 */
export interface OtherIncomeQueryParams extends PageQuery {
  /** 结算/资金账户 */
  account?: string
  /** 客户 */
  customer?: string
  /** 备注信息 */
  data?: string
  /** 查询截止时间 */
  endDate?: string
  /** 审核状态 */
  examine?: number
  /** 所属组织 */
  frame?: string
  /** ID */
  id?: string
  /** 核销状态 */
  nucleus?: number
  /** 单据编号 */
  number?: string
  /** 关联人员 */
  people?: string
  /** 查询起始时间 */
  startDate?: string
  /** 制单人 */
  user?: string
}

/**
 * 审核/反审核请求参数
 */
export interface ExamineRequest {
  /** 审核的id列表 */
  ids: string[]
  /** 审核状态：0-反审核，1-审核 */
  examine: number
}

/**
 * 审核/反审核响应数据
 */
export interface ExamineResponse {
  /** 审核状态 */
  examine: number
  /** id */
  id: string
}

/**
 * 分页响应数据
 */
export interface OtherIncomePageResult extends PageDTO<OtherIncomeItem> {}

/**
 * 通用响应类型
 */
export interface JsonVO<T = any> {
  /** 状态码 */
  code: number
  /** 数据对象 */
  data?: T
  /** 提示消息 */
  message: string
}
