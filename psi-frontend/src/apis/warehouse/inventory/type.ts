/**
 * PageDTO«InventoryListDTO»
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
 * 库存列表对象
 */
export interface InventoryList {
  /**
   * 商品辅助属性信息
   */
  attrs?: AttrStockDTO[]
  /**
   * 商品品牌
   */
  brand?: string
  /**
   * 商品分类id（内部使用）
   */
  categoryId?: string
  /**
   * 商品分类
   */
  categoryName?: string
  /**
   * 商品条码
   */
  code?: string
  /**
   * 商品仓库库存明细信息
   */
  goodsWarehouses?: WarehouseStockDTO[]
  /**
   * 商品ID（内部使用）
   */
  id?: string
  /**
   * 商品名称
   */
  name?: string
  /**
   * 商品编号
   */
  number?: string
  /**
   * 商品备注
   */
  remark?: string
  /**
   * 规格型号
   */
  spec?: string
  /**
   * 预警阈值
   */
  threshold?: number
  /**
   * 库存数量
   */
  totalStock?: number
  /**
   * 商品单位
   */
  unit?: string
  [property: string]: any
}

/**
 * 商品辅助属性库存对象
 */
export interface AttrStockDTO {
  /**
   * 商品辅助属性ID（内部使用）
   */
  attrId?: string
  /**
   * 商品属性
   */
  attrName?: string
  /**
   * 库存数量
   */
  totalStock?: number
  /**
   * 辅助属性的仓库库存明细
   */
  warehouses?: WarehouseStockDTO[]
  [property: string]: any
}

/**
 * 仓库库存对象
 */
export interface WarehouseStockDTO {
  /**
   * 库存数量
   */
  stockNum?: number
  /**
   * 所属仓库ID（内部使用）
   */
  warehouseId?: string
  /**
   * 所属仓库名称
   */
  warehouseName?: string
  [property: string]: any
}

/**
 * PageDTO«InventoryDetailDTO»
 */
export interface DetailPageDTO<T = any> {
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
 * 商品库存详情数据对象
 */
export interface InventoryDetailDTO {
  /**
   * 操作类型[0:减少|1:增加]
   */
  direction?: number
  /**
   * 组织ID（内部使用）
   */
  frameId?: string
  /**
   * 所属组织
   */
  frameName?: string
  /**
   * 单据ID（内部使用）
   */
  id?: string
  /**
   * 单据编号
   */
  number?: string
  /**
   * 操作数量
   */
  nums?: number
  /**
   * 操作时间
   */
  time?: string
  /**
   * 单据类型
   */
  type?: string
  [property: string]: any
}

export interface InventoryListRequest {
  /**
   * 商品品牌
   */
  goodsBrand?: string
  /**
   * 商品类别
   */
  goodsCategoryId?: string
  /**
   * 商品条码
   */
  goodsCode?: string
  /**
   * 商品名称
   */
  goodsName?: string
  /**
   * 商品编号
   */
  goodsNumber?: string
  /**
   * 商品备注
   */
  goodsRemark?: string
  /**
   * 商品型号
   */
  goodsSpec?: string
  /**
   * 查询页码
   */
  pageIndex?: number
  /**
   * 查询条数
   */
  pageSize?: number
  /**
   * 库存类型：0-常规库存，1-非零库存，2-预警库存
   */
  stockState?: number
  /**
   * 仓库信息
   */
  warehouseId?: string[]
  [property: string]: any
}

export interface InventoryDetailRequest {
  /**
   * 商品ID(点开详情时的查询条件,内部使用)
   */
  goodsId: string
  /**
   * 商品属性(点开详情时的查询条件,内部使用)
   */
  attrName?: string
  /**
   * 单据编号
   */
  documentNumber?: string
  /**
   * 单据类型
   */
  documentTypes?: string[]
  /**
   * 结束日期
   */
  endDate?: string
  /**
   * 查询页码
   */
  pageIndex?: number
  /**
   * 查询条数
   */
  pageSize?: number
  /**
   * 开始日期
   */
  startDate?: string
  /**
   * 仓库ID列表(点开详情时的查询条件,内部使用)
   */
  warehouseIds?: string[]
  [property: string]: any
}
