/**
 * 其它支出单相关类型定义
 */

import type { PageDTO, PageQuery } from '@/apis/type'

/**
 * 其它支出单主表数据
 */
export interface OtherExpenseData {
  id?: string
  account?: string
  actual?: number
  data?: string
  examine?: number
  file?: string
  frame?: string
  money?: number
  more?: string
  nucleus?: number // 添加核销状态字段
  number?: string
  people?: string
  supplier?: string
  time?: string
  total?: number
  user?: string
  writeOffAmount?: number
}

/**
 * 其它支出单明细数据
 */
export interface OtherExpenseInfoData {
  id?: string
  data?: string
  iet?: string
  money?: number
  pid?: string
  source?: string
}

/**
 * 新增/修改支出单封装视图DTO
 */
export interface OtherExpenseDTO {
  otherExpense: OtherExpenseData
  otherExpenseInfoList: OtherExpenseInfoData[]
}

/**
 * 其它支出单查询参数
 */
export interface OtherExpenseQuery extends PageQuery {
  account?: string
  data?: string
  endDate?: string
  examine?: number
  nucleus?: number
  number?: string
  people?: string
  startDate?: string
  supplier?: string
  user?: string
}

/**
 * 其它支出单列表响应数据
 */
export interface OtherExpenseListData extends OtherExpenseData {
  writeOffAmount?: number
  id: string // 确保id是必需的
}

/**
 * 其它支出单详细信息
 */
export interface OtherExpenseDetail extends OtherExpenseData {
  info?: OtherExpenseInfoData[]
  writeOffAmount?: number
  nucleus?: number // 确保核销状态字段存在
}

/**
 * 导入数据返回对象
 */
export interface OtherExpenseImportResult {
  failCount: number
  failDetails: string[]
  successCount: number
  totalCount: number
}

/**
 * 审核参数
 */
export interface ExamineParams {
  ids: string[]
  approve: boolean
}
