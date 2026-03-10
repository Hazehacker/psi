import { useHttp, type RequestCallback } from '@/plugins/http'
import type { PageDTO } from '@/apis/type'

// 重新导出所有类型
export * from './type'

// 基础URL
const BASE_URL = '/finance/trade-expense'

// 所有函数实现保持不变...
export const exportExpenseData = (params?: any) => {
  const http = useHttp()
  return http.getFile(`${BASE_URL}/export/expense`, params, {
    showLoading: true,
    responseType: 'blob'
  })
}

export const exportReportData = (params?: any) => {
  const http = useHttp()
  return http.getFile(`${BASE_URL}/export/report`, params, {
    showLoading: true,
    responseType: 'blob'
  })
}

/**
 * 获取结算单数据 - 修复参数格式
 * @param ids 结算单ID列表
 * @returns 结算单数据列表
 */
export const getSettles = (ids: string[]) => {
  const http = useHttp()
  // 将数组转换为逗号分隔的字符串，或者保持数组格式根据后端要求调整
  const params = {
    ids: ids.join(',') // 或者保持数组格式：ids: ids
  }
  return http.get<any>(`${BASE_URL}/getSettles`, params, {
    showLoading: true
  })
}

export const getTradeExpenseList = (params: any) => {
  const http = useHttp()
  return http.get<PageDTO<any>>(`${BASE_URL}/list`, params, {
    showLoading: true
  })
}

export const getTradeExpensePage = (params: any) => {
  const http = useHttp()
  return http.get<PageDTO<any>>(`${BASE_URL}/query/page`, params, {
    showLoading: true
  })
}
