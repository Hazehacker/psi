/**
 * PageDTO«批次列表数据对象»
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
 * 批次列表数据对象
 */
export interface BatchDTO {
  /**
   * 属性批次列表
   */
  attrBatches?: BatchAttrDTO[]
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
   * 商品备注
   */
  data?: string
  /**
   * 商品仓库库存列表
   */
  goodsWarehouses?: WarehouseStockDTO[]
  /**
   * 商品id（内部使用）
   */
  id?: string
  /**
   * 商品名称
   */
  name?: string
  /**
   * 无属性批次列表
   */
  noAttrBatches?: BatchNumberDTO[]
  /**
   * 商品编号
   */
  number?: string
  /**
   * 保质期（天）
   */
  protect?: number
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
 * 属性批次对象
 */
export interface BatchAttrDTO {
  /**
   * 属性名称
   */
  attrName?: string
  /**
   * 库存数量
   */
  attrStock?: number
  /**
   * 批次列表
   */
  batches?: BatchNumberDTO[]
  [property: string]: any
}

/**
 * 批次号对象
 */
export interface BatchNumberDTO {
  /**
   * 批次详情列表
   */
  batchDocuments?: BatchDocumentDTO[]
  /**
   * 批次号
   */
  batchNumber?: string
  /**
   * 库存数量
   */
  totalStock?: number
  [property: string]: any
}

/**
 * 批次详情对象
 */
export interface BatchDocumentDTO {
  /**
   * 批次ID（内部使用）
   */
  batchId?: string
  /**
   * 过期日期
   */
  expireDate?: string
  /**
   * 库存数量
   */
  nums?: number
  /**
   * 生产日期
   */
  productDate?: string
  /**
   * 仓库名称
   */
  warehouseName?: string
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
 * 详情
 */

/**
 * PageDTO«批次查询详情数据对象»
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
 * 批次查询详情数据对象
 */
export interface BatchDetailDTO {
  /**
   * 所属组织
   */
  frame?: string
  /**
   * 操作类型
   */
  info?: string
  /**
   * 单据编号
   */
  number?: string
  /**
   * 操作数量
   */
  nums?: number
  /**
   * pid所属批次id(查询使用)
   */
  pid?: string
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

/**
 * 批次列表请求参数
 */
export interface BatchListRequest {
  /**
   * 批次号码
   */
  batchNumber?: string
  /**
   * 批次类型：0-常规批次，1-预警批次
   */
  batchState?: number
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
   * 生产日期
   */
  productDate?: string
  /**
   * 仓库信息
   */
  warehouseIds?: string[]
  [property: string]: any
}

/**
 * 批次详情请求参数
 */
export interface BatchDetailRequest {
  /**
   * 结束日期
   */
  endTime?: string
  /**
   * 单据编号
   */
  number?: string
  /**
   * 查询页码
   */
  pageIndex: number
  /**
   * 查询条数
   */
  pageSize: number
  /**
   * 批次pid(点开详情时的查询条件,内部使用)
   */
  pid: string[]
  /**
   * 开始日期
   */
  startTime?: string
  /**
   * 单据类型
   */
  type?: string[]
  /**
   * 仓库信息(内部使用)
   */
  warehouseIds: string[]
  [property: string]: any
}
