import { useHttp } from '@/plugins/http'
import type { JsonVO, PageDTO, PageResult } from '@/apis/type'
import type {
  OtherExpenseDTO,
  OtherExpenseQuery,
  OtherExpenseListData,
  OtherExpenseDetail,
  OtherExpenseImportResult,
  ExamineParams
} from './type'

// 基础URL
const baseUrl = '/finance/other-expense'

/**
 * 新增支出单
 */
export const addOtherExpense = async (data: OtherExpenseDTO): Promise<JsonVO<string>> => {
  const http = useHttp()
  return http.post<string>(`${baseUrl}/add`, data, {
    showLoading: true
  } as any)
}

/**
 * 批量删除支出单
 */
export const batchDeleteOtherExpense = async (ids: string[]): Promise<JsonVO<string>> => {
  const http = useHttp()
  return http.delete<string>(`${baseUrl}/batch-delete`, ids, {
    showLoading: true
  } as any)
}

/**
 * 审核/反审核支出单
 */
export const examineOtherExpense = async (params: ExamineParams): Promise<JsonVO<string>> => {
  const http = useHttp()
  return http.post<string>(`${baseUrl}/examine`, params.ids, {
    params: { approve: params.approve },
    showLoading: true
  } as any)
}

/**
 * 导出其它支出单（简易报表）
 */
export const exportOtherExpense = async (ids: string[]): Promise<Blob> => {
  const http = useHttp()
  const response = await http.getInstance().post(`${baseUrl}/export`, ids, {
    responseType: 'blob',
    showLoading: true
  } as any)
  return response.data
}

/**
 * 导出其它支出单明细（详细报表）
 */
export const exportOtherExpenseDetail = async (ids: string[]): Promise<Blob> => {
  const http = useHttp()
  const response = await http.getInstance().post(`${baseUrl}/exportDetail`, ids, {
    responseType: 'blob',
    showLoading: true
  } as any)
  return response.data
}

/**
 * 导入支出单数据
 */
export const importOtherExpense = async (file: File): Promise<JsonVO<OtherExpenseImportResult>> => {
  const http = useHttp()
  const formData = new FormData()
  formData.append('file', file)
  return http.postWithFile<OtherExpenseImportResult>(`${baseUrl}/import`, formData, {
    showLoading: true
  } as any)
}

/**
 * 查询其它支出单列表
 */
export const getOtherExpenseList = async (
  params: OtherExpenseQuery
): Promise<JsonVO<PageDTO<OtherExpenseListData>>> => {
  const http = useHttp()
  return http.get<PageDTO<OtherExpenseListData>>(`${baseUrl}/list`, params, {
    showLoading: true
  } as any)
}

/**
 * 修改支出单
 */
export const updateOtherExpense = async (data: OtherExpenseDTO): Promise<JsonVO<string>> => {
  const http = useHttp()
  return http.put<string>(`${baseUrl}/update`, data, {
    showLoading: true
  } as any)
}

/**
 * 查询支出单详情
 */
export const getOtherExpenseDetail = async (id: string): Promise<JsonVO<OtherExpenseDetail>> => {
  const http = useHttp()
  return http.get<OtherExpenseDetail>(`${baseUrl}/${id}`, {}, {
    showLoading: true
  } as any)
}
