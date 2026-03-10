/**
 * 系统设置 API 类型定义
 * @file src/apis/advancedSettings/systemSettings/type.ts
 * @description 系统设置模块的 API 类型定义
 */

/**
 * 系统配置数据传输对象
 */
export interface SystemDTO {
    /** 配置项 ID */
    id?: string
    /** 配置名称 */
    name?: string
    /** 配置内容 */
    info?: string
    /** 备注信息 */
    remark?: string
  }
  
  /**
   * 保存配置请求数据格式
   */
  export type SaveSystemRequest = SystemDTO[]
  
  /**
   * 获取配置响应格式
   */
  export interface SystemJsonVO {
    /** 状态码，10000 表示成功 */
    code: number
    /** 提示信息 */
    message: string
    /** 数据对象 */
    data?: SystemDTO[]
  }
  
  /**
   * 保存配置响应格式
   */
  export interface SystemRespJsonVO {
    /** 状态码，10000 表示成功 */
    code: number
    /** 提示信息 */
    message: string
    /** 数据对象 */
    data?: number
  }
  
  /**
   * 前端表单数据结构
   */
  export interface SystemSettingsFormData {
    /** 基础信息配置 */
    basic: {
      /** 系统名称 */
      systemName: string
      /** 公司名称 */
      companyName: string
      /** 备案信息 */
      filingInfo: string
      /** 公告信息 */
      notice: string
    }
    /** 功能参数配置 */
    function: {
      /** 自动审核 */
      autoAudit: boolean
      /** 启用税金 */
      enableTax: boolean
      /** 税率 */
      taxRate: number
      /** 允许负库存 */
      allowNegativeInventory: boolean
      /** 计价方法 */
      pricingMethod: string
      /** 核算类型 */
      accountingType: string
      /** 成本规则 */
      costRule: string
      /** 数量小数位数 */
      quantityDecimal: number
      /** 金额小数位数 */
      amountDecimal: number
      /** 报表天数 */
      reportDays: number
    }
    /** 物流配置 */
    logistics: Array<{
      /** 物流标识 */
      key: string
      /** 物流名称 */
      name: string
      /** 是否启用 */
      enabled: boolean
    }>
    /** 商品品牌列表 */
    brand: string[]
    /** 计量单位列表 */
    unit: string[]
    /** 客户类别列表 */
    customerCategory: string[]
    /** 客户等级列表 */
    customerGrade: string[]
    /** 供应商类别列表 */
    supplierCategory: string[]
  }
  
  /**
   * 数据转换器配置项
   */
  export interface ListConfigItem {
    /** 配置键名 */
    key: string
    /** 配置名称 */
    name: string
    /** 配置数据 */
    data: string[]
  }
  
  /**
   * API 响应基础接口
   */
  export interface BaseApiResponse {
    /** 状态码 */
    code: number
    /** 提示信息 */
    message: string
  }
  
  /**
   * 分页请求参数
   */
  export interface PageRequest {
    /** 页码 */
    pageIndex: number
    /** 每页大小 */
    pageSize: number
  }
  
  /**
   * 分页响应数据
   */
  export interface PageResponse<T = any> {
    /** 总条数 */
    total: number
    /** 页码 */
    pageIndex: number
    /** 每页大小 */
    pageSize: number
    /** 数据列表 */
    rows: T[]
  }
  
  /**
   * 系统配置操作结果
   */
  export interface SystemConfigOperationResult {
    /** 操作是否成功 */
    success: boolean
    /** 影响的行数 */
    affectedRows?: number
    /** 错误信息 */
    error?: string
  }
  
  /**
   * 物流配置项
   */
  export interface LogisticsConfig {
    /** 物流标识 */
    key: string
    /** 物流名称 */
    name: string
    /** 是否启用 */
    enabled: boolean
  }
  
  /**
   * 功能参数配置项
   */
  export interface FunctionConfig {
    /** 自动审核 */
    autoAudit: boolean
    /** 启用税金 */
    enableTax: boolean
    /** 税率 */
    taxRate: number
    /** 允许负库存 */
    allowNegativeInventory: boolean
    /** 计价方法 */
    pricingMethod: string
    /** 核算类型 */
    accountingType: string
    /** 成本规则 */
    costRule: string
    /** 数量小数位数 */
    quantityDecimal: number
    /** 金额小数位数 */
    amountDecimal: number
    /** 报表天数 */
    reportDays: number
  }
  
  /**
   * 基础信息配置项
   */
  export interface BasicConfig {
    /** 系统名称 */
    systemName: string
    /** 公司名称 */
    companyName: string
    /** 备案信息 */
    filingInfo: string
    /** 公告信息 */
    notice: string
  }
  
  /**
   * 配置项分类
   */
  export enum ConfigCategory {
    /** 基础信息 */
    BASIC = 'basic',
    /** 功能参数 */
    FUNCTION = 'function',
    /** 物流配置 */
    LOGISTICS = 'logistics',
    /** 列表配置 */
    LIST = 'list'
  }
  
  /**
   * 配置项前缀
   */
  export enum ConfigPrefix {
    /** 基础信息前缀 */
    BASIC = 'basic_',
    /** 功能参数前缀 */
    FUNCTION = 'func_',
    /** 物流配置前缀 */
    LOGISTICS = 'logistics_',
    /** 列表配置前缀 */
    LIST = 'list_'
  }
  
  /**
   * 系统设置错误码
   */
  export enum SystemSettingsErrorCode {
    /** 成功 */
    SUCCESS = 10000,
    /** 参数错误 */
    PARAM_ERROR = 10001,
    /** 配置不存在 */
    CONFIG_NOT_FOUND = 10002,
    /** 保存失败 */
    SAVE_FAILED = 10003,
    /** 系统错误 */
    SYSTEM_ERROR = 10004
  }
  
  /**
   * 系统设置常量
   */
  export const SystemSettingsConstants = {
    /** 默认税率 */
    DEFAULT_TAX_RATE: 13,
    /** 默认小数位数 */
    DEFAULT_DECIMAL_PLACES: 2,
    /** 默认报表天数 */
    DEFAULT_REPORT_DAYS: 365,
    /** 布尔值映射 */
    BOOLEAN_MAP: {
      true: '1',
      false: '0'
    } as const,
    /** 支持的列表配置 */
    SUPPORTED_LIST_CONFIGS: [
      'brand',
      'unit', 
      'customerCategory',
      'customerGrade',
      'supplierCategory'
    ] as const
  } as const
  
  // 导出类型别名
  export type SupportedListConfig = typeof SystemSettingsConstants.SUPPORTED_LIST_CONFIGS[number]
  
  /**
   * 系统设置工具函数类型
   */
  export interface SystemSettingsUtils {
    /**
     * 验证配置数据
     */
    validateConfig: (config: SystemDTO) => boolean
    
    /**
     * 生成配置ID
     */
    generateConfigId: (category: ConfigCategory, key: string) => string
    
    /**
     * 解析配置ID
     */
    parseConfigId: (id: string) => { category: ConfigCategory; key: string } | null
  }
  
  export default {
    ConfigCategory,
    ConfigPrefix,
    SystemSettingsErrorCode,
    SystemSettingsConstants
  }