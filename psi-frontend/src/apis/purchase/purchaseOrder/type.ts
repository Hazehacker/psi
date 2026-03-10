// ========== 采购单相关接口类型定义 ==========

// 采购单明细项
export interface PurchaseNoteInfoDTO {
  /** 结算账户 */
  account: string
  /** 实际金额 */
  actual: number
  /** 辅助属性 */
  attr?: string
  /** 商品批次号 */
  batch?: string
  /** 单据费用 */
  cost: number
  /** 备注信息 */
  data: string
  /** 备注信息2 */
  data2: string
  /** 折扣率 */
  discount: number
  /** 折扣额 */
  dsc: number
  /** 所属商品ID */
  goods: string
  /** 商品编号 */
  goodsNumber?: string
  /** 物流信息 */
  logistics: string
  /** 生产日期 */
  mfd: string
  /** 实付金额 */
  money: number
  /** 商品名称 */
  name?: string
  /** 单据金额 */
  noteTotal: number
  /** 单据编号 */
  number: string
  /** 商品数量 */
  nums: number
  /** 关联人员 */
  people: string
  /** 商品单价 */
  price: number
  /** 序列号（JSON数组字符串） */
  serial: string
  /** 关联详情ID */
  source: string
  /** 规格型号 */
  spec?: string
  /** 供应商 */
  supplier: string
  /** 税额 */
  tat: number
  /** 税率 */
  tax: number
  /** 单据日期 */
  time: string
  /** 商品金额 */
  total: number
  /** 价税合计 */
  tpt: number
  /** 商品单位 */
  unit: string
  /** 仓库ID */
  warehouse: number
}

// 采购单主表信息
export interface PurchaseNoteBuyDTO {
  /** 结算账户ID */
  account: string
  /** 实际金额 */
  actual: number
  /** 单据费用 */
  cost: number
  /** 备注信息 */
  data?: string
  /** 单据附件 */
  file?: string
  /** 物流信息 */
  logistics?: string
  /** 实付金额 */
  money: number
  /** 单据编号 */
  number: string
  /** 关联人员ID */
  people: number
  /** 关联单据（BOR，用这个来确定是否为采购订单传来，非零表示生成而来的） */
  source?: number
  /** 供应商ID */
  supplier: number
  /** 单据时间 */
  time: string
  /** 单据金额 */
  total: number
}

// 新增采购单请求参数
export interface PurchaseNoteAddDTO {
  /** 采购单主表信息 */
  purchaseNoteBuyDTO: PurchaseNoteBuyDTO
  /** 采购单明细列表（至少包含一条明细） */
  purchaseNoteInfoDTOS: PurchaseNoteInfoDTO[]
}

