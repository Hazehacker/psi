/**
 * 其它收入单API接口
 */

import { useHttp } from '@/plugins/http'
import type {
  AddOtherIncomeDTO,
  OtherIncomeItem,
  OtherIncomeQueryParams,
  OtherIncomePageResult,
  OtherIncomeDetail,
  ExamineRequest,
  ExamineResponse,
  JsonVO
} from './type'

// 基础路径
const BASE_URL = '/finance/other-income'

/**
 * 新增收入单
 */
export const addOtherIncome = async (data: AddOtherIncomeDTO): Promise<JsonVO<string>> => {
  const http = useHttp()
  return await http.post(`${BASE_URL}/add`, data)
}

/**
 * 批量删除收入单
 */
export const batchDeleteOtherIncome = async (ids: string[]): Promise<JsonVO<string>> => {
  const http = useHttp()
  return await http.delete(`${BASE_URL}/batch-delete`, { ids })
}

/**
 * 审核或反审核
 */
export const examineOtherIncome = async (
  data: ExamineRequest
): Promise<JsonVO<ExamineResponse>> => {
  const http = useHttp()
  return await http.post(`${BASE_URL}/examine`, data)
}

/**
 * 导出其他收入单详细报表Excel（ZIP格式）
 */
export const exportDetailReportExcel = async (iceIds: string[]): Promise<any> => {
  const http = useHttp()
  return await http.post(
    `${BASE_URL}/export/detail`,
    { iceIds },
    {
      responseType: 'blob'
    }
  )
}
/**
 * 导出其他收入单简单报表Excel
 */
export const exportSimpleReportExcel = async (iceIds: string[]): Promise<any> => {
  const http = useHttp()
  return await http.post(
    `${BASE_URL}/export/simple`,
    { iceIds },
    {
      responseType: 'blob'
    }
  )
}

/**
 * 导入收入单数据(Excel-CSV)
 */
export const importIncomeData = async (file: File): Promise<JsonVO<string>> => {
  const http = useHttp()
  return await http.postWithFile(`${BASE_URL}/import`, { file })
}

/**
 * 获取其它收入单列表
 */
export const getOtherIncomeList = async (
  params: OtherIncomeQueryParams
): Promise<JsonVO<OtherIncomePageResult>> => {
  const http = useHttp()
  return await http.get(`${BASE_URL}/list`, params, {
    upType: 0 // 使用表单格式
  })
}

/**
 * 修改收入单
 */
export const reviseOtherIncome = async (data: AddOtherIncomeDTO): Promise<JsonVO<string>> => {
  const http = useHttp()
  return await http.post(`${BASE_URL}/revise`, data)
}

/**
 * 获取收入单详情
 */
export const getOtherIncomeDetail = async (id: string): Promise<JsonVO<OtherIncomeDetail>> => {
  const http = useHttp()
  return await http.get(`${BASE_URL}/${id}`)
}

// 导出所有类型
export type {
  AddOtherIncomeDTO,
  OtherIncomeItem,
  OtherIncomeQueryParams,
  OtherIncomePageResult,
  OtherIncomeDetail,
  ExamineRequest,
  ExamineResponse,
  JsonVO
}
