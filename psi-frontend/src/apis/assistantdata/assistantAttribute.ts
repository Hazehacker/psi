import { useHttp } from '@/plugins/http'
import type { PageDTO, PageQuery } from '../type'

// 辅助属性相关数据类型定义
export interface AttributeContent {
  name: string
}

export interface AttributeFormData {
  id?: string
  name: string
  sort: number
  data?: string
  content: AttributeContent[]
}

export interface AttributeQuery extends PageQuery {
  name?: string
  data?: string
}

export interface AttributeListData {
  id: string
  name: string
  sort: number
  data?: string
}

export interface AttributeDetailData {
  id: string
  name: string
  sort: number
  data?: string
  content: AttributeContent[]
}

// Java组接口基础路径
const JAVA_BASE_URL = '/j7-sysargs/attribute'

/**
 * 获取辅助属性列表（分页+条件）
 */
export const getAttributeList = async (params: AttributeQuery) => {
  const http = useHttp()
  return http.get<PageDTO<AttributeListData>>(`${JAVA_BASE_URL}/record`, params)
}

/**
 * 获取辅助属性详情
 */
export const getAttributeDetail = async (id: string) => {
  const http = useHttp()
  return http.get<AttributeDetailData>(`${JAVA_BASE_URL}/get`, { id })
}

/**
 * 新增辅助属性
 */
export const addAttribute = async (data: Omit<AttributeFormData, 'id'>) => {
  const http = useHttp()
  return http.post<string>(JAVA_BASE_URL, data)
}

/**
 * 修改辅助属性
 */
export const updateAttribute = async (data: AttributeFormData) => {
  const http = useHttp()
  return http.put<string>(JAVA_BASE_URL, data)
}

/**
 * 删除辅助属性
 */
export const deleteAttribute = async (id: string) => {
  const http = useHttp()
  return http.delete<string>(JAVA_BASE_URL, null, {
    params: { id },
    upType: 0
  })
}

/**
 * 获取属性名与属性内容列表（分页）
 */
export const getAttributeWithContent = async (params: PageQuery) => {
  const http = useHttp()
  return http.get<PageDTO<AttributeDetailData>>(`${JAVA_BASE_URL}/select`, params)
}
