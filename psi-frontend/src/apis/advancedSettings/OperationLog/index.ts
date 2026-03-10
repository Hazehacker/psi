/**
 * 操作日志 API 接口
 * @file src/apis/advancedSettings/OperationLog/index.ts
 * @description 操作日志模块的 API 接口定义，基于 OpenAPI 规范
 */

import { useHttp } from '@/plugins/http'
import type {
  LogOperationPageJsonVO,
  LogOperationResponseJsonVO,
  LogOperationResultJsonVO,
  LogOperationAddDTO
} from './type'

// 定义一个功能模块基础url，方便替换
const currBaseUrl = '/c1-systemparameters/log'

/**
 * 分页查询日志数据
 * @description 调用后端 GET /log/query-log 接口查询日志数据
 * @param params 查询参数
 * @returns Promise<LogOperationPageJsonVO> 返回日志列表
 */
export function queryLog(params: {
  pageIndex: number
  pageSize: number
  user?: string
  info?: string
  begintime?: string
  endtime?: string
}): Promise<LogOperationPageJsonVO> {
  const http = useHttp()
  
  // 手动构建查询字符串，确保格式与后端期望一致
  const queryParams = new URLSearchParams()
  queryParams.append('pageIndex', params.pageIndex.toString())
  queryParams.append('pageSize', params.pageSize.toString())
  
  if (params.user) queryParams.append('user', params.user)
  if (params.info) queryParams.append('info', params.info)
  if (params.begintime) queryParams.append('begintime', params.begintime)
  if (params.endtime) queryParams.append('endtime', params.endtime)
  
  const queryString = queryParams.toString()
  const url = `${currBaseUrl}/query-log${queryString ? `?${queryString}` : ''}`
  
  console.log('最终请求URL:', url) // 调试信息
  
  return http.get<LogOperationPageJsonVO>(url) as Promise<LogOperationPageJsonVO>
}

/**
 * 新增日志数据
 * @description 调用后端 POST /log/logs 接口新增日志数据
 * @param data 日志数据
 * @returns Promise<LogOperationResponseJsonVO> 返回新增结果
 */
export function addLog(data: LogOperationAddDTO): Promise<LogOperationResponseJsonVO> {
  const http = useHttp()
  return http.post<LogOperationResponseJsonVO>(currBaseUrl + '/logs', data) as Promise<LogOperationResponseJsonVO>
}

/**
 * 清空日志数据
 * @description 调用后端 DELETE /log/clear 接口清空日志数据
 * @param confirm 确认参数
 * @returns Promise<LogOperationResultJsonVO> 返回清空结果
 */
export function clearLogs(confirm: string): Promise<LogOperationResultJsonVO> {
  const http = useHttp()
  
  // 同样手动构建清空日志的URL
  const url = `${currBaseUrl}/clear?confirm=${encodeURIComponent(confirm)}`
  
  return http.delete<LogOperationResultJsonVO>(url) as Promise<LogOperationResultJsonVO>
}

export default {
  queryLog,
  addLog,
  clearLogs
}