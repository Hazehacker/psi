import type { PageQuery, PageResult } from '@/apis/type'

/**
 * 开票请求参数
 */
export interface InvoiceAddRequest {
  /** 备注信息 */
  data?: string
  /** 发票附件（可为URL或JSON） */
  file?: string
  /** 待开票单据集合 */
  infos: InvoiceItem[]
  /** 发票号码 */
  number: string
  /** 开票时间 */
  time: string
  /** 发票抬头 */
  title: string
}

/**
 * 待开票单据明细
 */
export interface InvoiceItem {
  /** 本张发票开给该单据的金额 */
  amount: number
  /** 单据ID */
  id: string
  /** 单据类型 */
  type: 'buy' | 'bre' | 'sell' | 'sre'
}

/**
 * 发票导出请求参数
 */
export interface InvoiceExportRequest {
  /** 导出发票的Key列表 */
  keys: InvoiceExportKey[]
}

/**
 * 发票导出Key
 */
export interface InvoiceExportKey {
  /** 主单据ID */
  id: string
  /** 单据类型 */
  type: 'buy' | 'bre' | 'sell' | 'sre'
}

/**
 * 报表导出请求参数
 */
export interface ReportExportRequest {
  /** 发票ID列表 */
  ids: string[]
}

/**
 * 发票列表查询参数
 */
export interface InvoiceListQuery extends PageQuery {
  /** 客户ID */
  customer?: string
  /** 查询截止时间 */
  endDate?: string
  /** 单据编号 */
  number?: string
  /** 查询起始时间 */
  startDate?: string
  /** 发票状态 [0:未开票|1:部分开票|2:已开票|3:无需开具] */
  status?: number[]
  /** 供应商ID */
  supplier?: string
  /** 单据类型 */
  type?: string[]
}

/**
 * 发票列表数据
 */
export interface InvoiceListItem {
  /** 未开票金额 */
  disinvoicedCount: number
  /** 所属组织 */
  frame: string
  /** 主单据ID */
  id: string
  /** 开票金额 */
  invoiceTotal: number
  /** 已开票金额 */
  invoicedCount: number
  /** 单据编号 */
  number: string
  /** 发票状态 */
  status: string
  /** 单据时间 */
  time: string
  /** 单据金额 */
  totalCount: number
  /** 单据类型 */
  type: string
  /** 往来单位 */
  unit: string
}

/**
 * 发票列表响应数据
 */
export interface InvoiceListResponse {
  /** 当前页码 */
  pageIndex: number
  /** 每页显示最大数据条数 */
  pageSize: number
  /** 总页数 */
  pages: number
  /** 当前页数据列表 */
  rows: InvoiceListItem[]
  /** 总条数 */
  total: number
}

/**
 * 购销发票报表查询参数
 */
export interface InvoiceReportQuery extends PageQuery {
  /** 客户ID */
  customer?: string
  /** 结束时间 */
  endDate?: string
  /** 发票号码 */
  inr?: string
  /** 单据类型 */
  mold?: string[]
  /** 单据编号 */
  number?: string
  /** 开始时间 */
  startDate?: string
  /** 供应商ID */
  supplier?: string
  /** 发票抬头 */
  title?: string
}

/**
 * 购销发票报表数据
 */
export interface InvoiceReportItem {
  /** 往来单位 */
  businessUnit: string
  /** 备注信息 */
  data: string
  /** 开票附件 */
  file: string
  /** 所属组织 */
  frame: string
  /** 购销发票id */
  id: string
  /** 发票金额 */
  invMoney: number
  /** 发票号码 */
  invNumber: string
  /** 开票时间 */
  invTime: string
  /** 发票状态 */
  invoice: string
  /** 单据编号 */
  number: string
  /** 单据时间 */
  time: string
  /** 发票抬头 */
  title: string
  /** 单据金额 */
  total: number
  /** 单据类型 */
  type: string
}

/**
 * 购销发票报表响应数据
 */
export interface InvoiceReportResponse {
  /** 当前页码 */
  pageIndex: number
  /** 每页显示最大数据条数 */
  pageSize: number
  /** 总页数 */
  pages: number
  /** 当前页数据列表 */
  rows: InvoiceReportItem[]
  /** 总条数 */
  total: number
}
