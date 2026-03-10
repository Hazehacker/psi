/*
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-23 22:55:27
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-08 22:57:57
 * @FilePath: \psi-frontend\src\apis\sysmanage\menu\index.ts
 * @Description: 系统配置-菜单管理相关接口
 */
import { useHttp, type RequestCallback } from '@/plugins/http'
import type { MenuItem, MenuNameTreeVO } from './type'

const currBaseUrl = '/j1-sysc/menu-manager'
const http = useHttp()

// 通用请求处理函数
const handleRequest = async <T>(
  request: Promise<any>,
  success: RequestCallback,
  fail: RequestCallback
) => {
  try {
    const res = await request
    if (res.data) {
      success(res.data)
    } else {
      fail(res)
    }
  } catch (err) {
    fail(err)
  }
}

/**
 * @description 获取菜单列表
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getMenuList = async (success: RequestCallback, fail: RequestCallback) => {
  return handleRequest(
    http.get<MenuItem[]>(`${currBaseUrl}/query-menuList`, {}, { showLoading: true }),
    success,
    fail
  )
}

/**
 * @description 获取菜单名称树
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getMenuTree = async (success: RequestCallback, fail: RequestCallback) => {
  return handleRequest(
    http.get<MenuNameTreeVO[]>(`${currBaseUrl}/query-menuTree`, {}, { showLoading: true }),
    success,
    fail
  )
}

/**
 * @description 获取指定菜单详情
 * @param id 菜单ID
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getMenuDetail = async (
  id: string,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<any>(`${currBaseUrl}/get-by-id/${id}`, {}, { showLoading: true }),
    success,
    fail
  )
}

/**
 * @description 新增菜单
 * @param params 菜单数据对象
 * @param success 成功回调
 * @param fail 失败回调
 */
export const addMenu = async (params: any, success: RequestCallback, fail: RequestCallback) => {
  return handleRequest(
    http.post<string>(`${currBaseUrl}/add-menu`, params, { showLoading: true }),
    success,
    fail
  )
}

/**
 * @description 修改指定菜单
 * @param id 菜单ID
 * @param params 菜单数据对象
 * @param success 成功回调
 * @param fail 失败回调
 */
export const updateMenu = async (
  id: string,
  params: any,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.put<string>(`${currBaseUrl}/modify-menu/${id}`, params, { showLoading: true }),
    success,
    fail
  )
}

/**
 * @description 删除指定菜单
 * @param id 菜单ID
 * @param success 成功回调
 * @param fail 失败回调
 */
export const deleteMenu = async (id: string, success: RequestCallback, fail: RequestCallback) => {
  return handleRequest(
    http.delete<string>(`${currBaseUrl}/delete-menu/${id}`, {}, { showLoading: true }),
    success,
    fail
  )
}

// 菜单类型枚举
export const MenuType = {
  INDEPENDENT: 0, // 独立菜单
  AFFILIATED: 1 // 附属菜单
} as const

// 菜单模式枚举
export const MenuModel = {
  TAB: 0, // 标签模式
  NEW_PAGE: 1 // 新页模式
} as const
