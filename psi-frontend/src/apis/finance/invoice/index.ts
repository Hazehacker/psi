import { useHttp, type RequestCallback } from '@/plugins/http'
import type {
  InvoiceAddRequest,
  InvoiceExportRequest,
  ReportExportRequest,
  InvoiceListQuery,
  InvoiceListResponse,
  InvoiceReportQuery,
  InvoiceReportResponse
} from './type'

// 基础URL - 根据文档，这些接口属于资金管理模块，服务地址为 8.159.143.208:9888
const baseUrl = '/finance/trade-invoice'

/**
 * 开票 - 批量为 buy/bre/sell/sre 单据开具发票
 * @param data 开票请求数据
 * @param success 成功回调
 * @param fail 失败回调
 */
export const addInvoice = async (
  data: InvoiceAddRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  try {
    const http = useHttp()
    const res = await http.post<string>(`${baseUrl}/add`, data, {
      showLoading: true
    })
    success(res)
  } catch (err) {
    fail(err)
  }
}

/**
 * 导出发票数据
 * @param data 导出请求数据
 * @param success 成功回调 - 返回Blob数据
 * @param fail 失败回调
 */
export const exportInvoiceData = async (
  data: InvoiceExportRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  try {
    const http = useHttp()
    // 使用getFile方法获取文件流
    const response = await http.getFile(`${baseUrl}/export`, data, {
      showLoading: true,
      responseType: 'blob'
    })
    success(response.data)
  } catch (err) {
    fail(err)
  }
}

/**
 * 导出报表数据
 * @param data 导出请求数据
 * @param success 成功回调 - 返回Blob数据
 * @param fail 失败回调
 */
export const exportReportData = async (
  data: ReportExportRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  try {
    const http = useHttp()
    // 使用getFile方法获取文件流
    const response = await http.getFile(`${baseUrl}/export/report`, data, {
      showLoading: true,
      responseType: 'blob'
    })
    success(response.data)
  } catch (err) {
    fail(err)
  }
}

/**
 * 获取发票列表
 * @param params 查询参数
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getInvoiceList = async (
  params: InvoiceListQuery,
  success: RequestCallback,
  fail: RequestCallback
) => {
  try {
    const http = useHttp()
    const res = await http.get<InvoiceListResponse>(`${baseUrl}/list/invoice`, params, {
      showLoading: true
    })
    success(res.data)
  } catch (err) {
    fail(err)
  }
}

/**
 * 获取购销发票报表(条件+分页)
 * @param params 查询参数
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getInvoiceReportList = async (
  params: InvoiceReportQuery,
  success: RequestCallback,
  fail: RequestCallback
) => {
  try {
    const http = useHttp()
    const res = await http.get<InvoiceReportResponse>(`${baseUrl}/list/report`, params, {
      showLoading: true
    })
    success(res.data)
  } catch (err) {
    fail(err)
  }
}

/**
 * 下载文件工具函数
 * @param blob 文件Blob数据
 * @param filename 文件名
 */
export const downloadFile = (blob: Blob, filename: string) => {
  const url = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  window.URL.revokeObjectURL(url)
}
