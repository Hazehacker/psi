/**
 * 公用接口模块
 * 包含各种通用的数据查询接口
 */

import { useHttp } from '@/plugins/http'
import type {
  OrgTreeNode,
  PaymentCategory,
  ProductCategory,
  ProductListItem,
  UserListItem,
  Warehouse,
  PersonnelListItem,
  SupplierListItem,
  AccountListItem,
  CustomerListItem,
  PageParams,
  PageResult
} from './type'

/**
 * 获取组织结构树
 * @returns 组织结构树数据
 */
export async function getOrgTree(): Promise<OrgTreeNode[]> {
  const http = useHttp()
  const res = await http.get<OrgTreeNode[]>('/common/org-tree')
  return res.data || []
}

/**
 * 获取收支类别名称列表（条件查询）
 * @param params 查询条件（如类型、关键字等）
 * @returns 收支类别列表
 */
export async function getPaymentCategories(params?: {
  type?: 'income' | 'expense'
  keyword?: string
}): Promise<PaymentCategory[]> {
  const http = useHttp()
  const res = await http.get<PaymentCategory[]>('/common/payment-categories', params)
  return res.data || []
}

/**
 * 获取商品类别名称树
 * @returns 商品类别树形数据
 */
export async function getProductCategories(): Promise<ProductCategory[]> {
  const http = useHttp()
  const res = await http.get<ProductCategory[]>('/common/product-categories')
  return res.data || []
}

/**
 * 获取商品选择列表（条件+分页）
 * @param params 分页参数和查询条件
 * @returns 商品分页数据
 */
export async function getProductList(params: PageParams): Promise<PageResult<ProductListItem>> {
  const http = useHttp()
  const res = await http.get<PageResult<ProductListItem>>('/common/products', params)
  return res.data || {
    total: 0,
    pageIndex: params.pageIndex,
    pageSize: params.pageSize,
    rows: []
  }
}

/**
 * 获取用户列表（条件+分页）
 * @param params 分页参数和查询条件
 * @returns 用户分页数据
 */
export async function getUserList(params: PageParams): Promise<PageResult<UserListItem>> {
  const http = useHttp()
  const res = await http.get<PageResult<UserListItem>>('/common/users', params)
  return res.data || {
    total: 0,
    pageIndex: params.pageIndex,
    pageSize: params.pageSize,
    rows: []
  }
}

/**
 * 获取仓库名称列表
 * @param params 查询参数（可选）
 * @returns 仓库列表
 */
export async function getWarehouseList(params?: Record<string, any>): Promise<Warehouse[]> {
  const http = useHttp()
  const res = await http.get<Warehouse[]>('/common/warehouses', params || {})
  return res.data || []
}

/**
 * 获取人员列表（条件+分页）
 * @param params 分页参数和查询条件
 * @returns 人员分页数据
 */
export async function getPersonnelList(params: PageParams): Promise<PageResult<PersonnelListItem>> {
  const http = useHttp()
  const res = await http.get<PageResult<PersonnelListItem>>('/common/personnel', params)
  return res.data || {
    total: 0,
    pageIndex: params.pageIndex,
    pageSize: params.pageSize,
    rows: []
  }
}

/**
 * 获取供应商列表（条件+分页）
 * @param params 分页参数和查询条件
 * @returns 供应商分页数据
 */
export async function getSupplierList(params: PageParams): Promise<PageResult<SupplierListItem>> {
  const http = useHttp()
  const res = await http.get<PageResult<SupplierListItem>>('/common/suppliers', params)
  return res.data || {
    total: 0,
    pageIndex: params.pageIndex,
    pageSize: params.pageSize,
    rows: []
  }
}

/**
 * 获取账户列表（条件+分页）
 * @param params 分页参数和查询条件
 * @returns 账户分页数据
 */
export async function getAccountList(params: PageParams): Promise<PageResult<AccountListItem>> {
  const http = useHttp()
  const res = await http.get<PageResult<AccountListItem>>('/common/accounts', params)
  return res.data || {
    total: 0,
    pageIndex: params.pageIndex,
    pageSize: params.pageSize,
    rows: []
  }
}

/**
 * 获取客户列表（条件+分页）
 * @param params 分页参数和查询条件
 * @returns 客户分页数据
 */
export async function getCustomerList(params: PageParams): Promise<PageResult<CustomerListItem>> {
  const http = useHttp()
  const res = await http.get<PageResult<CustomerListItem>>('/common/customers', params)
  return res.data || {
    total: 0,
    pageIndex: params.pageIndex,
    pageSize: params.pageSize,
    rows: []
  }
}

