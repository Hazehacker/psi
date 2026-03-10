/**
 * 销售单相关数据类型定义
 */

import type { PageDTO, PageQuery } from '@/apis/type'

/**
 * 销售单数据对象
 */
export interface SaleDTO {
  id?: string
  account?: string
  actual?: number
  check?: boolean
  cost?: number
  cse?: number
  customer?: string
  data?: string
  examine?: boolean
  file?: string
  frame?: string
  invoice?: number
  items?: SaleDetailItem[]
  logistics?: string
  money?: number
  more?: string
  nucleus?: number
  number?: string
  people?: string
  source?: string
  time?: string
  total?: number
  user?: string
}

/**
 * 销售单详情数据对象
 */
export interface SaleDetailItem {
  attr?: string
  batch?: string
  data?: string
  discount?: number
  dsc?: number
  goods?: string
  id?: string
  mfd?: string
  nums?: number
  pid?: string
  price?: number
  retreat?: number
  serial?: string
  source?: string
  tat?: number
  tax?: number
  total?: number
  tpt?: number
  unit?: string
  warehouse?: string
}

/**
 * 销售单列表数据对象
 */
export interface SaleListItem {
  id: string
  account?: string
  actual?: number
  check?: number
  cost?: number
  cse?: number
  customer?: string
  data?: string
  examine?: number
  file?: string
  frame?: string
  invoice?: number
  logistics?: string
  money?: number
  more?: string
  nucleus?: number
  number?: string
  people?: string
  source?: string
  time?: string
  total?: number
  user?: string
}

/**
 * 销售单查询参数
 */
export interface SaleQueryParams extends PageQuery {
  check?: number
  cse?: number
  customer?: string
  data?: string
  endTime?: string
  examine?: number
  goodsName?: string
  invoice?: number
  nucleus?: number
  number?: string
  people?: string
  startTime?: string
  user?: string
}

/**
 * 销售退货单生成数据对象
 */
export interface SaleReturnGenerateData {
  account?: string
  actual?: number
  check?: number
  cost?: number
  cse?: number
  customer?: string
  data?: string
  examine?: number
  file?: string
  frame?: string
  id?: string
  invoice?: number
  logistics?: string
  money?: number
  more?: string
  nucleus?: number
  number?: string
  people?: string
  source?: string
  time?: string
  total?: number
  user?: string
}

/**
 * 批量操作参数
 */
export interface BatchOperationParams {
  ids: string[]
  check?: boolean
  examine?: boolean
}
