/* eslint-disable */
// @ts-ignore

export type __ = {
  /** 应付金额 */
  actual?: number;
  /** 应付款金额 */
  actual2?: number;
  /** 备注 */
  data?: string;
  /** 明细列表 */
  details?: __2[];
  /** 优惠金额 */
  discountAmount?: number;
  /** 前端折叠控制标识，默认折叠状态 */
  expanded?: boolean;
  /** 所属组织 */
  frame?: string;
  /** 实付金额 */
  money?: number;
  /** 单据编号 */
  number?: string;
  /** 单据类型 */
  tableType?: string;
  /** 单据时间 */
  time?: string;
  /** 单据金额 */
  total?: number;
};

export type __2 = {
  /** 商品名(所属商品) */
  goods?: string;
  /** 数量 */
  nums?: number;
  /** 单价 */
  price?: number;
};

export type __3 = {
  /** 应收金额 */
  actual?: number;
  /** 应收/付款金额 */
  actual2?: number;
  /** 备注 */
  data?: string;
  /** 明细列表 */
  details?: __4[];
  /** 优惠金额 */
  discountAmount?: number;
  /** 前端折叠控制标识，默认折叠状态 */
  expanded?: boolean;
  /** 所属组织 */
  frame?: string;
  /** 实收金额 */
  money?: number;
  /** 单据编号 */
  number?: string;
  /** 单据类型 */
  tableType?: string;
  /** 单据时间 */
  time?: string;
  /** 单据金额 */
  total?: number;
};

export type __4 = {
  /** 商品名(所属商品) */
  goods?: string;
  /** 数量 */
  nums?: number;
  /** 单价 */
  price?: number;
};

export type CashBankStatementOpenApiExportUsingGetParams = {
  /** 资金账户 */
  account?: string;
  /** 客户 */
  customer?: string;
  /** 结束时间 */
  endTime?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 开始时间 */
  startTime?: string;
  /** 供应商 */
  supplier?: string;
  /** 制单人 */
  user?: string;
};

export type CashBankStatementOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: Blob;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type CashBankStatementUsingGetParams = {
  /** 资金账户 */
  account?: string;
  /** 客户 */
  customer?: string;
  /** 结束时间 */
  endTime?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 开始时间 */
  startTime?: string;
  /** 供应商 */
  supplier?: string;
  /** 制单人 */
  user?: string;
};

export type CashBankStatementUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO6;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type CustomerStatementOpenApiExportUsingGetParams = {
  /** 开始时间 */
  beginTime?: string;
  /** 结束时间 */
  endTime?: string;
  /** 隐藏/显示明细 */
  hideOrShowDetail?: boolean;
  /** 客户名 */
  name?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
};

export type CustomerStatementOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: Blob;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type CustomerStatementUsingGetParams = {
  /** 开始时间 */
  beginTime?: string;
  /** 结束时间 */
  endTime?: string;
  /** 隐藏/显示明细 */
  hideOrShowDetail?: boolean;
  /** 客户名 */
  name?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
};

export type CustomerStatementUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO__2;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type DetailedAccountPayableStatementOpenApiExportUsingGetParams = {
  /** 结束时间 */
  endTime?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 开始时间 */
  startTime?: string;
  /** 供应商 */
  supplier?: string;
  /** 供应商类型 */
  supplierType?: string;
};

export type DetailedAccountPayableStatementOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: string;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type DetailedAccountPayableStatementQueryUsingGetParams = {
  /** 结束时间 */
  endTime?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 开始时间 */
  startTime?: string;
  /** 供应商 */
  supplier?: string;
  /** 供应商类型 */
  supplierType?: string;
};

export type DetailedAccountPayableStatementQueryUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO3;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type DetailedAccountReceivableStatementOpenApiExportUsingGetParams = {
  /** 客户类型 */
  category?: string;
  /** 客户 */
  customer?: string;
  /** 结束时间 */
  endTime?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 开始时间 */
  startTime?: string;
};

export type DetailedAccountReceivableStatementOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: Blob;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type DetailedAccountReceivableStatementUsingGetParams = {
  /** 客户类型 */
  category?: string;
  /** 客户 */
  customer?: string;
  /** 结束时间 */
  endTime?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 开始时间 */
  startTime?: string;
};

export type DetailedAccountReceivableStatementUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO4;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type JsonVOPageDTO_ = {
  /** 状态码 */
  code?: number;
  data?: PageDTO_;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO__ = {
  /** 状态码 */
  code?: number;
  data?: PageDTO__;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO__2 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO__2;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO10 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO10;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO11 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO11;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO12 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO12;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO13 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO13;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO14 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO14;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO15 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO15;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO16 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO16;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO2 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO2;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO3 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO3;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO4 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO4;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO5 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO5;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO6 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO6;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO7 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO7;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO8 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO8;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTO9 = {
  /** 状态码 */
  code?: number;
  data?: PageDTO9;
  /** 提示消息 */
  message?: string;
};

export type JsonVOPageDTOObject_ = {
  /** 状态码 */
  code?: number;
  data?: PageDTOObject_;
  /** 提示消息 */
  message?: string;
};

export enum NucleusEnum {
  'NUMBER_0' = 0,
  'NUMBER_1' = 1,
  'NUMBER_2' = 2,
}

export type INucleusEnum = keyof typeof NucleusEnum;

export enum NucleusEnum2 {
  'NUMBER_0' = 0,
  'NUMBER_1' = 1,
  'NUMBER_2' = 2,
}

export type INucleusEnum2 = keyof typeof NucleusEnum2;

export type OtherIncomeExpenditureOpenApiExportUsingGetParams = {
  /** 收支类别 */
  category?: string;
  /** 单据编号 */
  documentNumber?: string;
  /** 单据类型 */
  documentType?: string;
  /** 结束时间 */
  endTime?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 备注信息 */
  remark?: string;
  /** 结算账户 */
  settlementAccount?: string;
  /** 开始时间 */
  startTime?: string;
};

export type OtherIncomeExpenditureOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: string;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type OtherIncomeExpenditureQueryUsingGetParams = {
  /** 收支类别 */
  category?: string;
  /** 单据编号 */
  documentNumber?: string;
  /** 单据类型 */
  documentType?: string;
  /** 结束时间 */
  endTime?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 备注信息 */
  remark?: string;
  /** 结算账户 */
  settlementAccount?: string;
  /** 开始时间 */
  startTime?: string;
};

export type OtherIncomeExpenditureQueryUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO_;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type PageDTO_ = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin__[];
  /** 总条数 */
  total?: number;
};

export type PageDTO__ = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: __[];
  /** 总条数 */
  total?: number;
};

export type PageDTO__2 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: __3[];
  /** 总条数 */
  total?: number;
};

export type PageDTO10 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_11[];
  /** 总条数 */
  total?: number;
};

export type PageDTO11 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_12[];
  /** 总条数 */
  total?: number;
};

export type PageDTO12 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_13[];
  /** 总条数 */
  total?: number;
};

export type PageDTO13 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_14[];
  /** 总条数 */
  total?: number;
};

export type PageDTO14 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_15[];
  /** 总条数 */
  total?: number;
};

export type PageDTO15 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_16[];
  /** 总条数 */
  total?: number;
};

export type PageDTO16 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_17[];
  /** 总条数 */
  total?: number;
};

export type PageDTO2 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_2[];
  /** 总条数 */
  total?: number;
};

export type PageDTO3 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_3[];
  /** 总条数 */
  total?: number;
};

export type PageDTO4 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_4[];
  /** 总条数 */
  total?: number;
};

export type PageDTO5 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_5[];
  /** 总条数 */
  total?: number;
};

export type PageDTO6 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_7[];
  /** 总条数 */
  total?: number;
};

export type PageDTO7 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_8[];
  /** 总条数 */
  total?: number;
};

export type PageDTO8 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_9[];
  /** 总条数 */
  total?: number;
};

export type PageDTO9 = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Pinyin_10[];
  /** 总条数 */
  total?: number;
};

export type PageDTOObject_ = {
  /** 当前页码 */
  pageIndex?: number;
  /** 每页显示最大数据条数 */
  pageSize?: number;
  /** 总页数 */
  pages?: number;
  /** 当前页数据列表 */
  rows?: Record<string, unknown>[];
  /** 总条数 */
  total?: number;
};

export type Pinyin__ = {
  /** 收支类别 */
  category?: string;
  /** 往来单位 */
  counterparty?: string;
  /** 单据时间 */
  dateTime?: string;
  /** 单据编号 */
  documentNumber?: string;
  /** 单据类型 */
  documentType?: string;
  /** 支出 */
  expenditure?: number;
  /** 所属组织 */
  frame?: string;
  /** 收入 */
  income?: number;
  /** 备注信息 */
  remark?: string;
  /** 结算账户 */
  settlementAccount?: string;
};

export type Pinyin_10 = {
  /** 辅助属性 */
  attr?: string;
  /** 备注信息 */
  data?: string;
  /** 折扣额 */
  dsc?: number;
  /** 所属组织 */
  frame?: string;
  /** 商品名称 */
  name?: string;
  /** 单据编号 */
  number?: string;
  /** 数量 */
  nums?: number;
  /** 单价 */
  price?: number;
  /** 供应商 */
  supplier?: string;
  /** 税额 */
  tat?: number;
  /** 单据时间 */
  time?: string;
  /** 金额 */
  total?: number;
  /** 价税合计 */
  tpt?: number;
  /** 单据类型 */
  type?: 0 | 1;
  /** 单位 */
  unit?: string;
  /** 仓库 */
  warehouse?: string;
};

export type Pinyin_11 = {
  /** 辅助属性 */
  attr?: string;
  /** 商品名称 */
  goodsName?: string;
  /** 关联人员 */
  people?: string;
  /** 采购单金额 */
  purchaseOrderAmount?: number;
  /** 采购单单价 */
  purchaseOrderPrice?: number;
  /** 采购单数量 */
  purchaseOrderQuantity?: number;
  /** 采购退货单金额 */
  purchaseReturnOrderAmount?: number;
  /** 采购退货单单价 */
  purchaseReturnOrderPrice?: number;
  /** 采购退货单数量 */
  purchaseReturnOrderQuantity?: number;
  /** 汇总金额 */
  summaryAmount?: number;
  /** 汇总数量 */
  summaryQuantity?: number;
  /** 供应商 */
  supplier?: string;
  /** 单位 */
  unit?: string;
  /** 用户 */
  user?: string;
  /** 仓库 */
  warehouse?: string;
};

export type Pinyin_12 = {
  /** 到货日期 */
  arrival?: string;
  /** 辅助属性 */
  attr?: string;
  /** 所属组织 */
  frame?: string;
  /** 商品名称 */
  goods?: string;
  /** 商品名称 */
  goodsName?: string;
  /** 备注信息 */
  itemRemark?: string;
  /** 单据编号 */
  number?: string;
  /** 数量 */
  nums?: number;
  /** 单价 */
  price?: string;
  /** 入库状态 */
  state?: 0 | 1 | 2 | 3;
  /** 供应商 */
  supplier?: string;
  /** 供应商名称 */
  supplierName?: string;
  /** 单据时间 */
  time?: string;
  /** 金额 */
  total?: number;
  /** 单位 */
  unit?: string;
  /** 未入库金额 */
  unstockedAmount?: number;
  /** 未入库数量 */
  unstockedQuantity?: number;
  /** 仓库 */
  warehouse?: string;
  /** 仓库名称 */
  warehouseName?: string;
};

export type Pinyin_13 = {
  /** 辅助属性 */
  auxiliaryAttribute?: string;
  /** 成本 */
  cost?: number;
  /** 折扣额 */
  discountAmount?: number;
  /** 毛利润 */
  grossProfit?: number;
  /** 毛利率 */
  grossProfitMargin?: number;
  /** 商品名称 */
  productName?: string;
  /** 商品编号 */
  productNumber?: string;
  /** 数量 */
  quantity?: number;
  /** 规格型号 */
  specificationModel?: string;
  /** 税额 */
  taxAmount?: number;
  /** 价税合计 */
  totalAmountWithTax?: number;
  /** 总成本 */
  totalCost?: number;
  /** 单位 */
  unit?: string;
};

export type Pinyin_14 = {
  /** 应收款余额 */
  accountsReceivableBalance?: number;
  /** 实际金额 */
  actual?: number;
  /** 收款率 */
  collectionRate?: number;
  /** 客户 */
  customer?: string;
  /** 备注信息 */
  data?: string;
  /** 所属组织 */
  frame?: string;
  /** 单据收款 */
  invoiceCollection?: number;
  /** 核销状态 */
  nucleus?: 0 | 1 | 2;
  /** 单据编号 */
  number?: string;
  /** 单据时间 */
  time?: string;
  /** 单据金额 */
  total?: number;
  /** 单据类型 */
  type?: string;
};

export type Pinyin_15 = {
  /** 辅助属性 */
  attr?: string;
  /** 客户 */
  customer?: string;
  /** 备注信息 */
  data?: string;
  /** 折扣额 */
  dsc?: number;
  /** 所属组织 */
  frame?: string;
  /** 商品名称 */
  name?: string;
  /** 单据编号 */
  number?: string;
  /** 数量 */
  nums?: number;
  /** 单价 */
  price?: number;
  /** 税额 */
  tat?: number;
  /** 单据时间 */
  time?: string;
  /** 金额 */
  total?: number;
  /** 价税合计 */
  tpt?: number;
  /** 单据类型 */
  type?: 0 | 1;
  /** 单位 */
  unit?: string;
  /** 仓库 */
  warehouse?: string;
};

export type Pinyin_16 = {
  /** 辅助属性（如颜色、大小） */
  attribute?: string;
  /** 分组依据（按商品分组时为商品名称，按客户分组时为客户名称...） */
  groupField?: string;
  /** 商品名称 */
  productName?: string;
  returnSales?: Pinyin_18;
  sales?: Pinyin_18;
  summary?: Pinyin_6;
  /** 单位 */
  unit?: string;
  /** 仓库名称 */
  warehouse?: string;
};

export type Pinyin_17 = {
  /** 到货日期 */
  arrival?: string;
  /** 辅助属性 */
  attr?: string;
  /** 客户 */
  customer?: string;
  /** 备注信息 */
  data?: string;
  /** 所属组织 */
  frame?: string;
  /** 商品名称 */
  goods?: string;
  /** 单据编号 */
  number?: string;
  /** 数量 */
  nums?: number;
  /** 单价 */
  price?: number;
  /** 出库状态 */
  state?: 0 | 1 | 2 | 3;
  /** 单据时间 */
  time?: string;
  /** 金额 */
  total?: number;
  /** 单位 */
  unit?: string;
  /** 未出库金额 */
  unstockedAmount?: number;
  /** 未出库数量 */
  unstockedQuantity?: number;
  /** 仓库 */
  warehouse?: string;
};

export type Pinyin_18 = {
  /** 金额 */
  amount?: number;
  /** 单价 */
  price?: number;
  /** 数量 */
  quantity?: number;
};

export type Pinyin_2 = {
  /** 金额（字符串格式，可能为空） */
  amount?: string;
  /** 项目名称 */
  projectName?: string;
};

export type Pinyin_3 = {
  /** 应付款余额 */
  accountsPayableBalance?: number;
  /** 增加应付款 */
  accountsPayableIncrease?: number;
  /** 增加预付款 */
  advancePaymentIncrease?: number;
  /** 单据时间 */
  dateTime?: string;
  /** 单据编号 */
  documentNumber?: string;
  /** 单据类型 */
  documentType?: string;
  /** 所属组织 */
  frame?: string;
  /** 备注 */
  remark?: string;
  /** 供应商 */
  supplier?: string;
};

export type Pinyin_4 = {
  /** 应收款余额 */
  balance?: number;
  /** 客户 */
  customer?: string;
  /** 备注 */
  data?: string;
  /** 所属组织 */
  frame?: string;
  /** 增加应收款 */
  increaseAccountsReceivable?: number;
  /** 增加预收款 */
  increaseAdvanceReceipts?: number;
  /** 单据编号 */
  number?: string;
  /** 单据时间 */
  time?: string;
  /** 单据类型 */
  type?: string;
};

export type Pinyin_5 = {
  /** 应收款余额 */
  balanceCol?: number;
  /** 总应收款余额 */
  balanceColSum?: number;
  /** 应付款余额 */
  balancePay?: number;
  /** 总应付款余额 */
  balancePaySum?: number;
  /** 备注信息 */
  data?: string;
  /** 单位名称 */
  name?: string;
  /** 单位编号 */
  number?: string;
  /** 单位类型 */
  type?: string;
};

export type Pinyin_6 = {
  /** 总金额 */
  totalAmount?: number;
  /** 总数量 */
  totalQuantity?: number;
};

export type Pinyin_7 = {
  /** 账户余额 */
  balance?: number;
  /** 往来单位 */
  customer?: string;
  /** 备注 */
  data?: string;
  /** 支出 */
  expend?: number;
  /** 所属组织 */
  frame?: string;
  /** 收入 */
  income?: number;
  /** 账户名称 */
  name?: string;
  /** 单据编号 */
  number?: string;
  /** 单据时间 */
  time?: string;
  /** 单据类型 */
  type?: string;
  /** 制单人 */
  user?: string;
};

export type Pinyin_8 = {
  /** 实际金额 */
  actual?: number;
  /** 应付款余额 */
  balance?: number;
  /** 备注信息 */
  data?: string;
  /** 所属组织 */
  frame?: string;
  /** 核销状态 */
  nucleus?: 0 | 1 | 2;
  /** 单据编号 */
  number?: string;
  /** 单据付款 */
  payment?: number;
  /** 付款率 */
  rate?: string;
  /** 供应商 */
  supplier?: string;
  /** 单据时间 */
  time?: string;
  /** 单据金额 */
  total?: number;
  /** 单据类型 */
  type?: '采购单' | '采购退货单';
};

export type Pinyin_9 = {
  /** 辅助属性 */
  attr?: string;
  /** 成本 */
  cost?: number;
  /** 折扣额 */
  dsc?: number;
  /** 商品名称 */
  goodsName?: string;
  /** 商品编号 */
  goodsNumber?: string;
  /** 数量 */
  nums?: number;
  /** 规格型号 */
  spec?: string;
  /** 税额 */
  tat?: number;
  /** 总成本 */
  totalCost?: number;
  /** 价税合计 */
  tpt?: number;
  /** 单位 */
  unit?: string;
};

export type ProcurementDetailFormOpenApiExportUsingGetParams = {
  /** 备注信息 */
  data?: string;
  /** 结束日期 */
  endTime?: string;
  /** 商品名称 */
  name?: string;
  /** 单据编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 开始日期 */
  startTime?: string;
  /** 供应商 */
  supplier?: string;
  /** 单据类型 */
  type?: 0 | 1;
  /** 仓库 */
  warehouse?: string;
};

export type ProcurementDetailFormOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: Blob;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type ProcurementDetailFormQueryUsingGetParams = {
  /** 备注信息 */
  data?: string;
  /** 结束日期 */
  endTime?: string;
  /** 商品名称 */
  name?: string;
  /** 单据编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 开始日期 */
  startTime?: string;
  /** 供应商 */
  supplier?: string;
  /** 单据类型 */
  type?: 0 | 1;
  /** 仓库 */
  warehouse?: string;
};

export type ProcurementDetailFormQueryUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO9;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type ProcurementPaymentFormOpenApiExportUsingGetParams = {
  /** 结束时间 */
  endTime?: string;
  /** 核销状态 */
  nucleus?: 0 | 1 | 2;
  /** 单据编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 开始时间 */
  startTime?: string;
  /** 供应商 */
  supplier?: string;
  /** 单据类型 */
  type?: '采购单' | '采购退货单';
};

export type ProcurementPaymentFormOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: string;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type ProcurementPaymentFormUsingGetParams = {
  /** 结束时间 */
  endTime?: string;
  /** 核销状态 */
  nucleus?: 0 | 1 | 2;
  /** 单据编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 开始时间 */
  startTime?: string;
  /** 供应商 */
  supplier?: string;
  /** 单据类型 */
  type?: '采购单' | '采购退货单';
};

export type ProcurementPaymentFormUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO7;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type ProfitOpenApiExportUsingGetParams = {
  /** 结束时间 */
  endTime?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 开始时间 */
  startTime?: string;
};

export type ProfitOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: string;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type ProfitUsingGetParams = {
  /** 结束时间 */
  endTime?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 开始时间 */
  startTime?: string;
};

export type ProfitUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO2;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type PurchaseOrderSummaryFormOpenApiExportUsingGetParams = {
  /** 时间范围-结束时间 */
  endTime?: string;
  /** 商品名称 */
  goods?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 关联人员 */
  people?: string;
  /** 时间范围-开始时间 */
  startTime?: string;
  /** 汇总维度：goods-按商品, supplier-按供应商, people-按人员, user-按用户 */
  summaryType?: string;
  /** 供应商 */
  supplier?: string;
  /** 用户 */
  user?: string;
  /** 仓库信息 */
  warehouse?: string;
};

export type PurchaseOrderSummaryFormOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: Blob;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type PurchaseOrderSummaryFormUsingGetParams = {
  /** 时间范围-结束时间 */
  endTime?: string;
  /** 商品名称 */
  goods?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 关联人员 */
  people?: string;
  /** 时间范围-开始时间 */
  startTime?: string;
  /** 汇总维度：goods-按商品, supplier-按供应商, people-按人员, user-按用户 */
  summaryType?: string;
  /** 供应商 */
  supplier?: string;
  /** 用户 */
  user?: string;
  /** 仓库信息 */
  warehouse?: string;
};

export type PurchaseOrderSummaryFormUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO10;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type PurchaseOrderTrackingFormOpenApiExportUsingGetParams = {
  /** 到货开始时间 */
  beginarrival?: string;
  /** 单据开始时间 */
  begintime?: string;
  /** 到货结束时间 */
  endarrival?: string;
  /** 单据结束时间 */
  endtime?: string;
  /** 商品名称 */
  goods?: string;
  /** 订单编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 入库状态 */
  state?: 0 | 1 | 2 | 3;
  /** 供应商 */
  supplier?: string;
  /** 仓库 */
  warehouse?: string;
};

export type PurchaseOrderTrackingFormOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: Blob;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type PurchaseOrderTrackingFormUsingGetParams = {
  /** 到货开始时间 */
  beginarrival?: string;
  /** 单据开始时间 */
  begintime?: string;
  /** 到货结束时间 */
  endarrival?: string;
  /** 单据结束时间 */
  endtime?: string;
  /** 商品名称 */
  goods?: string;
  /** 订单编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 入库状态 */
  state?: 0 | 1 | 2 | 3;
  /** 供应商 */
  supplier?: string;
  /** 仓库 */
  warehouse?: string;
};

export type PurchaseOrderTrackingFormUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO11;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type PurchaseRankingFormOpenApiExportUsingGetParams = {
  /** 单据开始日期 */
  beginTime?: string;
  /** 单据结束日期 */
  endTime?: string;
  /** 商品名称 */
  goodsName?: string;
  /** 商品编号 */
  goodsNumber?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
};

export type PurchaseRankingFormOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: Blob;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type PurchaseRankingFormUsingGetParams = {
  /** 单据开始日期 */
  beginTime?: string;
  /** 单据结束日期 */
  endTime?: string;
  /** 商品名称 */
  goodsName?: string;
  /** 商品编号 */
  goodsNumber?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
};

export type PurchaseRankingFormUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO8;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type SalesDetailFormOpenApiExportUsingGetParams = {
  /** 客户 */
  customer?: string;
  /** 备注信息 */
  data?: string;
  /** 结束日期 */
  endTime?: string;
  /** 商品名称 */
  name?: string;
  /** 单据编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 开始日期 */
  startTime?: string;
  /** 单据类型 */
  type?: 0 | 1;
  /** 仓库 */
  warehouse?: string;
};

export type SalesDetailFormOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: Blob;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type SalesDetailFormQueryUsingGetParams = {
  /** 客户 */
  customer?: string;
  /** 备注信息 */
  data?: string;
  /** 结束日期 */
  endTime?: string;
  /** 商品名称 */
  name?: string;
  /** 单据编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 开始日期 */
  startTime?: string;
  /** 单据类型 */
  type?: 0 | 1;
  /** 仓库 */
  warehouse?: string;
};

export type SalesDetailFormQueryUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO14;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type SalesOrderTrackingFormOpenApiExportUsingGetParams = {
  /** 到货开始时间 */
  beginarrival?: string;
  /** 单据开始时间 */
  begintime?: string;
  /** 客户 */
  customer?: string;
  /** 到货结束时间 */
  endarrival?: string;
  /** 单据结束时间 */
  endtime?: string;
  /** 商品名称 */
  goods?: string;
  /** 订单编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 出库状态 */
  state?: 0 | 1 | 2 | 3;
  /** 仓库 */
  warehouse?: string;
};

export type SalesOrderTrackingFormOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: Blob;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type SalesOrderTrackingFormUsingGetParams = {
  /** 到货开始时间 */
  beginarrival?: string;
  /** 单据开始时间 */
  begintime?: string;
  /** 客户 */
  customer?: string;
  /** 到货结束时间 */
  endarrival?: string;
  /** 单据结束时间 */
  endtime?: string;
  /** 商品名称 */
  goods?: string;
  /** 订单编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 出库状态 */
  state?: 0 | 1 | 2 | 3;
  /** 仓库 */
  warehouse?: string;
};

export type SalesOrderTrackingFormUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO16;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type SalesProfitOpenApiExportUsingGetParams = {
  /** 单据开始时间 */
  beginTime?: string;
  /** 客户 */
  customer?: string;
  /** 明细 */
  data: string;
  /** 单据结束时间 */
  leftTime?: string;
  /** 单据编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 关联人员 */
  people?: string;
  /** 单据类型 */
  type?: string;
  /** 制单人 */
  user?: string;
  dataB?: string;
  frame?: string;
  nucleus?: number;
};

export type SalesProfitOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: string;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type SalesProfitUsingGetParams = {
  /** 单据开始时间 */
  beginTime?: string;
  /** 客户 */
  customer?: string;
  /** 明细 */
  data: string;
  /** 单据结束时间 */
  leftTime?: string;
  /** 单据编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 关联人员 */
  people?: string;
  /** 单据类型 */
  type?: string;
  /** 制单人 */
  user?: string;
  dataB?: string;
  frame?: string;
  nucleus?: number;
};

export type SalesProfitUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTOObject_;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type SalesRankingTableOpenApiExportUsingGetParams = {
  /** 开始时间 */
  begintime?: string;
  /** 结束时间 */
  endtime?: string;
  /** 查询页码 */
  pageIndex?: number;
  /** 查询条数 */
  pageSize?: number;
  /** 笔记本 */
  productName?: string;
  /** 商品编号 */
  productNumber?: string;
};

export type SalesRankingTableOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: Blob;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type SalesRankingTableUsingGetParams = {
  /** 开始时间 */
  begintime?: string;
  /** 结束时间 */
  endtime?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 笔记本 */
  productName?: string;
  /** 商品编号 */
  productNumber?: string;
};

export type SalesRankingTableUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO12;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type SalesReceiptOpenApiExportUsingGetParams = {
  /** 单据开始时间 */
  begintime?: string;
  /** 客户 */
  customer?: string;
  /** 单据结束时间 */
  endtime?: string;
  /** 核销状态 */
  nucleus?: 0 | 1 | 2;
  /** 单据编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 单据类型 */
  type?: string;
};

export type SalesReceiptOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: Blob;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type SalesReceiptUsingGetParams = {
  /** 单据开始时间 */
  begintime?: string;
  /** 客户 */
  customer?: string;
  /** 单据结束时间 */
  endtime?: string;
  /** 核销状态 */
  nucleus?: 0 | 1 | 2;
  /** 单据编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 单据类型 */
  type?: string;
};

export type SalesReceiptUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO13;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type SalesSummaryOpenApiExportUsingGetParams = {
  /** 客户id */
  customerId?: string;
  /** 结束时间 */
  endDate?: string;
  /** 分组维度：product-商品，customer-客户，user-用户，people-人员 */
  groupBy: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 关联人员 */
  peopleId?: string;
  /** 商品名称 */
  productName?: string;
  /** 开始时间 */
  startDate?: string;
  /** 用户id */
  userId?: string;
  /** 仓库id */
  warehouseId?: number;
};

export type SalesSummaryOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: Blob;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type SalesSummaryUsingGetParams = {
  /** 客户id */
  customerId?: string;
  /** 结束时间 */
  endDate?: string;
  /** 分组维度：product-商品，customer-客户，user-用户，people-人员 */
  groupBy: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 关联人员 */
  peopleId?: string;
  /** 商品名称 */
  productName?: string;
  /** 开始时间 */
  startDate?: string;
  /** 用户id */
  userId?: string;
  /** 仓库id */
  warehouseId?: number;
};

export type SalesSummaryUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO15;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export enum StateEnum {
  'NUMBER_0' = 0,
  'NUMBER_1' = 1,
  'NUMBER_2' = 2,
  'NUMBER_3' = 3,
}

export type IStateEnum = keyof typeof StateEnum;

export enum StateEnum2 {
  'NUMBER_0' = 0,
  'NUMBER_1' = 1,
  'NUMBER_2' = 2,
  'NUMBER_3' = 3,
}

export type IStateEnum2 = keyof typeof StateEnum2;

export type SupplierStatementOpenApiExportUsingGetParams = {
  /** 开始时间 */
  beginTime?: string;
  /** 结束时间 */
  endTime?: string;
  /** 隐藏/显示明细 */
  hideOrShowDetail?: boolean;
  /** 供应商名 */
  name?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
};

export type SupplierStatementOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: Blob;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type SupplierStatementUsingGetParams = {
  /** 开始时间 */
  beginTime?: string;
  /** 结束时间 */
  endTime?: string;
  /** 隐藏/显示明细 */
  hideOrShowDetail?: boolean;
  /** 供应商名 */
  name?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
};

export type SupplierStatementUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO__;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export enum TypeEnum {
  '采购单' = '采购单',
  '采购退货单' = '采购退货单',
}

export type ITypeEnum = keyof typeof TypeEnum;

export enum TypeEnum2 {
  'NUMBER_0' = 0,
  'NUMBER_1' = 1,
}

export type ITypeEnum2 = keyof typeof TypeEnum2;

export enum TypeEnum3 {
  'NUMBER_0' = 0,
  'NUMBER_1' = 1,
}

export type ITypeEnum3 = keyof typeof TypeEnum3;

export type UnitArrearsReportFormOpenApiExportUsingGetParams = {
  /** 备注信息 */
  data?: string;
  /** 单位名称 */
  name?: string;
  /** 单位编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 单位类型 */
  type?: string;
};

export type UnitArrearsReportFormOpenApiExportUsingGetResponses = {
  /**
   * OK
   */
  200: string;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};

export type UnitArrearsReportFormUsingGetParams = {
  /** 备注信息 */
  data?: string;
  /** 单位名称 */
  name?: string;
  /** 单位编号 */
  number?: string;
  /** 查询页码 */
  pageIndex: number;
  /** 查询条数 */
  pageSize: number;
  /** 单位类型 */
  type?: string;
};

export type UnitArrearsReportFormUsingGetResponses = {
  /**
   * OK
   */
  200: JsonVOPageDTO5;
  /**
   * Unauthorized
   */
  401: unknown;
  /**
   * Forbidden
   */
  403: unknown;
  /**
   * Not Found
   */
  404: unknown;
};
