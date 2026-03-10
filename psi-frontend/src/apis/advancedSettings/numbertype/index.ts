/**
 * 单据编号 API 接口
 * @file src/apis/advancedSettings/numbertype/index.ts
 * @description 单据编号模块的 API 接口定义，基于 OpenAPI 规范
 */

import { useHttp } from '@/plugins/http'
import type {
  ReceiptPageJsonVO,
  ReceiptgetJsonVO,
  ReceiptAddNumberDTO,
  ReceiptRemoveNumberDTO
} from './type'

// 定义一个功能模块基础url，方便替换
const currBaseUrl = '/c1-systemparameters/number-type'

/**
 * 查询所有单据编号记录
 * @description 调用后端 GET /number-type/query-all 接口查询单据编号记录
 * @param params 查询参数
 * @returns Promise<ReceiptPageJsonVO> 返回单据编号列表
 */
export function queryAllDocument(params: {
  pageIndex: number
  pageSize: number
  NumberType: string
}): Promise<ReceiptPageJsonVO> {
  const http = useHttp()
  const queryParams = new URLSearchParams()
  queryParams.append('NumberType', params.NumberType)
  queryParams.append('pageIndex', params.pageIndex.toString())
  queryParams.append('pageSize', params.pageSize.toString())
  const url = `${currBaseUrl}/query-all?${queryParams.toString()}`
  return http.get<ReceiptPageJsonVO>(url) as Promise<ReceiptPageJsonVO>
}

/**
 * 新增单据编号
 * @description 调用后端 POST /number-type/get-number 接口新增单据编号
 * @param data 单据编号数据
 * @returns Promise<ReceiptgetJsonVO> 返回新增结果
 */
export function addReceiptNumber(data: ReceiptAddNumberDTO): Promise<ReceiptgetJsonVO> {
  const http = useHttp()
  return http.post<ReceiptgetJsonVO>(currBaseUrl + '/get-number', data) as Promise<ReceiptgetJsonVO>
}

/**
 * 删除单据编号记录
 * @description 调用后端 DELETE /number-type/remove-number 接口删除单据编号记录
 * @param data 删除数据
 * @returns Promise<any> 返回删除结果
 */
export function removeReceiptNumber(data: ReceiptRemoveNumberDTO): Promise<any> {
  const http = useHttp()
  return http.delete(currBaseUrl + '/remove-number', { data }) as Promise<any>
}

export default {
  queryAllDocument,
  addReceiptNumber,
  removeReceiptNumber
}