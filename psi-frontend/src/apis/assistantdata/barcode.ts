// apis/assistantdata/barcode.ts
import { useHttp } from '@/plugins/http'
import type { PageDTO, PageQuery } from '../type'

// 条码相关数据类型定义 - 根据新的接口文档调整
export interface BarcodeDTO {
  data?: string // 备注信息
  info: string // 条码内容
  name: string // 条码名称
  type: number // 条码类型 [0:条形码 | 1:二维码]
}

export interface BarcodeUpdateDTO extends BarcodeDTO {
  id: string // 条码ID
}

// 搜索查询参数 - 不包含分页字段
export interface BarcodeSearchParams {
  name?: string // 条码名称
  info?: string // 条码内容
  type?: number // 条码类型
  data?: string // 备注信息
}

// 完整查询参数 - 包含分页
export interface BarcodeQueryParams extends PageQuery, BarcodeSearchParams {}

export interface BarcodeDetailVO {
  id: string // 条码ID
  name: string // 条码名称
  info: string // 条码内容
  type: number // 条码类型
  data?: string // 备注信息
}

export interface BarcodeListVO {
  id: string // 条码ID
  name: string // 条码名称
  info: string // 条码内容
  type: number // 条码类型
  // 注意：列表接口不返回 data 字段
}

// 导入响应数据类型
export interface CodeVO {
  id: string
  name: string
  info: string
  type: number
  data?: string
}

// 导出请求数据类型
export interface ExportBarcodeItem {
  data?: string // 备注信息
  img?: string // 条码图片
  info?: string // 条码内容
  name?: string // 条码名称
  type?: string // 条码类型（字符串格式）
}

// Java组接口基础路径 - 根据文档，条码管理属于j7组
const JAVA_BASE_URL = '/j7-sysargs/code'

/**
 * 获取条码列表（分页+条件）
 */
export const getBarcodeList = async (params: BarcodeQueryParams) => {
  const http = useHttp()
  return http.get<PageDTO<BarcodeListVO>>(`${JAVA_BASE_URL}/getCodeList`, params, {
    upType: 0 // 表单方式
  })
}

/**
 * 获取条码详情
 */
export const getBarcodeDetail = async (id: string) => {
  const http = useHttp()
  return http.get<BarcodeDetailVO>(
    `${JAVA_BASE_URL}/getCodeDetail`,
    { id },
    {
      upType: 0 // 表单方式
    }
  )
}

/**
 * 获取条码图片
 */
export const getBarcodeImage = async (id: string) => {
  const http = useHttp()
  return http.get<string>(
    `${JAVA_BASE_URL}/getCodeImage`,
    { id },
    {
      upType: 0 // 表单方式
    }
  )
}

/**
 * 新增条码
 */
export const addBarcode = async (data: BarcodeDTO) => {
  const http = useHttp()
  return http.post<string>(`${JAVA_BASE_URL}/addCode`, data, {
    upType: 1 // JSON方式
  })
}

/**
 * 修改条码
 */
export const updateBarcode = async (data: BarcodeUpdateDTO) => {
  const http = useHttp()
  return http.put<string>(`${JAVA_BASE_URL}/updateCode`, data, {
    upType: 1 // JSON方式
  })
}

/**
 * 删除条码（支持批量）
 */
export const deleteBarcodes = async (ids: string[]) => {
  const http = useHttp()
  return http.delete<string[]>(`${JAVA_BASE_URL}/deleteCodes`, ids, {
    upType: 1 // JSON方式
  })
}

/**
 * 导入数据 - 修复参数名
 */
export const importBarcodes = async (file: File) => {
  const http = useHttp()
  const formData = new FormData()
  formData.append('file', file) // 使用正确的参数名
  return http.postWithFile<CodeVO[]>(`${JAVA_BASE_URL}/import`, formData)
}

/**
 * 导出数据 - 修复类型问题
 */
export const exportBarcodes = async (barcodeList: ExportBarcodeItem[]) => {
  const http = useHttp()
  // 使用 instance.request 来获取原始响应，避免类型转换问题
  const response = await http.getInstance().request({
    method: 'post',
    url: `${JAVA_BASE_URL}/export`,
    data: barcodeList,
    responseType: 'blob' // 重要：指定响应类型为 blob
  })
  return response.data
}
