/**
 * PageDTO«盘盈单数据对象»
 */
export interface PageDTO<T = any> {
  /**
   * 当前页码
   */
  pageIndex?: number
  /**
   * 总页数
   */
  pages?: number
  /**
   * 每页显示最大数据条数
   */
  pageSize?: number
  /**
   * 当前页数据列表
   */
  rows?: T[]
  /**
   * 总条数
   */
  total?: number
  [property: string]: any
}

/**
 * 库存盘点DTO
 */
export interface InventoryVerifyDTO {
  /**
   * 商品属性Id
   */
  attrId?: string
  /**
   * 商品id
   */
  goodId?: string
  /**
   * 盘点差异数量
   */
  inventoryDifference?: number
  /**
   * 仓库Id
   */
  warehouseId?: string
  [property: string]: any
}

/**
 * 盘盈单数据对象
 */
export interface OtherInListInfoDTO {
  /**
   * 辅助属性
   */
  attr?: string
  /**
   * 批次号
   */
  batch?: string
  /**
   * 备注信息
   */
  data?: string
  /**
   * 所属商品
   */
  goods?: string
  /**
   * 生产日期
   */
  mfd?: string
  /**
   * 数量
   */
  nums?: number
  /**
   * 成本
   */
  price?: number
  /**
   * 序列号
   */
  serial?: string[]
  /**
   * 总成本
   */
  total?: number
  /**
   * 单位
   */
  unit?: string
  /**
   * 仓库
   */
  warehouse?: string
  [property: string]: any
}

/**
 * 盘亏单数据对象
 */
export interface OtherOutListInfoDTO {
  /**
   * 辅助属性
   */
  attr?: string
  /**
   * 批次号
   */
  batch?: string
  /**
   * 备注信息
   */
  data?: string
  /**
   * 所属商品
   */
  goods?: string
  /**
   * id
   */
  id?: string
  /**
   * 生产日期
   */
  mfd?: string
  /**
   * 数量
   */
  nums?: number
  /**
   * 所属出库单ID
   */
  pid?: string
  /**
   * 成本
   */
  price?: number
  /**
   * 序列号
   */
  serial?: string
  /**
   * 总成本
   */
  total?: number
  /**
   * 单位
   */
  unit?: string
  /**
   * 仓库
   */
  warehouse?: string
  [property: string]: any
}

/**
 * 库存盘点请求参数
 */
export interface InventoryVerifyRequest {
  /**
   * 库存盘点列表
   */
  inventoryVerifyList: InventoryVerifyDTO[]
  [property: string]: any
}

/**
 * 通用响应VO
 */
export interface JsonVO<T = any> {
  /**
   * 状态码
   */
  code?: number
  /**
   * 数据对象
   */
  data?: T
  /**
   * 提示消息
   */
  message?: string
  [property: string]: any
}
