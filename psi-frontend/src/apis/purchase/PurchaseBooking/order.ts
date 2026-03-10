import { useHttp, DataUpType } from '@/plugins/http'
import type { ApiResponse, PageDTO } from '@/apis/type'
import { read, utils } from 'xlsx'

// 定义一个功能模块基础url，方便替换
const currBaseUrl = '/j3-purchase/purchase-order'

// 采购订单详情项
export interface PurchaseOrderInfoDTO {
  /** 辅助属性 */
  attr?: string
  /** 备注信息 */
  data?: string
  /** 折扣率 */
  discount?: number
  /** 折扣额 */
  dsc?: number
  /** 所属商品ID */
  goods: string
  /** 数量 */
  nums: number
  /** 单价 */
  price: number
  /** 税额 */
  tat?: number
  /** 税率 */
  tax?: number
  /** 金额 */
  total: number
  /** 价税合计 */
  tpt: number
  /** 单位 */
  unit: string
  /** 仓库ID */
  warehouse: string
}

// 新增采购订单请求参数
export interface PurchaseOrderAddDTO {
  /** 实际金额 */
  actual: number
  /** 到货时间 */
  arrival: string
  /** 备注信息 */
  data?: string
  /** 单据附件 */
  file?: string
  /** 物流信息 */
  logistics?: string
  /** 单据编号 */
  number: string
  /** 关联人员ID */
  people: string
  /** 采购订单详情列表 */
  purchaseOrderInfoDTOList?: PurchaseOrderInfoDTO[]
  /** 关联单据ID */
  source?: string
  /** 供应商ID */
  supplier: string
  /** 单据时间 */
  time: string
  /** 单据金额 */
  total: number
}

// 修改采购订单请求参数
export interface PurchaseOrderUpdateDTO {
  /** 实际金额 */
  actual: number
  /** 到货时间 */
  arrival: string
  /** 备注信息 */
  data?: string
  /** 单据附件 */
  file?: string
  /** 采购订单id编号 */
  id?: string
  /** 物流信息 */
  logistics?: string
  /** 单据编号 */
  number: string
  /** 关联人员ID */
  people: string
  /** 采购订单详情列表 */
  purchaseOrderInfoDTOList?: PurchaseOrderInfoDTO[]
  /** 关联单据ID */
  source?: string
  /** 供应商ID */
  supplier: string
  /** 单据时间 */
  time: string
  /** 单据金额 */
  total: number
}

// 采购订单列表查询请求参数
export interface PurchaseOrderListQuery {
  /** 到货结束时间 */
  arrivalEndTime?: string
  /** 到货开始时间 */
  arrivalStartTime?: string
  /** 备注信息 */
  data?: string
  /** 单据结束时间 */
  documentEndTime?: string
  /** 单据开始时间 */
  documentStartTime?: string
  /** 审核状态 */
  examine?: string
  /** 商品名称 */
  name?: string
  /** 单据编号 */
  number?: string
  /** 查询页码 */
  pageIndex?: number
  /** 查询条数 */
  pageSize?: number
  /** 关联人员 */
  people?: string
  /** 入库状态 */
  state?: string
  /** 供应商 */
  supplier?: string
  /** 制单人 */
  user?: string
}

// 采购订单列表数据对象
export interface PurchaseOrderListItem {
  /** 订单ID */
  id?: number
  /** 实际金额 */
  actual: string | number
  /** 到货日期 */
  arrivalTime?: string
  /** 备注信息 */
  data?: string
  /** 单据时间 */
  documentTime?: string
  /** 审核状态 */
  examine?: string
  /** 所属组织 */
  frame?: string
  /** 商品名称 */
  goods?: string
  /** 单据编号 */
  number?: string
  /** 关联人员 */
  people?: string
  /** 入库状态 */
  state?: string
  /** 供应商 */
  supplier?: string
  /** 单据金额 */
  total: string | number
  /** 制单人 */
  user?: string
}

// 采购订单其他部分详情
export interface PurchaseOrderOtherDetailDTO {
  /** 实际金额 */
  actual?: string | number
  /** 到货日期 */
  arrivalTime?: string
  /** 备注信息 */
  data?: string
  /** 单据附件 */
  file?: string
  /** 物流信息 */
  logistics?: string
  /** 单据编号 */
  number?: string
  /** 关联人员 */
  people?: string
  /** 供应商 */
  supplier?: string
  /** 单据日期 */
  time?: string
  /** 单据金额 */
  total?: string | number
}

// 采购订单详情商品项
export interface PurchaseOrderDetailItem {
  /** 金额 */
  amount?: string | number
  /** 辅助属性 */
  attr?: string
  /** 备注信息 */
  data?: string
  /** 商品编号 */
  goods?: string
  /** 入库数量 */
  handle?: string | number
  /** 商品名称 */
  name?: string
  /** 数量 */
  nums?: string | number
  /** 单价 */
  price?: string | number
  /** 采购订单其他部分详情 */
  purchaseOrderOtherDetailDTO?: PurchaseOrderOtherDetailDTO
  /** 规格型号 */
  spec?: string
  /** 税额 */
  tat?: string | number
  /** 税率 */
  tax?: string | number
  /** 价税合计 */
  tpt?: string | number
  /** 单位 */
  unit?: string
  /** 仓库 */
  warehouse?: string
}

// 采购订单详情响应（根据接口文档，data 可能是单个商品对象或商品数组）
export type PurchaseOrderDetailResponse = PurchaseOrderDetailItem | PurchaseOrderDetailItem[]

// 生成采购单其他部分数据对象
export interface PurchaseNoteOtherGenerateDataDTO {
  /** 结算账户 */
  account?: string
  /** 实际金额 */
  actual?: string
  /** 单据费用 */
  cost?: string
  /** 备注信息 */
  data?: string
  /** 单据附件 */
  file?: string
  /** 物流信息 */
  logistics?: string
  /** 实付金额 */
  money?: string
  /** 单据编号 */
  number?: string
  /** 关联人员 */
  people?: string
  /** 供应商 */
  supplier?: string
  /** 单据日期 */
  time?: string
  /** 单据金额 */
  total?: string
}

// 生成采购单数据对象
export interface PurchaseOrderGenerateDataDTO {
  /** 金额 */
  amount?: string
  /** 辅助属性 */
  attr?: string
  /** 批次号 */
  batch?: string
  /** 备注信息 */
  data?: string
  /** 商品编号 */
  goods?: string
  /** 商品名称 */
  name?: string
  /** 数量 */
  nums?: string
  /** 单价 */
  price?: string
  /** 生成采购单其他部分展示信息 */
  purchaseNoteOtherGenerateDataDTO?: PurchaseNoteOtherGenerateDataDTO
  /** 规格型号 */
  spec?: string
  /** 税额 */
  tat?: string
  /** 税率 */
  tax?: string
  /** 价税合计 */
  tpt?: string
  /** 单位 */
  unit?: string
  /** 仓库 */
  warehouse?: string
}


/**
 * 采购订单 API
 */
export class PurchaseOrderAPI {
  private static http = useHttp()

  /**
   * 新增采购订单
   * 接口路径: /j3-purchase/purchase-order/add
   * 请求方法: POST
   */
  static async addPurchaseOrder(
    data: PurchaseOrderAddDTO
  ): Promise<ApiResponse<string>> {
    try {
      // 验证必填字段
      this.validateRequiredFields(data)
      
      // 清理空值，确保数据格式正确
      const cleanData = this.cleanRequestData(data)
      
      // 验证清理后的数据
      if (!cleanData.purchaseOrderInfoDTOList || cleanData.purchaseOrderInfoDTOList.length === 0) {
        throw new Error('采购订单详情列表不能为空，请确保所有行的商品ID和仓库ID都已填写')
      }
      
      // 调试日志：打印请求数据
      console.log('采购订单请求数据:', JSON.stringify(cleanData, null, 2))
      
      const result = await this.http.post<string>(
        `${currBaseUrl}/add`,
        cleanData,
        {
          upType: DataUpType.json,
          showLoading: true
        }
      )
      return result
    } catch (error: any) {
      console.error('新增采购订单失败:', error)
      // 打印详细的错误信息
      if (error.response) {
        console.error('响应数据:', error.response.data)
        console.error('响应状态:', error.response.status)
        
        // 如果是服务器错误（9994），检查是否是数据库连接超时
        const responseData = error.response.data
        if (responseData?.code === 9994) {
          const errorData = typeof responseData.data === 'string' ? responseData.data : JSON.stringify(responseData.data)
          if (errorData && (
            errorData.includes('JDBC Connection') ||
            errorData.includes('wait_timeout') ||
            errorData.includes('CommunicationsException')
          )) {
            // 数据库连接超时错误已经在 http.ts 中处理，这里只需要重新抛出
            // 但可以添加额外的日志
            console.warn('检测到数据库连接超时，建议联系后端管理员检查数据库连接池配置')
          }
        }
      }
      throw error
    }
  }

  /**
   * 获取采购订单列表（条件+分页）
   * 接口路径: /j3-purchase/purchase-order/list
   * 请求方法: GET
   */
  static async getPurchaseOrderList(
    params: PurchaseOrderListQuery
  ): Promise<ApiResponse<PageDTO<PurchaseOrderListItem>>> {
    try {
      // 构建查询参数，移除 undefined 和空字符串
      const queryParams: Record<string, any> = {}
      
      Object.keys(params).forEach(key => {
        const value = (params as any)[key]
        // 保留数字 0、false 和有效值
        if (value !== undefined && value !== null && value !== '') {
          queryParams[key] = value
        }
      })

      // 确保分页参数存在
      if (!queryParams.pageIndex) {
        queryParams.pageIndex = 1
      }
      if (!queryParams.pageSize) {
        queryParams.pageSize = 10
      }

      const result = await this.http.get<PageDTO<PurchaseOrderListItem>>(
        `${currBaseUrl}/list`,
        queryParams,
        {
          showLoading: true
        }
      )
      return result
    } catch (error: any) {
      console.error('获取采购订单列表失败:', error)
      if (error.response) {
        console.error('响应数据:', error.response.data)
        console.error('响应状态:', error.response.status)
      }
      throw error
    }
  }

  /**
   * 验证必填字段
   */
  private static validateRequiredFields(data: PurchaseOrderAddDTO): void {
    // 验证主对象必填字段
    if (!data.number || (typeof data.number === 'string' && data.number.trim() === '')) {
      throw new Error('单据编号不能为空')
    }
    if (!data.supplier || (typeof data.supplier === 'string' && data.supplier.trim() === '')) {
      throw new Error('供应商不能为空')
    }
    if (!data.people || (typeof data.people === 'string' && data.people.trim() === '')) {
      throw new Error('关联人员不能为空')
    }
    if (!data.time || (typeof data.time === 'string' && data.time.trim() === '')) {
      throw new Error('单据时间不能为空')
    }
    // arrival 可能是字符串或数字，需要先转换为字符串再验证
    const arrivalStr = data.arrival ? String(data.arrival).trim() : ''
    if (!arrivalStr) {
      throw new Error('到货时间不能为空')
    }

    // 验证详情列表
    if (!data.purchaseOrderInfoDTOList || data.purchaseOrderInfoDTOList.length === 0) {
      throw new Error('采购订单详情列表不能为空')
    }

    // 验证详情项必填字段
    data.purchaseOrderInfoDTOList.forEach((item, index) => {
      if (!item.goods || (typeof item.goods === 'string' && item.goods.trim() === '')) {
        throw new Error(`第 ${index + 1} 行的商品ID不能为空`)
      }
      if (!item.warehouse || (typeof item.warehouse === 'string' && item.warehouse.trim() === '')) {
        throw new Error(`第 ${index + 1} 行的仓库ID不能为空`)
      }
      if (!item.unit || (typeof item.unit === 'string' && item.unit.trim() === '')) {
        throw new Error(`第 ${index + 1} 行的单位不能为空`)
      }
    })
  }

  /**
   * 清理请求数据，移除可选字段的空值
   * 过滤掉无效的详情项（goods 或 warehouse 为空的项）
   * 转换日期格式为 YYYY-MM-DD
   * 将 ID 字段转换为字符串格式
   */
  private static cleanRequestData(data: PurchaseOrderAddDTO): any {
    const cleaned: any = {}
    
    // 必填字段列表（即使为空也要保留）
    const requiredFields = ['number', 'supplier', 'people', 'time', 'arrival', 'actual', 'total']
    
    // ID 字段列表（需要转换为字符串）
    const idFields = ['supplier', 'people', 'source']
    const detailIdFields = ['goods', 'warehouse'] // warehouse 是字符串类型，需要转换为字符串
    
    // 处理主对象
    Object.keys(data).forEach(key => {
      const value = (data as any)[key]
      const isRequired = requiredFields.includes(key)
      
      // 处理日期格式转换
      if (key === 'arrival' && value) {
        cleaned[key] = this.formatDate(value)
      } else if (key === 'time' && value) {
        cleaned[key] = this.formatDate(value)
      } else if (idFields.includes(key) && value !== undefined && value !== null) {
        // ID 字段转换为字符串
        cleaned[key] = String(value)
      } else if (isRequired) {
        // 如果是必填字段，直接保留
        cleaned[key] = value
      } else if (key === 'purchaseOrderInfoDTOList' && Array.isArray(value)) {
        // 处理嵌套数组：过滤掉无效项（goods 或 warehouse 为空的项）
        const validItems = value
          .filter(item => {
            // 只保留 goods 和 warehouse 都不为空的项
            const goods = item.goods
            const warehouse = item.warehouse
            const goodsStr = goods !== undefined && goods !== null ? String(goods).trim() : ''
            // warehouse 是字符串类型，检查是否不为空
            const warehouseStr = warehouse !== undefined && warehouse !== null ? String(warehouse).trim() : ''
            return goodsStr !== '' && warehouseStr !== ''
          })
          .map(item => {
            const cleanedItem: any = {}
            Object.keys(item).forEach(itemKey => {
              const itemValue = (item as any)[itemKey]
              // 详情项中的必填字段
              const detailRequiredFields = ['goods', 'nums', 'price', 'total', 'tpt', 'unit', 'warehouse']
              const isDetailRequired = detailRequiredFields.includes(itemKey)
              
              // ID 字段转换为字符串
              if (detailIdFields.includes(itemKey) && itemValue !== undefined && itemValue !== null) {
                cleanedItem[itemKey] = String(itemValue)
              } else if (isDetailRequired || (itemValue !== undefined && itemValue !== null && itemValue !== '')) {
                // 必填字段保留，可选字段移除空值
                cleanedItem[itemKey] = itemValue
              } else if (itemValue === 0 || itemValue === false) {
                // 保留数字 0 和 false
                cleanedItem[itemKey] = itemValue
              }
            })
            return cleanedItem
          })
        
        // 如果过滤后还有有效项，才添加到 cleaned 中
        if (validItems.length > 0) {
          cleaned[key] = validItems
        }
      } else if (value !== undefined && value !== null && value !== '') {
        // 可选字段：移除空字符串和 null/undefined
        cleaned[key] = value
      } else if (value === 0 || value === false || (Array.isArray(value) && value.length === 0)) {
        // 保留数字 0、false、空数组
        cleaned[key] = value
      }
    })
    
    return cleaned
  }

  /**
   * 格式化日期为 YYYY-MM-DD 格式
   */
  private static formatDate(dateValue: string | Date): string {
    if (!dateValue) return ''
    
    try {
      // 如果是 ISO 格式字符串（如 2025-11-17T16:00:00.000Z）
      if (typeof dateValue === 'string') {
        // 如果已经是 YYYY-MM-DD 格式，直接返回
        if (/^\d{4}-\d{2}-\d{2}$/.test(dateValue)) {
          return dateValue
        }
        
        // 如果是 ISO 格式，提取日期部分
        const date = new Date(dateValue)
        if (!isNaN(date.getTime())) {
          const year = date.getFullYear()
          const month = String(date.getMonth() + 1).padStart(2, '0')
          const day = String(date.getDate()).padStart(2, '0')
          return `${year}-${month}-${day}`
        }
      }
      
      // 如果是 Date 对象
      if (dateValue instanceof Date) {
        const year = dateValue.getFullYear()
        const month = String(dateValue.getMonth() + 1).padStart(2, '0')
        const day = String(dateValue.getDate()).padStart(2, '0')
        return `${year}-${month}-${day}`
      }
      
      return String(dateValue)
    } catch (error) {
      console.warn('日期格式转换失败:', dateValue, error)
      return String(dateValue)
    }
  }

  /**
   * 批量审核/反审核采购订单
   * 接口路径: /j3-purchase/purchase-order/audit
   * 请求方法: PUT
   * @param ids 订单ID数组
   * @param type 操作类型：1=审核，0=反审核
   * @returns 响应数据
   */
  static async auditPurchaseOrders(
    ids: number[],
    type: 0 | 1
  ): Promise<ApiResponse<string[]>> {
    try {
      if (!ids || ids.length === 0) {
        throw new Error('订单ID数组不能为空')
      }

      if (type === undefined || type === null) {
        throw new Error('操作类型不能为空')
      }

      // 过滤掉无效的ID
      const validIds = ids
        .map(id => typeof id === 'number' ? id : parseInt(String(id), 10))
        .filter(id => !isNaN(id) && id > 0)

      if (validIds.length === 0) {
        throw new Error('订单ID数组中没有有效的订单ID')
      }

      // 按照接口文档，参数名是 ids 和 type（type: 1=审核，0=反审核）
      const requestData = {
        ids: validIds,
        type: type
      }

      const result = await this.http.put<string[]>(
        `${currBaseUrl}/audit`,
        requestData,
        {
          upType: DataUpType.json,
          showLoading: true
        }
      )
      return result
    } catch (error: any) {
      console.error('批量审核/反审核采购订单失败:', error)
      if (error.response) {
        console.error('响应数据:', error.response.data)
        console.error('响应状态:', error.response.status)
      }
      throw error
    }
  }

  /**
   * 删除采购订单(支持批量)
   * 接口路径: /j3-purchase/purchase-order/delete
   * 请求方法: DELETE
   * @param ids 订单ID数组（接口要求为字符串数组）
   * @returns 响应数据
   */
  static async deletePurchaseOrders(
    ids: number[]
  ): Promise<ApiResponse<string>> {
    try {
      if (!ids || ids.length === 0) {
        throw new Error('订单ID数组不能为空')
      }

      // 过滤掉无效的ID并转换为字符串数组（接口要求）
      const validIds = ids
        .map(id => typeof id === 'number' ? id : parseInt(String(id), 10))
        .filter(id => !isNaN(id) && id > 0)
        .map(id => String(id)) // 转换为字符串数组

      if (validIds.length === 0) {
        throw new Error('订单ID数组中没有有效的订单ID')
      }

      // 按照接口文档，直接传递数组，使用 JSON 格式
      const result = await this.http.delete<string>(
        `${currBaseUrl}/delete`,
        validIds, // 直接传递数组，而不是包装在对象中
        {
          upType: DataUpType.json, // 使用 JSON 格式
          showLoading: true
        }
      )
      return result
    } catch (error: any) {
      console.error('删除采购订单失败:', error)
      if (error.response) {
        console.error('响应数据:', error.response.data)
        console.error('响应状态:', error.response.status)
      }
      throw error
    }
  }

  /**
   * 获取指定采购订单详情
   * 接口路径: /j3-purchase/purchase-order/detail
   * 请求方法: GET
   * @param id 订单ID（字符串类型）
   * @returns 响应数据
   */
  static async getPurchaseOrderDetail(
    id: string | number
  ): Promise<ApiResponse<PurchaseOrderDetailResponse>> {
    try {
      if (id === undefined || id === null || id === '') {
        throw new Error('订单ID不能为空')
      }

      // 将 ID 转换为字符串
      const idStr = String(id)

      const result = await this.http.get<PurchaseOrderDetailResponse>(
        `${currBaseUrl}/detail`,
        { id: idStr },
        {
          showLoading: true
        }
      )
      return result
    } catch (error: any) {
      console.error('获取采购订单详情失败:', error)
      if (error.response) {
        console.error('响应数据:', error.response.data)
        console.error('响应状态:', error.response.status)
      }
      throw error
    }
  }

  /**
   * 获取生成采购单数据
   * 接口路径: /j3-purchase/purchase-order/generate-purchaseData
   * 请求方法: GET
   * @param id 订单ID
   * @returns 响应数据
   */
  static async getGeneratePurchaseData(
    id: string | number
  ): Promise<ApiResponse<PurchaseOrderGenerateDataDTO[]>> {
    try {
      if (id === undefined || id === null || id === '') {
        throw new Error('订单ID不能为空')
      }

      // 将 ID 转换为字符串
      const idStr = String(id)

      console.log('🔍 [获取生成采购单数据] 请求参数:', {
        id: idStr,
        idType: typeof id,
        url: `${currBaseUrl}/generate-purchaseData`
      })

      const result = await this.http.get<PurchaseOrderGenerateDataDTO[]>(
        `${currBaseUrl}/generate-purchaseData`,
        { id: idStr },
        {
          showLoading: true
        }
      )

      // 打印完整的响应数据
      console.log('✅ [获取生成采购单数据] 完整响应:', JSON.stringify(result, null, 2))
      console.log('📊 [获取生成采购单数据] 响应数据结构分析:', {
        code: result.code,
        message: result.message,
        hasData: !!result.data,
        dataType: Array.isArray(result.data) ? 'array' : typeof result.data,
        dataLength: Array.isArray(result.data) ? result.data.length : result.data ? 1 : 0,
        dataContent: result.data
      })

      // 如果是数组，打印数组详情
      if (Array.isArray(result.data)) {
        console.log(`📦 [获取生成采购单数据] 明细行数量: ${result.data.length}`)
        result.data.forEach((item, index) => {
          console.log(`  [明细行 ${index + 1}]:`, {
            goods: item.goods,
            name: item.name,
            nums: item.nums,
            price: item.price,
            warehouse: item.warehouse,
            hasOtherData: !!item.purchaseNoteOtherGenerateDataDTO
          })
        })
      } else if (result.data && typeof result.data === 'object') {
        const singleData = result.data as PurchaseOrderGenerateDataDTO
        console.log('📦 [获取生成采购单数据] 单个明细行:', {
          goods: singleData.goods,
          name: singleData.name,
          nums: singleData.nums,
          price: singleData.price,
          warehouse: singleData.warehouse,
          hasOtherData: !!singleData.purchaseNoteOtherGenerateDataDTO
        })
      } else {
        console.warn('⚠️ [获取生成采购单数据] 响应数据为空!')
      }

      return result
    } catch (error: any) {
      console.error('❌ [获取生成采购单数据] 请求失败:', error)
      if (error.response) {
        console.error('📥 [获取生成采购单数据] 错误响应数据:', JSON.stringify(error.response.data, null, 2))
        console.error('📥 [获取生成采购单数据] 错误响应状态:', error.response.status)
        console.error('📥 [获取生成采购单数据] 错误响应头:', error.response.headers)
      }
      if (error.message) {
        console.error('📥 [获取生成采购单数据] 错误消息:', error.message)
      }
      throw error
    }
  }

  /**
   * 修改采购订单
   * 接口路径: /j3-purchase/purchase-order/update
   * 请求方法: PUT
   * @param data 更新订单数据
   * @returns 响应数据
   */
  static async updatePurchaseOrder(
    data: PurchaseOrderUpdateDTO
  ): Promise<ApiResponse<string>> {
    try {
      // 验证必填字段
      this.validateUpdateRequiredFields(data)
      
      // 清理空值，确保数据格式正确
      const cleanData = this.cleanUpdateRequestData(data)
      
      // 验证清理后的数据
      if (!cleanData.purchaseOrderInfoDTOList || cleanData.purchaseOrderInfoDTOList.length === 0) {
        throw new Error('采购订单详情列表不能为空，请确保所有行的商品ID和仓库ID都已填写')
      }
      
      // 调试日志：打印请求数据
      console.log('修改采购订单请求数据:', JSON.stringify(cleanData, null, 2))
      
      const result = await this.http.put<string>(
        `${currBaseUrl}/update`,
        cleanData,
        {
          upType: DataUpType.json,
          showLoading: true
        }
      )
      return result
    } catch (error: any) {
      console.error('修改采购订单失败:', error)
      // 打印详细的错误信息
      if (error.response) {
        console.error('响应数据:', error.response.data)
        console.error('响应状态:', error.response.status)
      }
      throw error
    }
  }

  /**
   * 验证更新必填字段
   */
  private static validateUpdateRequiredFields(data: PurchaseOrderUpdateDTO): void {
    // 验证主对象必填字段
    if (!data.number || (typeof data.number === 'string' && data.number.trim() === '')) {
      throw new Error('单据编号不能为空')
    }
    if (!data.supplier || (typeof data.supplier === 'string' && data.supplier.trim() === '')) {
      throw new Error('供应商不能为空')
    }
    if (!data.people || (typeof data.people === 'string' && data.people.trim() === '')) {
      throw new Error('关联人员不能为空')
    }
    if (!data.time || (typeof data.time === 'string' && data.time.trim() === '')) {
      throw new Error('单据时间不能为空')
    }
    // arrival 可能是字符串或数字，需要先转换为字符串再验证
    const arrivalStr = data.arrival ? String(data.arrival).trim() : ''
    if (!arrivalStr) {
      throw new Error('到货时间不能为空')
    }

    // 验证详情列表
    if (!data.purchaseOrderInfoDTOList || data.purchaseOrderInfoDTOList.length === 0) {
      throw new Error('采购订单详情列表不能为空')
    }

    // 验证详情项必填字段
    data.purchaseOrderInfoDTOList.forEach((item, index) => {
      if (!item.goods || (typeof item.goods === 'string' && item.goods.trim() === '')) {
        throw new Error(`第 ${index + 1} 行的商品ID不能为空`)
      }
      if (!item.warehouse || (typeof item.warehouse === 'string' && item.warehouse.trim() === '')) {
        throw new Error(`第 ${index + 1} 行的仓库ID不能为空`)
      }
      if (!item.unit || (typeof item.unit === 'string' && item.unit.trim() === '')) {
        throw new Error(`第 ${index + 1} 行的单位不能为空`)
      }
    })
  }

  /**
   * 清理更新请求数据，移除可选字段的空值
   * 过滤掉无效的详情项（goods 或 warehouse 为空的项）
   * 转换日期格式为 YYYY-MM-DD
   * 将 ID 字段转换为字符串格式
   */
  private static cleanUpdateRequestData(data: PurchaseOrderUpdateDTO): any {
    const cleaned: any = {}
    
    // 必填字段列表（即使为空也要保留）
    const requiredFields = ['number', 'supplier', 'people', 'time', 'arrival', 'actual', 'total']
    
    // ID 字段列表（需要转换为字符串）
    const idFields = ['supplier', 'people', 'source', 'id']
    const detailIdFields = ['goods', 'warehouse'] // warehouse 是字符串类型，需要转换为字符串
    
    // 处理主对象
    Object.keys(data).forEach(key => {
      const value = (data as any)[key]
      const isRequired = requiredFields.includes(key)
      
      // 处理日期格式转换
      if (key === 'arrival' && value) {
        cleaned[key] = this.formatDate(value)
      } else if (key === 'time' && value) {
        cleaned[key] = this.formatDate(value)
      } else if (idFields.includes(key) && value !== undefined && value !== null) {
        // ID 字段转换为字符串
        cleaned[key] = String(value)
      } else if (isRequired) {
        // 如果是必填字段，直接保留
        cleaned[key] = value
      } else if (key === 'purchaseOrderInfoDTOList' && Array.isArray(value)) {
        // 处理嵌套数组：过滤掉无效项（goods 或 warehouse 为空的项）
        const validItems = value
          .filter(item => {
            // 只保留 goods 和 warehouse 都不为空的项
            const goods = item.goods
            const warehouse = item.warehouse
            const goodsStr = goods !== undefined && goods !== null ? String(goods).trim() : ''
            // warehouse 是字符串类型，检查是否不为空
            const warehouseStr = warehouse !== undefined && warehouse !== null ? String(warehouse).trim() : ''
            return goodsStr !== '' && warehouseStr !== ''
          })
          .map(item => {
            const cleanedItem: any = {}
            Object.keys(item).forEach(itemKey => {
              const itemValue = (item as any)[itemKey]
              // 详情项中的必填字段
              const detailRequiredFields = ['goods', 'nums', 'price', 'total', 'tpt', 'unit', 'warehouse']
              const isDetailRequired = detailRequiredFields.includes(itemKey)
              
              // ID 字段转换为字符串
              if (detailIdFields.includes(itemKey) && itemValue !== undefined && itemValue !== null) {
                cleanedItem[itemKey] = String(itemValue)
              } else if (isDetailRequired || (itemValue !== undefined && itemValue !== null && itemValue !== '')) {
                // 必填字段保留，可选字段移除空值
                cleanedItem[itemKey] = itemValue
              } else if (itemValue === 0 || itemValue === false) {
                // 保留数字 0 和 false
                cleanedItem[itemKey] = itemValue
              }
            })
            return cleanedItem
          })
        
        // 如果过滤后还有有效项，才添加到 cleaned 中
        if (validItems.length > 0) {
          cleaned[key] = validItems
        }
      } else if (value !== undefined && value !== null && value !== '') {
        // 可选字段：移除空字符串和 null/undefined
        cleaned[key] = value
      } else if (value === 0 || value === false || (Array.isArray(value) && value.length === 0)) {
        // 保留数字 0、false、空数组
        cleaned[key] = value
      }
    })
    
    return cleaned
  }

  /**
   * 导入采购订单数据
   * 接口路径: /j3-purchase/purchase-order/import-bor
   * 请求方法: POST
   * 请求类型: multipart/form-data
   * @param file 要上传的Excel文件
   * @returns 响应数据
   */
  static async importPurchaseOrders(
    file: File
  ): Promise<ApiResponse<string>> {
    try {
      if (!file) {
        throw new Error('文件不能为空')
      }

      // 验证文件类型
      const fileName = file.name.toLowerCase()
      if (!fileName.endsWith('.xlsx') && !fileName.endsWith('.xls')) {
        throw new Error('只支持上传 .xlsx 或 .xls 格式的Excel文件')
      }

      console.log('📤 [导入采购订单] 开始上传文件:', {
        fileName: file.name,
        fileSize: file.size,
        fileType: file.type,
        url: `${currBaseUrl}/import-bor`
      })

      // 在上传前读取Excel内容，用于调试
      try {
        const fileBuffer = await file.arrayBuffer()
        const workbook = read(fileBuffer, { type: 'array' })
        const sheetName = workbook.SheetNames[0]
        const worksheet = workbook.Sheets[sheetName]
        
        // 读取Excel数据（包含表头）
        const jsonData = utils.sheet_to_json(worksheet, { 
          header: 1, // 使用数组格式，第一行是表头
          defval: '' // 空单元格使用空字符串
        })
        
        console.log('📋 [导入采购订单] Excel文件内容预览:')
        console.log('工作表名称:', sheetName)
        console.log('总行数:', jsonData.length)
        console.log('前5行数据:', jsonData.slice(0, 5))
        
        // 如果有数据，打印表头和前几行数据
        if (jsonData.length > 0) {
          console.log('表头（第1行）:', jsonData[0])
          if (jsonData.length > 1) {
            console.log('第2行数据:', jsonData[1])
          }
          if (jsonData.length > 2) {
            console.log('第3行数据:', jsonData[2])
            const row3 = jsonData[2] as any[]
            if (Array.isArray(row3)) {
              console.log('第3行数据类型检查:', row3.map((cell: any, index: number) => ({
                index,
                value: cell,
                type: typeof cell,
                isEmpty: cell === null || cell === undefined || cell === '' || String(cell).trim() === ''
              })))
            }
          }
          if (jsonData.length > 3) {
            console.log('第4行数据:', jsonData[3])
            const row4 = jsonData[3] as any[]
            if (Array.isArray(row4)) {
              console.log('第4行数据类型检查:', row4.map((cell: any, index: number) => ({
                index,
                value: cell,
                type: typeof cell,
                isEmpty: cell === null || cell === undefined || cell === '' || String(cell).trim() === ''
              })))
            }
          }
        }
        
        // 使用对象格式读取（以表头为key）
        const jsonDataWithHeaders = utils.sheet_to_json(worksheet, {
          defval: '',
          raw: false // 不保留原始值，转换为字符串
        })
        console.log('📊 [导入采购订单] Excel数据（对象格式，前3条）:', jsonDataWithHeaders.slice(0, 3))
        
      } catch (parseError) {
        console.warn('⚠️ [导入采购订单] 解析Excel文件失败（不影响上传）:', parseError)
      }

      // 使用 postWithFile 方法上传文件
      const result = await this.http.postWithFile<string>(
        `${currBaseUrl}/import-bor`,
        { file },
        {
          showLoading: true
        }
      )

      console.log('✅ [导入采购订单] 上传成功:', result)
      return result
    } catch (error: any) {
      console.error('❌ [导入采购订单] 上传失败:', error)
      if (error.response) {
        console.error('响应数据:', error.response.data)
        console.error('响应状态:', error.response.status)
        
        // 如果后端返回了详细的错误信息，打印出来
        if (error.response.data?.data) {
          console.error('后端错误详情:', error.response.data.data)
        }
      }
      throw error
    }
  }

  /**
   * 导出简单报表
   * 接口路径: /purchase-order/export-bor
   * 请求方法: GET
   * 请求数据类型: application/x-www-form-urlencoded
   * 响应数据类型: 文件流
   * @param pid 订单ID，字符串类型，必须
   * @returns 响应数据，文件流，包含 data 和 headers
   */
  static async exportSimpleReport(
    pid: string | number
  ): Promise<any> {
    try {
      // 参数验证：pid 必须提供且不能为空
      if (pid === undefined || pid === null || pid === '') {
        throw new Error('订单ID不能为空')
      }

      // 将 ID 转换为字符串（接口要求 string 类型）
      const pidStr = String(pid)

      // 使用 getFile 方法发送 GET 请求下载文件
      // getFile 方法会将 params 作为 query 参数传递，符合接口要求
      const result = await this.http.getFile(
        `${currBaseUrl}/export-bor`,
        { pid: pidStr },
        {
          responseType: 'blob', // 响应类型为 blob（文件流）
          showLoading: true
        }
      )

      // 返回完整的响应对象，包含 data（文件流）和 headers（响应头）
      return result
    } catch (error: any) {
      console.error('❌ [导出简单报表] 导出失败:', error)
      if (error.response) {
        console.error('响应数据:', error.response.data)
        console.error('响应状态:', error.response.status)
      }
      throw error
    }
  }

  /**
   * 导出详细报表
   * @param ids 订单ID数组（string[]）
   * @returns 文件下载响应
   */
  static async exportDetailReport(
    ids: (string | number)[]
  ): Promise<any> {
    try {
      // 参数验证：ids 必须提供且不能为空数组
      if (!ids || ids.length === 0) {
        throw new Error('订单ID列表不能为空')
      }

      // 将 ID 数组转换为字符串数组（接口要求 string[] 类型）
      const idsStr = ids.map(id => String(id))

      // 使用 POST 请求下载文件，需要直接调用 instance.request
      // 因为 getFile 只支持 GET 请求，而导出详细报表接口是 POST
      const result = await (this.http as any).instance.request({
        method: 'post',
        url: `${currBaseUrl}/export/detail`,
        data: idsStr, // POST 请求体数据
        responseType: 'blob', // 响应类型为 blob（文件流）
        showLoading: true, // 显示加载提示
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      })

      // 返回完整的响应对象，包含 data（文件流）和 headers（响应头）
      return {
        data: result.data,
        headers: result.headers
      }
    } catch (error: any) {
      console.error('❌ [导出详细报表] 导出失败:', error)
      if (error.response) {
        console.error('响应数据:', error.response.data)
        console.error('响应状态:', error.response.status)
      }
      throw error
    }
  }

}

