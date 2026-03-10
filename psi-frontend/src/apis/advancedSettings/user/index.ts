/**
 * 用户管理 API 接口
 * @file src/apis/advancedSettings/user/index.ts
 * @description 用户管理模块的 API 接口定义，基于 OpenAPI 规范
 */

import { useHttp } from '@/plugins/http'
import type {
  UserPageJsonVO,
  UserDetailJsonVO,
  UserJsonVO,
  UserAddDTO,
  UserModDTO
} from './type'

// 定义一个功能模块基础url，方便替换
const currBaseUrl = '/c1-systemparameters/user'
/**
 * 获取用户列表(条件+分页)
 * @description 调用后端 GET /user/query-all 接口获取用户列表
 * @param params 查询参数
 * @returns Promise<UserPageJsonVO> 返回用户列表
 */
export function getUserList(params: {
  pageIndex: number
  pageSize: number
  data?: string
  tel?: string
  name?: string
  user?: string
}): Promise<UserPageJsonVO> {
  const http = useHttp()
  
  // 手动构建查询字符串，避免 useHttp 的嵌套参数处理
  const queryParams = new URLSearchParams()
  queryParams.append('pageIndex', params.pageIndex.toString())
  queryParams.append('pageSize', params.pageSize.toString())
  
  if (params.name) queryParams.append('name', params.name)
  if (params.user) queryParams.append('user', params.user)
  if (params.tel) queryParams.append('tel', params.tel)
  if (params.data) queryParams.append('data', params.data)
  
  const url = currBaseUrl + '/query-all?' + queryParams.toString()
  
  return http.get<UserPageJsonVO>(url) as Promise<UserPageJsonVO>
}

/**
 * 获取指定用户详情
 * @description 调用后端 GET /user/query-one 接口获取用户详情
 * @param id 用户编号
 * @returns Promise<UserDetailJsonVO> 返回用户详情
 */
export function getUserDetail(id: string): Promise<UserDetailJsonVO> {
  const http = useHttp()
  
  // 手动构建查询字符串，确保 id 参数正确传递
  const queryParams = new URLSearchParams()
  queryParams.append('id', id)
  
  const url = currBaseUrl + '/query-one?' + queryParams.toString()
  
  return http.get<UserDetailJsonVO>(url) as Promise<UserDetailJsonVO>
}

/**
 * 新增用户
 * @description 调用后端 POST /user/add-user 接口新增用户
 * @param data 用户数据
 * @returns Promise<UserJsonVO> 返回新增结果
 */
export function addUser(data: UserAddDTO): Promise<UserJsonVO> {
  const http = useHttp()
  return http.post<UserJsonVO>(currBaseUrl + '/add-user', data) as Promise<UserJsonVO>
}

/**
 * 修改用户
 * @description 调用后端 PUT /user/mod-user 接口修改用户
 * @param data 用户数据
 * @returns Promise<UserJsonVO> 返回修改结果
 */
export function modifyUser(data: UserModDTO): Promise<UserJsonVO> {
  const http = useHttp()
  return http.put<UserJsonVO>(currBaseUrl + '/mod-user', data) as Promise<UserJsonVO>
}

/**
 * 删除用户
 * @description 调用后端 DELETE /user/del-user 接口删除用户
 * @param id 用户ID
 * @returns Promise<UserJsonVO> 返回删除结果
 */
export function deleteUser(id: string): Promise<UserJsonVO> {
  const http = useHttp()
  return http.delete<UserJsonVO>(currBaseUrl + '/del-user', { data: id }) as Promise<UserJsonVO>
}

export default {
  getUserList,
  getUserDetail,
  addUser,
  modifyUser,
  deleteUser
}
