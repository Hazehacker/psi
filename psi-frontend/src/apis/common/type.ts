/**
 * 公用数据接口相关类型在这里声明
 */

/**
 * 字典项
 */
export interface DictItem {
  /** 唯一标识 */
  id: string
  /** 显示文字 */
  label: string
  /** 字典编码 */
  code?: string
}

/**
 * 字典
 */
export interface Dict {
  /** 唯一标识 */
  id: string
  /** 字典名称 */
  name: string
  /** 类型编码 */
  code: string
  /** 字典项 */
  items: Array<DictItem>
}

/**
 * 组织结构树节点
 */
export interface OrgTreeNode {
  /** 节点ID */
  id: string
  /** 节点名称 */
  name: string
  /** 父节点ID */
  parentId?: string
  /** 子节点 */
  children?: OrgTreeNode[]
  /** 其他属性 */
  [key: string]: any
}

/**
 * 收支类别名称列表项（条件）
 */
export interface PaymentCategory {
  /** 类别ID */
  id: string
  /** 类别名称 */
  name: string
  /** 类别编码 */
  code?: string
  /** 类别类型：收入/支出 */
  type?: 'income' | 'expense'
}

/**
 * 商品类别名称列表项（树形结构）
 */
export interface ProductCategory {
  /** 类别ID */
  id: string
  /** 类别名称 */
  name: string
  /** 类别编码 */
  code?: string
  /** 父类别ID */
  parentId?: string
  /** 子类别 */
  children?: ProductCategory[]
  /** 其他属性 */
  [key: string]: any
}

/**
 * 商品选择列表项（条件+分页）
 */
export interface ProductListItem {
  /** 商品ID */
  id: string
  /** 商品名称 */
  name: string
  /** 商品编码 */
  code: string
  /** 商品条码 */
  barcode?: string
  /** 规格型号 */
  spec?: string
  /** 单位 */
  unit?: string
  /** 价格 */
  price?: number
  /** 类别ID */
  categoryId?: string
  /** 类别名称 */
  categoryName?: string
  /** 品牌ID */
  brandId?: string
  /** 品牌名称 */
  brandName?: string
  /** 备注主色 */
  mainColor?: string
  /** 是否有规格明细（子项） */
  hasSpecs?: boolean
  /** 规格明细列表 */
  specs?: ProductSpecItem[]
  /** 其他属性 */
  [key: string]: any
}

/**
 * 商品规格明细项
 */
export interface ProductSpecItem {
  /** 规格ID */
  id: string
  /** 规格名称 */
  specName: string
  /** 商品编码（继承自主商品） */
  code?: string
  /** 规格条码 */
  barcode?: string
  /** 单位 */
  unit?: string
  /** 价格 */
  price?: number
  /** 其他属性 */
  [key: string]: any
}

/**
 * 用户列表项（条件+分页）
 */
export interface UserListItem {
  /** 用户ID（用户标识） */
  id: string
  /** 用户账号 */
  username: string
  /** 用户名称（真实姓名） */
  realName?: string
  /** 手机号码 */
  phone?: string
  /** 邮箱 */
  email?: string
  /** 所属架构ID */
  deptId?: string
  /** 所属架构名称 */
  deptName?: string
  /** 用户角色 */
  roleName?: string
  /** 角色ID列表 */
  roleIds?: string[]
  /** 备注信息 */
  remark?: string
  /** 状态 */
  status?: number
  /** 其他属性 */
  [key: string]: any
}

/**
 * 仓库名称列表项
 */
export interface Warehouse {
  /** 仓库ID（唯一标识） */
  id: string
  /** 仓库名称 */
  name: string
  /** 仓库编号 */
  code?: string
  /** 仓库地址 */
  address?: string
  /** 负责人 */
  manager?: string
  /** 联系电话 */
  phone?: string
  /** 仓库类型 */
  type?: string
  /** 状态 */
  status?: number
  /** 备注 */
  remark?: string
  /** 其他属性 */
  [key: string]: any
}

/**
 * 人员列表项（条件+分页）
 */
export interface PersonnelListItem {
  /** 人员ID */
  id: string
  /** 人员名称（姓名） */
  name: string
  /** 人员编号（工号） */
  employeeNo?: string
  /** 所属组织ID */
  deptId?: string
  /** 所属组织名称 */
  deptName?: string
  /** 人员性别 */
  gender?: string
  /** 联系电话 */
  phone?: string
  /** 联系地址 */
  address?: string
  /** 身份证号 */
  idCard?: string
  /** 备注信息 */
  remark?: string
  /** 岗位 */
  position?: string
  /** 状态 */
  status?: number
  /** 其他属性 */
  [key: string]: any
}

/**
 * 供应商列表项（条件+分页）
 */
export interface SupplierListItem {
  /** 供应商ID */
  id: string
  /** 供应商名称 */
  name: string
  /** 供应商编号 */
  code?: string
  /** 供应商类别 */
  category?: string
  /** 应付款余额 */
  payableBalance?: number
  /** 所属组织 */
  deptName?: string
  /** 所属用户 */
  ownerName?: string
  /** 联系人 */
  contact?: string
  /** 联系电话 */
  phone?: string
  /** 联系地址 */
  address?: string
  /** 备注信息 */
  remark?: string
  /** 邮箱 */
  email?: string
  /** 状态 */
  status?: number
  /** 其他属性 */
  [key: string]: any
}

/**
 * 账户列表项（条件+分页）
 */
export interface AccountListItem {
  /** 账户ID */
  id: string
  /** 账户名称 */
  name: string
  /** 账户编号 */
  accountNo?: string
  /** 所属组织 */
  deptName?: string
  /** 账户余额 */
  balance?: number
  /** 账户类型 */
  type?: string
  /** 开户银行 */
  bank?: string
  /** 备注信息 */
  remark?: string
  /** 状态 */
  status?: number
  /** 其他属性 */
  [key: string]: any
}

/**
 * 客户列表项（条件+分页）
 */
export interface CustomerListItem {
  /** 客户ID */
  id: string
  /** 客户名称 */
  name: string
  /** 客户编号 */
  code?: string
  /** 客户类别 */
  category?: string
  /** 客户等级 */
  level?: string
  /** 应收款余额 */
  receivableBalance?: number
  /** 联系人 */
  contact?: string
  /** 联系电话 */
  phone?: string
  /** 地址 */
  address?: string
  /** 邮箱 */
  email?: string
  /** 所属用户 */
  ownerName?: string
  /** 备注信息 */
  remark?: string
  /** 状态 */
  status?: number
  /** 其他属性 */
  [key: string]: any
}

/**
 * 分页查询参数
 */
export interface PageParams {
  /** 页码 */
  pageIndex: number
  /** 每页条数 */
  pageSize: number
  /** 其他查询条件 */
  [key: string]: any
}

/**
 * 分页响应数据
 */
export interface PageResult<T> {
  /** 总条数 */
  total: number
  /** 总页数 */
  pages?: number
  /** 当前页码 */
  pageIndex: number
  /** 每页条数 */
  pageSize: number
  /** 数据列表 */
  rows: T[]
}
