import type { PageDTO, PageQuery } from '@/apis/type'

// 所有接口定义保持不变，但确保每个接口都正确导出
export interface TradeExpenseQueryParams extends PageQuery {
  customerId?: string
  documentNumber?: string
  documentType?: string[]
  startDate?: string
  endDate?: string
  expenseTypeId?: string[]
  settlementStatus?: number[]
  supplierId?: string
}

export interface TradeExpenseReportQueryParams extends PageQuery {
  customerId?: string
  documentNumber?: string
  documentType?: string[]
  startDate?: string
  endDate?: string
  expenseTypeId?: string[]
  settlementStatus?: number[]
  supplierId?: string
}

export interface TradeExpensePageQueryParams extends PageQuery {
  customerId?: string
  documentType?: string[]
  startDate?: string
  endDate?: string
  expenseCategoryId?: string[]
  orderNumber?: string
  settleStatus?: number[]
  supplierId?: string
}

export interface CostDTO {
  id?: string
  money?: number
  type?: string
  data?: string
}

export interface ReportInfoDTO {
  id?: string
  amount?: number
  documentDate?: string
  documentNumber?: string
  documentType?: string
  organization?: string
  partnerUnit?: string
}

export interface TradeExpenseReportDTO {
  amount?: number
  documentDate?: string
  documentNumber?: string
  documentType?: string
  expenseType?: string
  id?: string
  infos?: ReportInfoDTO[]
  organization?: string
  partnerUnit?: string
  settlementStatus?: string
}

export interface TradeExpenseResponseDTO {
  amount?: number
  documentDate?: string
  documentNumber?: string
  documentType?: string
  expenseType?: string
  id?: string
  organization?: string
  partnerUnit?: string
  settledAmount?: number
  settlementAmount?: number
  settlementStatus?: string
  unsettledAmount?: number
}

export interface SettlementRequest {
  ids: string[]
}

export interface ExportRequest {
  ids?: string[]
  customerId?: string
  documentNumber?: string
  documentType?: string[]
  startDate?: string
  endDate?: string
  expenseTypeId?: string[]
  settlementStatus?: number[]
  supplierId?: string
}
