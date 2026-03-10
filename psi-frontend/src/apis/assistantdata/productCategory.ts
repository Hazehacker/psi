import { useHttp } from '@/plugins/http'
import type { PageDTO, PageQuery } from '../type'

// 定义商品类别相关数据类型
export interface ProductCategory {
  id?: string
  name: string
  pid: string // 父类别id
  sort: number
  data?: string // 备注信息
}

export interface ProductCategoryQuery extends PageQuery {
  name?: string
}

export interface CategoryTreeNode {
  data: string
  id: string
  name: string
  pid: string
  sort: number
  tnChildren?: CategoryTreeNode[]
}

// Java组接口基础路径
const JAVA_BASE_URL = '/j7-sysargs/category'

/**
 * 获取商品类别列表（分页+条件）
 */
export const getCategoryList = async (params: ProductCategoryQuery) => {
  const http = useHttp()
  return http.get<PageDTO<ProductCategory>>(`${JAVA_BASE_URL}/queryAll`, params)
}

/**
 * 获取商品类别详情
 */
export const getCategoryDetail = async (id: string) => {
  const http = useHttp()
  return http.get<ProductCategory>(`${JAVA_BASE_URL}/queryById`, { id })
}

/**
 * 获取商品类别树
 */
export const getCategoryTree = async (id: string = '0') => {
  const http = useHttp()
  return http.get<CategoryTreeNode>(`${JAVA_BASE_URL}/queryTree`, { id })
}

/**
 * 新增商品类别
 */
export const addCategory = async (data: Omit<ProductCategory, 'id'>) => {
  const http = useHttp()
  return http.post<string>(`${JAVA_BASE_URL}/add`, data)
}

/**
 * 修改商品类别
 */
export const updateCategory = async (data: ProductCategory) => {
  const http = useHttp()
  return http.put<string>(`${JAVA_BASE_URL}/update`, data)
}

/**
 * 删除商品类别
 */
export const deleteCategory = async (id: string) => {
  const http = useHttp()
  return http.delete<string>(`${JAVA_BASE_URL}/delete`, null, {
    params: { id },
    upType: 0
  })
}
