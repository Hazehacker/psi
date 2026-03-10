/**
 * 结账管理 API 接口
 * @file src/apis/advancedSettings/period/index.ts
 * @description 结账管理模块的 API 接口定义，基于 OpenAPI 规范
 */

import { useHttp } from '@/plugins/http'
import type {
  PeriodJsonVO,
  PeriodRecordPageRespVO,
  SavePeriodDTO
} from './type'

// 定义一个功能模块基础url，方便替换
const currBaseUrl = '/c1-systemparameters/periods'
/**
 * 获取结账列表
 * @description 调用后端 GET /periods/query 接口获取结账列表
 * @param params 查询参数
 * @returns Promise<PeriodRecordPageRespVO> 返回结账列表
 */
export function queryPeriods(params: {
  pageIndex: number
  pageSize: number
}): Promise<PeriodRecordPageRespVO> {
  const http = useHttp()
  return http.get<PeriodRecordPageRespVO>(currBaseUrl + '/query', { params }) as Promise<PeriodRecordPageRespVO>
}

/**
 * 结账
 * @description 调用后端 POST /periods/save 接口进行结账
 * @param data 结账数据
 * @returns Promise<PeriodJsonVO> 返回结账结果
 */
export function savePeriod(data: SavePeriodDTO): Promise<PeriodJsonVO> {
  const http = useHttp()
  return http.post<PeriodJsonVO>(currBaseUrl + '/save', data) as Promise<PeriodJsonVO>
}

/**
 * 反结账
 * @description 调用后端 DELETE /periods/delete 接口进行反结账
 * @returns Promise<PeriodJsonVO> 返回反结账结果
 */
export function deletePeriod(): Promise<PeriodJsonVO> {
  const http = useHttp()
  return http.delete<PeriodJsonVO>(currBaseUrl + '/delete') as Promise<PeriodJsonVO>
}

export default {
  queryPeriods,
  savePeriod,
  deletePeriod
}
