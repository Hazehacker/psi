// src/apis/dashboard.ts
// 首页面板接口（包含本地模拟数据）

import { useHttp } from '@/plugins/http'

import type {
  DashboardStatisticsVO,
  DashboardFunctionsVO,
  DashboardSummaryVO,
  DashboardTrendVO,
  DashboardStockVO,
  DashboardAssetsVO,
  DashboardFundsVO,
  OverviewRangeQuery,
  PurchaseReturnQuery,
  SaleQuery,
  SaleReturnQuery,
  PaymentTrendQuery,
  ReceiptQuery
} from './type'
// =============================
// 一、数据统计（顶部四项）
// =============================
export async function getStatistics() {
  return useHttp().get<DashboardStatisticsVO>('/homepage/query-data-statistics')
}

/** 常用功能 */
export function getFunctions() {
  return useHttp().get<DashboardFunctionsVO>('/homepage/query-common-menus')
}

/** 汇总信息 */
export function getSummary() {
  return useHttp().get<DashboardSummaryVO>('/homepage/query-summary-info')
}

/** 数据概览（假设前 6 个都是 GET） */
//采购单
export function getOverviewPurchase(params: OverviewRangeQuery) {
  return useHttp().get<DashboardTrendVO>('/homepage/query-overview-purchase', params)
}
//采购退货单
export function getOverviewPurchaseReturn(params: PurchaseReturnQuery) {
  return useHttp().get<DashboardTrendVO>('/homepage/query-overview-purchase-return', params)
}
//销售单
export function getOverviewSale(params: SaleQuery) {
  return useHttp().get<DashboardTrendVO>('/homepage/query-overview-sales', params)
}
//销售退货单
export function getOverviewSaleReturn(params: SaleReturnQuery) {
  return useHttp().get<DashboardTrendVO>('/homepage/query-show-Imy', params)
}
//收款单
export function getOverviewReceipt(params: ReceiptQuery) {
  return useHttp().get<DashboardTrendVO>('/homepage/query-show-omy', params)
}
//付款单
export function getOverviewPayment(params: PaymentTrendQuery) {
  return useHttp().get<DashboardTrendVO>('/homepage/data-overview-omy', params)
}

/** 库存饼图 */
export function getOverviewStock() {
  return useHttp().get<DashboardStockVO>('/homepage/data-overview-room')
}

/** 资产 */
export function getAssets() {
  return useHttp().get<DashboardAssetsVO>('/homepage/query-balance/sum')
}

/** 资金 */
export function getFunds() {
  return useHttp().get<DashboardFundsVO>('/homepage/query-balance/account')
}
