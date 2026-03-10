/**
 * 组织结构 API 接口
 * @file src/apis/advancedSettings/organization/index.ts
 * @description 组织结构模块的 API 接口定义，基于 OpenAPI 规范
 */

import { useHttp } from '@/plugins/http'
import type {
  OrganizationListJsonVO,
  OrganizationDetailJsonVO,
  OrganizationTreeJsonVO,
  OrganizationAddDTO,
  OrganizationUpdateDTO,
  BooleanJsonVO
} from './type'

// 定义一个功能模块基础url，方便替换
const currBaseUrl = '/c1-systemparameters/organization'

/**
 * 获取组织列表
 * @description 调用后端 GET /organization/query-by-list 接口获取组织列表
 * @returns Promise<OrganizationListJsonVO> 返回组织列表
 */
export function getOrganizationList(): Promise<OrganizationListJsonVO> {
  const http = useHttp()
  return http.get<OrganizationListJsonVO>(currBaseUrl + '/query-by-list') as Promise<OrganizationListJsonVO>
}

/**
 * 获取指定组织详情
 * @description 调用后端 GET /organization/detail 接口获取组织详情
 * @param id 组织ID
 * @returns Promise<OrganizationDetailJsonVO> 返回组织详情
 */
export function getOrganizationDetail(id: string): Promise<OrganizationDetailJsonVO> {
  const http = useHttp()
  return http.get<OrganizationDetailJsonVO>(`${currBaseUrl}/detail?id=${encodeURIComponent(id)}`) as Promise<OrganizationDetailJsonVO>
}


/**
 * 获取组织结构树
 * @description 调用后端 GET /organization/tree 接口获取组织结构树
 * @returns Promise<OrganizationTreeJsonVO> 返回组织结构树
 */
export function getOrganizationTree(): Promise<OrganizationTreeJsonVO> {
  const http = useHttp()
  return http.get<OrganizationTreeJsonVO>(currBaseUrl + '/tree') as Promise<OrganizationTreeJsonVO>
}

/**
 * 添加组织
 * @description 调用后端 POST /organization/add 接口添加组织
 * @param data 组织数据
 * @returns Promise<any> 返回添加结果
 */
export function addOrganization(data: OrganizationAddDTO): Promise<any> {
  const http = useHttp()
  return http.post(currBaseUrl + '/add', data) as Promise<any>
}

/**
 * 修改组织
 * @description 调用后端 PUT /organization/update 接口修改组织
 * @param id 组织ID
 * @param data 组织数据
 * @returns Promise<any> 返回修改结果
 */
export function updateOrganization(id: string, data: OrganizationUpdateDTO): Promise<any> {
  const http = useHttp()
  return http.put(`${currBaseUrl}/update?id=${encodeURIComponent(id)}`, data)
}

/**
 * 删除组织
 * @description 调用后端 DELETE /organization/delete 接口删除组织
 * @param id 组织ID
 * @returns Promise<BooleanJsonVO> 返回删除结果
 */
export function deleteOrganization(id: string): Promise<BooleanJsonVO> {
  const http = useHttp()
  return http.delete<BooleanJsonVO>(`${currBaseUrl}/delete?id=${encodeURIComponent(id)}`) as Promise<BooleanJsonVO>
  }


export default {
  getOrganizationList,
  getOrganizationDetail,
  getOrganizationTree,
  addOrganization,
  updateOrganization,
  deleteOrganization
}