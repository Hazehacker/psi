export interface DashboardStatisticsVO {
  //数据统计
  dailySales: { amount: number; rate: number }
  dailyGross: { amount: number; rate: number }
  dailyCount: { amount: number; rate: number }
  dailyIncome: { amount: number; rate: number }
}

export type DashboardFunctionsVO = Array<{ label: string; to: string }> //常用功能

export type DashboardSummaryVO = Array<{ label: string; value: number; to: string }> //汇总信息

export interface OverviewRangeQuery {
  // 采购单
  startDate: string
  endDate: string
}
export interface PurchaseReturnQuery {
  //采购退货单
  type: string
  startDate: string
  endDate: string
}
export interface SaleQuery {
  //销售单
  type: string
  startDate: string
  endDate: string
}
export interface SaleReturnQuery {
  //销售退货单
  type: string
}
export interface ReceiptQuery {
  //收款单
  type: string
}
export interface PaymentTrendQuery {
  //付款单
  type: string
  from: string
  to: string
}

export interface DashboardTrendVO {
  //数据概览
  type: string
  list: Array<{ date: string; value: number }>
}

export interface DashboardStockVO {
  //库存饼图
  list: Array<{ name: string; value: number }>
}

export interface DashboardAssetsVO {
  //资产数据
  list: Array<{ label: string; value: string; to: string }>
}

export interface DashboardFundsVO {
  //资金数据
  list: Array<{ name: string; value: number }>
}
