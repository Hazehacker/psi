/**
 * 用户角色 API 接口
 * @file src/apis/advancedSettings/userrole/index.ts
 * @description 用户角色模块的 API 接口定义，基于 OpenAPI 规范
 */

import { useHttp } from '@/plugins/http'
import type {
  RoleListPageJsonVO,
  RoleDetailJsonVO,
  RoleNameListJsonVO,
  RoleFunctionPermissionsJsonVO,
  RoleAddDTO,
  RoleListDTO
} from './type'

// 定义一个功能模块基础url，方便替换
const currBaseUrl = '/c1-systemparameters/role'

/**
 * 获取角色列表（条件+分页）
 * @description 调用后端 GET /role/query-all 接口获取角色列表
 * @param params 查询参数
 * @returns Promise<RoleListPageJsonVO> 返回角色列表
 */
export function getRoleList(params: {
  pageIndex: number
  pageSize: number
  name?: string
  data?: string
}): Promise<RoleListPageJsonVO> {
  const http = useHttp()
  
  // 手动构建查询字符串，确保格式正确
  const queryParams = new URLSearchParams()
  queryParams.append('pageIndex', params.pageIndex.toString())
  queryParams.append('pageSize', params.pageSize.toString())
  
  if (params.name) queryParams.append('name', params.name)
  if (params.data) queryParams.append('data', params.data)
  
  const queryString = queryParams.toString()
  const url = `${currBaseUrl}/query-all${queryString ? `?${queryString}` : ''}`
  
  console.log('角色列表请求URL:', url) // 调试信息
  
  return http.get<RoleListPageJsonVO>(url) as Promise<RoleListPageJsonVO>
}

/**
 * 获取指定角色详情
 * @description 调用后端 GET /role/detail 接口获取角色详情
 * @param id 角色唯一标识
 * @returns Promise<RoleDetailJsonVO> 返回角色详情
 */
export function getRoleDetail(id: string): Promise<RoleDetailJsonVO> {
  const http = useHttp()
  
  // 手动构建详情查询URL
  const url = `${currBaseUrl}/detail?id=${encodeURIComponent(id)}`
  
  return http.get<RoleDetailJsonVO>(url) as Promise<RoleDetailJsonVO>
}

/**
 * 获取角色名称列表（条件）
 * @description 调用后端 GET /role/list 接口获取角色名称列表
 * @param keyword 搜索关键词（可选）
 * @returns Promise<RoleNameListJsonVO> 返回角色名称列表
 */
export function getRoleNameList(keyword?: string): Promise<RoleNameListJsonVO> {
  const http = useHttp()
  
  let url = `${currBaseUrl}/list`
  if (keyword) {
    url += `?keyword=${encodeURIComponent(keyword)}`
  }
  
  return http.get<RoleNameListJsonVO>(url) as Promise<RoleNameListJsonVO>
}

/**
 * 获取功能权限列表
 * @description 调用后端 GET /role/permission 接口获取功能权限列表
 * @returns Promise<RoleFunctionPermissionsJsonVO> 返回功能权限列表
 */
export function getRolePermission(): Promise<RoleFunctionPermissionsJsonVO> {
  const http = useHttp()
  return http.get<RoleFunctionPermissionsJsonVO>(currBaseUrl + '/permission') as Promise<RoleFunctionPermissionsJsonVO>
}

/**
 * 新增角色
 * @description 调用后端 POST /role/add-role 接口新增角色
 * @param data 角色数据
 * @returns Promise<any> 返回新增结果
 */
export function addRole(data: RoleAddDTO): Promise<any> {
  const http = useHttp()
  return http.post(currBaseUrl + '/add-role', data) as Promise<any>
}

/**
 * 修改角色
 * @description 调用后端 PUT /role/modify-role 接口修改角色
 * @param data 角色数据
 * @returns Promise<any> 返回修改结果
 */
export function modifyRole(data: RoleListDTO): Promise<any> {
  const http = useHttp()
  return http.put(currBaseUrl + '/modify-role', data) as Promise<any>
}

/**
 * 删除角色
 * @description 调用后端 DELETE /role/delete-role 接口删除角色
 * @param ids 角色ID数组
 * @returns Promise<any> 返回删除结果
 */
export function deleteRole(ids: string[]): Promise<any> {
  const http = useHttp()
  return http.delete(currBaseUrl + '/delete-role', { data: ids }) as Promise<any>
}

export default {
  getRoleList,
  getRoleDetail,
  getRoleNameList,
  getRolePermission,
  addRole,
  modifyRole,
  deleteRole
}