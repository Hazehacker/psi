import { useHttp } from '@/plugins/http'
import type { PageDTO, PageQuery } from '../type'

// 定义收支类别相关数据类型
export interface IncomeExpenseCategory {
  id?: string
  name: string
  type: number // 0:收入, 1:支出
  sort: number
  data?: string // 备注信息
}

export interface IncomeExpenseCategoryQuery extends PageQuery {
  name?: string
  type?: number
}

// Java组接口基础路径
const JAVA_BASE_URL = '/j7-sysargs/iet'

/**
 * 获取收支类别列表（分页+条件）
 */
export const getCategoryList = async (params: IncomeExpenseCategoryQuery) => {
  const http = useHttp()
  return http.get<PageDTO<IncomeExpenseCategory>>(`${JAVA_BASE_URL}/queryList`, params)
}

/**
 * 获取收支类别详情
 */
export const getCategoryDetail = async (id: string) => {
  const http = useHttp()
  return http.get<IncomeExpenseCategory>(`${JAVA_BASE_URL}/queryDetail`, { id })
}

/**
 * 新增收支类别
 */
export const addCategory = async (data: Omit<IncomeExpenseCategory, 'id'>) => {
  const http = useHttp()
  return http.post<string>(`${JAVA_BASE_URL}/addIet`, data)
}

/**
 * 修改收支类别
 */
export const updateCategory = async (data: IncomeExpenseCategory) => {
  const http = useHttp()
  return http.put<string>(`${JAVA_BASE_URL}/updateIet`, data)
}

/**
 * 删除收支类别
 */
export const deleteCategory = async (id: string) => {
  const http = useHttp()
  return http.delete<string>(`${JAVA_BASE_URL}/deleteIet`, null, {
    params: { id },
    upType: 0
  })
}

/**
 * 获取收支类别名称列表（用于下拉选择）
 */
export const getCategoryNames = async (type?: number) => {
  const http = useHttp()
  return http.get<Array<{ id: string; name: string }>>(`${JAVA_BASE_URL}/queryByType`, { type })
}
