/**
 * 销售订单相关类型定义
 */

import type { PageDTO, PageQuery } from '@/apis/type'

/**
 * 销售订单数据对象
 */
export interface SaleOrderData {
  id?: string
  customer?: string
  time?: string
  number?: string
  total?: number
  actual?: number
  examine?: number
  state?: number
  user?: string
  people?: string
  logistics?: string
  data?: string
  frame?: string
  arrival?: string
  file?: string
  more?: string
}

/**
 * 销售订单查询参数
 */
export interface SaleOrderQuery extends PageQuery {
  customer?: string
  number?: string
  time?: string
  startTime?: string
  endTime?: string
  examine?: number
  state?: number
  user?: string
  data?: string
  arrival?: string
  id?: string
}

/**
 * 销售订单审核请求对象
 */
export interface SaleOrderVerifyRequest {
  ids: string[]
  num: number // 1表示审核，0表示反审核
}

/**
 * 销售订单详情数据对象
 */
export interface SaleOrderDetailData {
  id?: string
  pid?: string
  goods?: string
  nums?: number
  price?: number
  total?: number
  tax?: number
  tat?: number
  tpt?: number
  discount?: number
  dsc?: number
  data?: string
  attr?: string
  unit?: string
  warehouse?: string
  handle?: number
}

/**
 * 生成销售订单数据传输对象
 */
export interface GenerateSaleOrderData {
  id?: string
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
 * 生成采购订单数据传输对象
 */
export interface GeneratePurchaseOrderData {
  id?: string
  account?: string
  actual?: number
  check?: number
  cost?: number
  cse?: number
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
  supplier?: string
  time?: string
  total?: number
  user?: string
}

/**
 * 导入响应类型
 */
export interface ImportResponse {
  code: number
  data: boolean
  message: string
}

/**
 * 删除请求类型
 */
export interface DeleteRequest {
  ids: string[]
}
