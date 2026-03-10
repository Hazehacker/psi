import { useHttp, DataUpType } from '@/plugins/http'
import type { ApiResponse } from '@/apis/type'
import type {
  PurchaseNoteAddDTO,
  PurchaseNoteBuyDTO,
  PurchaseNoteInfoDTO
} from './type'

// 定义一个功能模块基础url，方便替换
const currBaseUrl = '/j3-purchase/purchase-note'

/**
 * 采购单 API
 */
export class PurchaseNoteAPI {
  private static http = useHttp()

  /**
   * 格式化日期为 YYYY-MM-DD 格式
   * @param dateValue 日期值（字符串或 Date 对象）
   * @returns 格式化后的日期字符串
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
      console.error('日期格式化失败:', error)
      return String(dateValue)
    }
  }

  /**
   * 新增采购单
   * 接口路径: /j3-purchase/purchase-note/add
   * 请求方法: POST
   */
  static async addPurchaseNote(
    data: PurchaseNoteAddDTO
  ): Promise<ApiResponse<string>> {
    try {
      // 验证必填字段
      if (!data.purchaseNoteBuyDTO) {
        throw new Error('采购单主表信息不能为空')
      }
      if (!data.purchaseNoteInfoDTOS || data.purchaseNoteInfoDTOS.length === 0) {
        throw new Error('采购单明细列表不能为空，至少需要一条明细')
      }

      const buyDTO = data.purchaseNoteBuyDTO
      // 验证主表必填字段
      if (!buyDTO.number || (typeof buyDTO.number === 'string' && buyDTO.number.trim() === '')) {
        throw new Error('单据编号不能为空')
      }
      if (!buyDTO.supplier || buyDTO.supplier === 0) {
        throw new Error('供应商不能为空')
      }
      // 验证供应商ID是否在Java int范围内（-2147483648 到 2147483647）
      const supplierIdNum = Number(buyDTO.supplier)
      const INT_MIN = -2147483648
      const INT_MAX = 2147483647
      if (isNaN(supplierIdNum) || supplierIdNum < INT_MIN || supplierIdNum > INT_MAX) {
        throw new Error(
          `供应商ID值超出范围（${buyDTO.supplier}）。Java int类型范围：${INT_MIN} 到 ${INT_MAX}。请联系后端开发人员将supplier字段改为Long类型。`
        )
      }
      if (!buyDTO.people || buyDTO.people === 0) {
        throw new Error('关联人员不能为空')
      }
      if (!buyDTO.account || buyDTO.account === '' || buyDTO.account === '0') {
        throw new Error('结算账户不能为空')
      }
      if (!buyDTO.time || (typeof buyDTO.time === 'string' && buyDTO.time.trim() === '')) {
        throw new Error('单据时间不能为空')
      }

      // 验证明细列表
      data.purchaseNoteInfoDTOS.forEach((item, index) => {
        if (!item.goods || (typeof item.goods === 'string' && item.goods.trim() === '')) {
          throw new Error(`第 ${index + 1} 行的商品ID不能为空`)
        }
        if (!item.warehouse || item.warehouse === 0) {
          throw new Error(`第 ${index + 1} 行的仓库ID不能为空`)
        }
        if (!item.unit || (typeof item.unit === 'string' && item.unit.trim() === '')) {
          throw new Error(`第 ${index + 1} 行的单位不能为空`)
        }
      })

      // 清理和格式化数据
      const cleanData: PurchaseNoteAddDTO = {
        purchaseNoteBuyDTO: {
          account: buyDTO.account,
          actual: Number(buyDTO.actual) || 0,
          cost: Number(buyDTO.cost) || 0,
          data: buyDTO.data || '',
          file: buyDTO.file || '',
          logistics: buyDTO.logistics || '',
          money: Number(buyDTO.money) || 0,
          number: String(buyDTO.number).trim(),
          people: Number(buyDTO.people),
          source: buyDTO.source ? Number(buyDTO.source) : undefined,
          supplier: Number(buyDTO.supplier),
          time: this.formatDate(buyDTO.time),
          total: Number(buyDTO.total) || 0
        },
        purchaseNoteInfoDTOS: data.purchaseNoteInfoDTOS.map(item => ({
          account: String(item.account || ''),
          actual: Number(item.actual) || 0,
          attr: item.attr || '',
          batch: item.batch || '',
          cost: Number(item.cost) || 0,
          data: String(item.data || ''),
          data2: String(item.data2 || ''),
          discount: Number(item.discount) || 0,
          dsc: Number(item.dsc) || 0,
          goods: String(item.goods),
          goodsNumber: item.goodsNumber || '',
          logistics: String(item.logistics || ''),
          mfd: item.mfd ? this.formatDate(item.mfd) : this.formatDate(new Date()),
          money: Number(item.money) || 0,
          name: item.name || '',
          noteTotal: Number(item.noteTotal) || 0,
          number: String(item.number || buyDTO.number).trim(),
          nums: Number(item.nums) || 0,
          people: String(item.people || ''),
          price: Number(item.price) || 0,
          serial: item.serial || '[]',
          source: String(item.source || '0'),
          spec: item.spec || '',
          supplier: String(item.supplier || ''),
          tat: Number(item.tat) || 0,
          tax: Number(item.tax) || 0,
          time: this.formatDate(item.time || buyDTO.time),
          total: Number(item.total) || 0,
          tpt: Number(item.tpt) || 0,
          unit: String(item.unit),
          warehouse: Number(item.warehouse)
        }))
      }

      // 调试日志：打印请求数据
      console.log('采购单请求数据:', JSON.stringify(cleanData, null, 2))

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
      console.error('新增采购单失败:', error)
      // 打印详细的错误信息
      if (error.response) {
        console.error('响应数据:', error.response.data)
        console.error('响应状态:', error.response.status)
      }
      throw error
    }
  }
}

