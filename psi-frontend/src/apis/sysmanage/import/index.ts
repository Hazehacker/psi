/*
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-25 22:59:12
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-10 18:31:12
 * @FilePath: \psi-frontend\src\apis\sysmanage\import\index.ts
 * @Description: 系统配置-导入模板相关接口
 */
import { useHttp, type RequestCallback } from '@/plugins/http'
import { DataUpType } from '@/apis/http'
import type { PageDTO } from '@/components/mytable/type'
import type {
  PageRequest,
  ImportTemplateList,
  ImportTemplateForm,
  ImportTemplateDetail
} from './type'

const currBaseUrl = '/j1-sysc/import-template'
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
 * @description 获取模板列表（条件+分页）
 * @param params 分页和搜索参数
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getTemplateList = async (
  params: PageRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<PageDTO<ImportTemplateList>>(`${currBaseUrl}/list`, params, { showLoading: true }),
    success,
    fail
  )
}

/**
 * @description 获取指定模板详情
 * @param id 模板ID
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getTemplateDetail = async (
  id: string,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<ImportTemplateDetail>(`${currBaseUrl}/download/${id}`, {}, { showLoading: true }),
    success,
    fail
  )
}

/**
 * @description 新增模板
 * @param formData FormData 对象
 * @param success 成功回调
 * @param fail 失败回调
 */
export const importTemplate = async (
  formData: FormData,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    // 使用 upType: DataUpType.file 告知 http 插件以 multipart/form-data 方式发送
    http.post<string>(`${currBaseUrl}/import`, formData, {
      showLoading: true,
      upType: DataUpType.file
    }),
    success,
    fail
  )
}

/**
 * @description 修改指定模板
 * @param id 模板ID
 * @param formData FormData 对象
 * @param success 成功回调
 * @param fail 失败回调
 */
export const updateTemplate = async (
  id: string,
  formData: FormData,
  success: RequestCallback,
  fail: RequestCallback
) => {
  console.log('updateTemplate 请求参数:', {
    url: `${currBaseUrl}/update/${id}`,
    dataType: typeof formData,
    isFormData: formData instanceof FormData
  })

  return handleRequest(
    // 使用 upType: DataUpType.file 告知 http 插件以 multipart/form-data 方式发送
    http.put<string>(`${currBaseUrl}/update/${id}`, formData, {
      showLoading: true,
      upType: DataUpType.file
    }),
    success,
    fail
  )
}

/**
 * @description 删除指定模板
 * @param id 模板ID
 * @param success 成功回调
 * @param fail 失败回调
 */
export const deleteTemplate = async (
  id: string,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.delete<string>(`${currBaseUrl}/delete/${id}`, {}, { showLoading: true }),
    success,
    fail
  )
}
