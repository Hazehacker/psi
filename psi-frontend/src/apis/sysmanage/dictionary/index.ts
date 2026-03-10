/*
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-25 22:59:12
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-07 12:23:57
 * @FilePath: \psi-frontend\src\apis\sysmanage\dictionary\index.ts
 * @Description: 系统配置-字典管理相关接口
 */
import { useHttp, type RequestCallback } from '@/plugins/http'
import type { PageDTO } from '@/components/mytable/type'
import type {
  DictPageRequest,
  DictNameItem,
  DictDetailDTO,
  TypePageRequest,
  DictionTypeDTO,
  DictionaryTypeItem
} from './type'

const currBaseUrl = '/j1-sysc/dictionary-manager'
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
 * 字典
 */

/**
 * @description 获取字典列表（条件+分页）
 * @param params 字典分页和搜索参数
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getDictionaryList = async (
  params: DictPageRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<PageDTO<any>>(`${currBaseUrl}/dict-queryList`, params, { showLoading: true }),
    success,
    fail
  )
}

/**
 * @description 获取字典名称列表
 * @param typeId 字典类型唯一标识
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getDictionaryNames = async (
  typeId: string,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<DictNameItem[]>(`${currBaseUrl}/dict-getNames`, { typeId }, { showLoading: true }),
    success,
    fail
  )
}

/**
 * @description 新增字典
 * @param params 字典数据对象
 * @param success 成功回调
 * @param fail 失败回调
 */
export const addDictionary = async (
  params: DictDetailDTO,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(http.post<string>(currBaseUrl, params, { showLoading: true }), success, fail)
}

/**
 * @description 修改字典
 * @param params 字典修改数据对象
 * @param success 成功回调
 * @param fail 失败回调
 */
export const updateDictionary = async (
  params: DictDetailDTO,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(http.put<string>(currBaseUrl, params, { showLoading: true }), success, fail)
}

/**
 * @description 删除字典
 * @param dictId 字典ID
 * @param success 成功回调
 * @param fail 失败回调
 */
export const deleteDictionary = async (
  dictId: string,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.delete<string>(`${currBaseUrl}/${dictId}`, {}, { showLoading: true }),
    success,
    fail
  )
}

/**
 * 字典类型
 */

/**
 * 获取字典类型列表（条件+分页）
 * @param params 字典类型分页和搜索参数
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getDictionaryTypeList = async (
  params: TypePageRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<PageDTO<DictionTypeDTO>>(`${currBaseUrl}/dict-queryList-type`, params, {
      showLoading: true
    }),
    success,
    fail
  )
}

/**
 * @description 获取字典类型名称列表
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getDictionaryTypeNames = async (success: RequestCallback, fail: RequestCallback) => {
  return handleRequest(
    http.get<DictionaryTypeItem[]>(`${currBaseUrl}/dict-getNames-type`, {}, { showLoading: true }),
    success,
    fail
  )
}

/**
 * @description 新增字典类型
 * @param params 字典类型数据对象
 * @param success 成功回调
 * @param fail 失败回调
 */
export const addDictionaryType = async (
  params: DictionTypeDTO,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.post<string>(`${currBaseUrl}/add-dict-type`, params, { showLoading: true }),
    success,
    fail
  )
}

/**
 * @description 修改字典类型
 * @param id 字典类型ID
 * @param params 字典类型数据对象
 * @param success 成功回调
 * @param fail 失败回调
 */
export const updateDictionaryType = async (
  id: string,
  params: DictionTypeDTO,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.put<string>(`${currBaseUrl}/${id}`, params, { showLoading: true }),
    success,
    fail
  )
}

/**
 * @description 删除字典类型
 * @param id 字典类型ID
 * @param success 成功回调
 * @param fail 失败回调
 */
export const deleteDictionaryType = async (
  id: string,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.delete<string>(`${currBaseUrl}/remove-dict-type${id}`, {}, { showLoading: true }),
    success,
    fail
  )
}
