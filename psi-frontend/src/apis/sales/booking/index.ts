import { useHttp } from '@/plugins/http'
import type {
  SaleOrderData,
  SaleOrderQuery,
  SaleOrderVerifyRequest,
  SaleOrderDetailData,
  GenerateSaleOrderData,
  GeneratePurchaseOrderData,
  ImportResponse,
  DeleteRequest
} from './type'
import type { JsonVO, PageDTO } from '@/apis/type'

// 销售订单基础路径
const baseUrl = '/sale/order'

/**
 * 获取生成采购订单数据
 * @param purchaseId 采购单ID
 */
export const getGeneratePurchaseOrderData = async (
  purchaseId: string
): Promise<JsonVO<GeneratePurchaseOrderData>> => {
  const http = useHttp()
  return http.get(`${baseUrl}/getGeneratePurchaseOrderData`, { purchaseId })
}

/**
 * 获取生成销售单数据
 * @param saleId 销售单ID
 */
export const getGenerateSaleOrderData = async (
  saleId: string
): Promise<JsonVO<GenerateSaleOrderData>> => {
  const http = useHttp()
  return http.get(`${baseUrl}/getGenerateSaleOrderData`, { saleId })
}

/**
 * 导出详细报表
 * @param billNo 单据编号数组
 */
export const exportDetailReport = async (billNo: string[]): Promise<any> => {
  const http = useHttp()
  return http.getFile(
    `${baseUrl}/saleOrderExportDetail`,
    { billNo },
    {
      responseType: 'blob',
      showLoading: true
    }
  )
}

/**
 * 导出简单报表
 * @param billNo 单据编号数组
 */
export const exportSimpleReport = async (billNo: string[]): Promise<any> => {
  const http = useHttp()
  return http.getFile(
    `${baseUrl}/saleOrderExportSimple`,
    { billNo },
    {
      responseType: 'blob',
      showLoading: true
    }
  )
}

/**
 * 导入销售订单数据
 * @param file Excel文件
 */
export const importSaleOrders = async (file: File): Promise<JsonVO<boolean>> => {
  const http = useHttp()
  return http.postWithFile(
    `${baseUrl}/saleOrderImport`,
    { file },
    {
      showLoading: true
    }
  )
}

/**
 * 获取指定销售订单详情
 * @param params 查询参数
 */
export const getSaleOrderInfo = async (params: {
  customer?: string
  number?: string
  time?: string
}): Promise<JsonVO<SaleOrderDetailData[][]>> => {
  const http = useHttp()
  return http.get(`${baseUrl}/saleOrderInfo`, params, {
    showLoading: true
  })
}

/**
 * 新增销售订单
 * @param data 销售订单数据
 */
export const addSaleOrder = async (data: SaleOrderData): Promise<JsonVO<boolean>> => {
  const http = useHttp()
  return http.post(`${baseUrl}/saleOrdersAdd`, data, {
    showLoading: true
  })
}

/**
 * 批量删除销售订单
 * @param ids 订单ID列表
 */
export const deleteSaleOrders = async (ids: string[]): Promise<JsonVO<boolean>> => {
  const http = useHttp()
  return http.delete(
    `${baseUrl}/saleOrdersDelete`,
    { ids },
    {
      showLoading: true
    }
  )
}

/**
 * 获取销售订单列表（条件+分页）
 * @param query 查询参数
 */
export const getSaleOrdersList = async (
  query: SaleOrderQuery
): Promise<JsonVO<PageDTO<SaleOrderData>>> => {
  const http = useHttp()
  return http.post(`${baseUrl}/saleOrdersList`, query, {
    showLoading: true
  })
}

/**
 * 修改销售订单
 * @param data 销售订单数据
 */
export const updateSaleOrder = async (data: SaleOrderData): Promise<JsonVO<boolean>> => {
  const http = useHttp()
  return http.put(`${baseUrl}/saleOrdersUpdate`, data, {
    showLoading: true
  })
}

/**
 * 批量审核-反审核销售订单
 * @param request 审核请求对象
 */
export const verifySaleOrders = async (
  request: SaleOrderVerifyRequest
): Promise<JsonVO<boolean>> => {
  const http = useHttp()
  return http.post(`${baseUrl}/saleOrdersVerify`, request, {
    showLoading: true
  })
}

// 默认导出所有API
export default {
  getGeneratePurchaseOrderData,
  getGenerateSaleOrderData,
  exportDetailReport,
  exportSimpleReport,
  importSaleOrders,
  getSaleOrderInfo,
  addSaleOrder,
  deleteSaleOrders,
  getSaleOrdersList,
  updateSaleOrder,
  verifySaleOrders
}

// 导出类型
export type {
  SaleOrderData,
  SaleOrderQuery,
  SaleOrderVerifyRequest,
  SaleOrderDetailData,
  GenerateSaleOrderData,
  GeneratePurchaseOrderData,
  ImportResponse,
  DeleteRequest
}
