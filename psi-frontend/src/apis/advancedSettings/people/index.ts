/**
 * 人员管理 API 接口
 * @file src/apis/advancedSettings/people/index.ts
 * @description 人员管理模块的 API 接口定义，基于 OpenAPI 规范
 */

import { useHttp } from '@/plugins/http'
import type {
  PersonnelListPageJsonVO,
  PersonnelDetailJsonVO,
  PersonnelJsonVO,
  PersonnelDeleteJsonVO,
  PersonnelImportJsonVO,
  PersonnelAddDTO,
  PersonnelEditDTO,
  PersonnelExportReqDTO
} from './type'

// 定义一个功能模块基础url，方便替换
const currBaseUrl = '/c1-systemparameters/personnel'
/**
 * 获取人员列表（条件+分页）
 * @description 调用后端 GET /personnel/query-by-list 接口获取人员列表
 * @param params 查询参数
 * @returns Promise<PersonnelListPageJsonVO> 返回人员列表
 */
export function getPersonnelList(params: {
  pageIndex: number
  pageSize: number
  number?: string
  name?: string
  sex?: string
  tel?: string
  add?: string
  card?: string
  data?: string
}): Promise<PersonnelListPageJsonVO> {
  const http = useHttp()
  
  // 手动构建查询字符串，绕过全局参数包装
  const queryString = Object.entries(params)
    .filter(([_, value]) => value !== undefined && value !== '')
    .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
    .join('&');
  
  const url = queryString ? `${currBaseUrl}/query-by-list?${queryString}` : `${currBaseUrl}/query-by-list`;
  
  return http.get<PersonnelListPageJsonVO>(url) as Promise<PersonnelListPageJsonVO>
}
/**
 * 获取指定人员详情
 * @description 调用后端 GET /personnel/detail 接口获取人员详情
 * @param id 人员编号
 * @returns Promise<PersonnelDetailJsonVO> 返回人员详情
 */
export function getPersonnelDetail(id: string): Promise<PersonnelDetailJsonVO> {
  const http = useHttp()
  return http.get<PersonnelDetailJsonVO>(currBaseUrl + '/detail', { params: { id } }) as Promise<PersonnelDetailJsonVO>
}

/**
 * 新增人员
 * @description 调用后端 POST /personnel/add 接口新增人员
 * @param data 人员数据
 * @returns Promise<PersonnelJsonVO> 返回新增结果
 */
export function addPersonnel(data: PersonnelAddDTO): Promise<PersonnelJsonVO> {
  const http = useHttp()
  return http.post<PersonnelJsonVO>(currBaseUrl + '/add', data) as Promise<PersonnelJsonVO>
}

/**
 * 修改指定人员
 * @description 调用后端 PUT /personnel/modify-per 接口修改人员
 * @param data 人员数据数组
 * @returns Promise<PersonnelJsonVO> 返回修改结果
 */
export function modifyPersonnel(data: PersonnelEditDTO[]): Promise<PersonnelJsonVO> {
  const http = useHttp()
  return http.put<PersonnelJsonVO>(currBaseUrl + '/modify-per', data) as Promise<PersonnelJsonVO>
}

/**
 * 删除指定人员（支持批量）
 * @description 调用后端 DELETE /personnel/delete 接口删除人员
 * @param ids 人员ID字符串，多个用逗号分隔
 * @returns Promise<PersonnelDeleteJsonVO> 返回删除结果
 */
export function deletePersonnel(ids: string): Promise<PersonnelDeleteJsonVO> {
  const http = useHttp()
  return http.delete<PersonnelDeleteJsonVO>(currBaseUrl + '/delete', { params: { ids } }) as Promise<PersonnelDeleteJsonVO>
}

/**
 * 导入数据
 * @description 调用后端 POST /personnel/import 接口导入人员数据
 * @param file 上传的文件（FormData）
 * @returns Promise<PersonnelImportJsonVO> 返回导入结果
 */
export function importPersonnel(file: FormData): Promise<PersonnelImportJsonVO> {
  const http = useHttp()
  return http.post<PersonnelImportJsonVO>(currBaseUrl + '/import', file, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }) as Promise<PersonnelImportJsonVO>
}

/**
 * 导出数据
 * @description 调用后端 POST /personnel/export 接口导出人员数据
 * @param data 导出请求数据
 * @returns Promise<Blob> 返回导出文件
 */
export function exportPersonnel(data: PersonnelExportReqDTO): Promise<Blob> {
  const http = useHttp()
  return http.post(currBaseUrl + '/export', data, { 
    responseType: 'blob',
    timeout: 60000 // 增加超时时间，因为导出可能需要较长时间
  }) as unknown as Promise<Blob>
}

export default {
  getPersonnelList,
  getPersonnelDetail,
  addPersonnel,
  modifyPersonnel,
  deletePersonnel,
  importPersonnel,
  exportPersonnel
}
