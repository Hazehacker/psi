/**
 * 收支类别相关类型定义
 */

export interface CategorySelectItem {
  id: string
  name: string
}

export interface CategoryFormData {
  id?: string
  name: string
  type: number
  sort: number
  data?: string
}

export interface CategoryQueryParams {
  name?: string
  type?: number
  pageIndex: number
  pageSize: number
}
