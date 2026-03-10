import { useHttp, type RequestCallback } from '@/plugins/http'
import type { JsonVO, PageDTO } from '@/apis/type'
import type {
  SaleDTO,
  SaleDetailItem,
  SaleListItem,
  SaleQueryParams,
  SaleReturnGenerateData,
  BatchOperationParams
} from './type'

// 销售单基础URL
const baseUrl = '/sale/form'

/**
 * 销售单API
 */
export const saleOrderApi = {
  /**
   * 新增销售单
   */
  async addSale(data: SaleDTO): Promise<JsonVO<SaleDTO>> {
    const http = useHttp()
    return await http.post<SaleDTO>(`${baseUrl}/addSale`, data, {
      showLoading: true
    })
  },

  /**
   * 核对/反核对销售单（支持批量）
   */
  async checkSales(ids: string[], check: boolean): Promise<JsonVO<number>> {
    const http = useHttp()
    return await http.post<number>(`${baseUrl}/check`, ids, {
      params: { check },
      showLoading: true
    })
  },

  /**
   * 删除销售单（支持批量）
   */
  async deleteSales(ids: string[]): Promise<JsonVO<number>> {
    const http = useHttp()
    return await http.delete<number>(`${baseUrl}/deleteSales`, ids, {
      showLoading: true
    })
  },

  /**
   * 审核/反审核销售单（支持批量）
   */
  async examineSales(ids: string[], examine: boolean): Promise<JsonVO<number>> {
    const http = useHttp()
    return await http.post<number>(`${baseUrl}/examine`, ids, {
      params: { examine },
      showLoading: true
    })
  },

  /**
   * 导出销售单详情报表
   */
  async exportDetail(ids: string[]): Promise<Blob> {
    const http = useHttp()
    const response = await http.getFile(`${baseUrl}/export-detail`, { ids })
    return response.data
  },

  /**
   * 导出销售单简单报表
   */
  async exportSimple(ids: string[]): Promise<Blob> {
    const http = useHttp()
    const response = await http.getFile(`${baseUrl}/export-simple`, { ids })
    return response.data
  },

  /**
   * 获取生成销售退货单数据
   */
  async getGenerateReturnData(saleId: string): Promise<JsonVO<SaleReturnGenerateData>> {
    const http = useHttp()
    return await http.get<SaleReturnGenerateData>(`${baseUrl}/getGenerateReturnData`, { saleId })
  },

  /**
   * 获取销售单详情
   */
  async getSaleDetail(params: {
    customer?: string
    number?: string
    time?: string
  }): Promise<JsonVO<SaleDetailItem[]>> {
    const http = useHttp()
    return await http.get<SaleDetailItem[]>(`${baseUrl}/getSaleDetail`, params)
  },

  /**
   * 获取销售单列表（条件+分页）
   */
  async getSaleList(params: SaleQueryParams): Promise<JsonVO<PageDTO<SaleListItem>>> {
    const http = useHttp()
    return await http.get<PageDTO<SaleListItem>>(`${baseUrl}/getSaleList`, params, {
      showLoading: true
    })
  },

  /**
   * 导入销售单详细报表
   */
  async importSale(file: File): Promise<JsonVO<string>> {
    const http = useHttp()
    const formData = new FormData()
    formData.append('file', file)
    return await http.postWithFile<string>(`${baseUrl}/import`, formData, {
      showLoading: true
    })
  },

  /**
   * 修改销售单
   */
  async updateSale(data: SaleDTO): Promise<JsonVO<SaleDTO>> {
    const http = useHttp()
    return await http.put<SaleDTO>(`${baseUrl}/updateSale`, data, {
      showLoading: true
    })
  }
}

export default saleOrderApi
