/**
 * 系统设置 API 接口
 * @file src/apis/advancedSettings/systemSettings/index.ts
 */

import { useHttp } from '@/plugins/http'
import { useUserStore } from '@/stores/user'
import type { SystemJsonVO, SaveSystemRequest, SystemRespJsonVO, SystemDTO } from './type'

const currBaseUrl = '/c1-systemparameters/sys'
/**
 * 通用安全请求封装函数（局部修复 Token 过期问题）
 * @param requestFn 实际的请求函数（比如 http.get 或 http.put）
 * @param args 参数
 * @returns Promise<T>
 */
async function safeRequest<T>(requestFn: Function, ...args: any[]): Promise<T> {
  const store = useUserStore()
  const http = useHttp()

  try {
    // 第一次请求
    return await requestFn(...args)
  } catch (err: any) {
    // 判断是否是 token 失效
    const msg = err?.message || err?.response?.data?.message || ''
    const code = err?.response?.data?.code

    if (
      code === 401 ||
      msg.includes('Token: check fail') ||
      msg.includes('Jwt expired') ||
      msg.includes('token')
    ) {
      console.warn('检测到 Token 失效，尝试刷新...')
      try {
        await store.reloadToken()
        console.log('Token 刷新成功，重新发起请求')
        return await requestFn(...args) // 🔁 重试请求
      } catch (refreshErr) {
        console.error('刷新 token 失败，请重新登录')
        store.resetSaveData()
        throw new Error('登录已过期，请重新登录')
      }
    }

    // 其他错误直接抛出
    throw err
  }
}

/**
 * 获取系统配置信息
 * GET /sys/get-msg
 */
export async function getSystemSettings(): Promise<SystemJsonVO> {
  const http = useHttp()
  return safeRequest<SystemJsonVO>(() =>
    http.get<SystemJsonVO>(currBaseUrl + '/get-msg')
  )
}

/**
 * 保存系统配置信息
 * PUT /sys/put-msg
 */
export async function saveSystemSettings(configs: SaveSystemRequest): Promise<SystemRespJsonVO> {
  const http = useHttp()
  
  // 修改为UTF-8编码
  return safeRequest<SystemRespJsonVO>(() =>
    http.put<SystemRespJsonVO>(currBaseUrl + '/put-msg', configs, {
      headers: {
        'Content-Type': 'application/json', 
        'Authorization': `Bearer ${useUserStore().getToken}`
      }
    })
  )
}

/**
 * 数据转换工具类（修复ID问题和数据结构）
 */
export class SystemConfigTransformer {
  // 存储原始配置映射，用于保存时使用原有ID
  private static originalConfigMap: Map<string, SystemDTO> = new Map()

  static initializeWithOriginalConfigs(configs: SystemDTO[]) {
    this.originalConfigMap.clear()
    configs.forEach(config => {
      if (config.name) {
        this.originalConfigMap.set(config.name, config)
      }
    })
  }

  static transformToApiFormat(formData: any): SaveSystemRequest {
    const configs: SaveSystemRequest = []
    
    // 基础信息 - 使用原有ID
    const basicConfigs = [
      { name: 'name', value: formData.basic.systemName, remark: '软件名称' },
      { name: 'company', value: formData.basic.companyName, remark: '公司名称' },
      { name: 'icp', value: formData.basic.filingInfo, remark: '备案信息' },
      { name: 'notice', value: formData.basic.notice, remark: '公告信息' }
    ]

    basicConfigs.forEach(item => {
      const originalConfig = this.originalConfigMap.get(item.name)
      configs.push({
        id: originalConfig?.id || this.generateId(),
        name: item.name,
        info: JSON.stringify(item.value || ''),
        remark: item.remark
      })
    })
    
    // 功能参数 - 使用原有ID
    const func = formData.function
    const functionData = {
      examine: func.autoAudit,
      tax: func.enableTax,
      rate: func.taxRate || 0,
      contain: func.allowNegativeInventory,
      overflow: false,
      valuation: func.pricingMethod || 'base',
      branch: func.accountingType === 'warehouse' ? 0 : 1,
      rule: this.mapCostRule(func.costRule),
      digit: {
        nums: func.quantityDecimal || 0,
        money: func.amountDecimal || 2
      },
      days: func.reportDays?.toString() || '30'
    }
    
    const funOriginalConfig = this.originalConfigMap.get('fun')
    configs.push({
      id: funOriginalConfig?.id || this.generateId(),
      name: 'fun',
      info: JSON.stringify(functionData),
      remark: '功能参数'
    })
    
    // 物流配置 - 使用原有ID
    if (formData.logistics && Array.isArray(formData.logistics)) {
      const logisticsData = formData.logistics.map((logistics: any) => ({
        key: logistics.key,
        name: logistics.name,
        enable: logistics.enabled
      }))
      
      const logisticsOriginalConfig = this.originalConfigMap.get('logistics')
      configs.push({
        id: logisticsOriginalConfig?.id || this.generateId(),
        name: 'logistics',
        info: JSON.stringify(logisticsData),
        remark: '物流配置'
      })
    }
    
    // 列表配置 - 使用原有ID
    const listConfigs = [
      { key: 'brand', name: 'brand', data: formData.brand, remark: '商品品牌' },
      { key: 'unit', name: 'unit', data: formData.unit, remark: '计量单位' },
      { key: 'customerCategory', name: 'crCategory', data: formData.customerCategory, remark: '客户类别' },
      { key: 'customerGrade', name: 'crGrade', data: formData.customerGrade, remark: '客户等级' },
      { key: 'supplierCategory', name: 'srCategory', data: formData.supplierCategory, remark: '供应商类别' }
    ]
    
    listConfigs.forEach(config => {
      if (config.data && Array.isArray(config.data)) {
        const originalConfig = this.originalConfigMap.get(config.name)
        configs.push({
          id: originalConfig?.id || this.generateId(),
          name: config.name,
          info: JSON.stringify(config.data),
          remark: config.remark
        })
      }
    })
    
    console.log('转换后的API数据:', configs)
    return configs
  }

  static transformFromApiFormat(configs: SystemDTO[]): any {
    // 初始化原始配置映射
    this.initializeWithOriginalConfigs(configs)

    const formData: any = {
      basic: {
        systemName: '',
        companyName: '',
        filingInfo: '',
        notice: ''
      },
      function: {
        autoAudit: false,
        enableTax: false,
        taxRate: 0,
        allowNegativeInventory: false,
        pricingMethod: 'base',
        accountingType: 'warehouse',
        costRule: 'def',
        quantityDecimal: 0,
        amountDecimal: 2,
        reportDays: 30
      },
      logistics: [],
      brand: [],
      unit: [],
      customerCategory: [],
      customerGrade: [],
      supplierCategory: []
    }

    if (!Array.isArray(configs)) {
      console.warn('配置数据不是数组格式:', configs)
      return formData
    }

    console.log('开始解析API数据，共', configs.length, '条配置')

    configs.forEach((config, index) => {
      const { name, info } = config
      
      if (!name || info === undefined || info === null) {
        console.warn(`配置项${index}缺少name或info，跳过`)
        return
      }

      try {
        let parsedInfo
        if (typeof info === 'string') {
          try {
            parsedInfo = JSON.parse(info)
          } catch (jsonError) {
            console.warn(`配置项${index} name:${name} JSON解析失败，使用原始值:`, info)
            parsedInfo = info
          }
        } else {
          parsedInfo = info
        }

        console.log(`配置项${index} name:${name}, 解析后值:`, parsedInfo)

        switch (name) {
          case 'name':
            formData.basic.systemName = String(parsedInfo || '')
            break
          case 'company':
            formData.basic.companyName = String(parsedInfo || '')
            break
          case 'icp':
            formData.basic.filingInfo = String(parsedInfo || '')
            break
          case 'notice':
            formData.basic.notice = String(parsedInfo || '')
            break
          case 'fun':
            if (typeof parsedInfo === 'object') {
              formData.function.autoAudit = Boolean(parsedInfo.examine)
              formData.function.enableTax = Boolean(parsedInfo.tax)
              formData.function.taxRate = Number(parsedInfo.rate) || 0
              formData.function.allowNegativeInventory = Boolean(parsedInfo.contain)
              formData.function.pricingMethod = String(parsedInfo.valuation || 'base')
              formData.function.accountingType = parsedInfo.branch === 0 ? 'warehouse' : 'headquarters'
              formData.function.costRule = this.reverseMapCostRule(parsedInfo.rule)
              
              if (parsedInfo.digit && typeof parsedInfo.digit === 'object') {
                formData.function.quantityDecimal = Number(parsedInfo.digit.nums) || 0
                formData.function.amountDecimal = Number(parsedInfo.digit.money) || 2
              }
              
              formData.function.reportDays = Number(parsedInfo.days) || 30
            }
            break
          case 'logistics':
            if (Array.isArray(parsedInfo)) {
              formData.logistics = parsedInfo.map((item: any) => ({
                key: item.key,
                name: item.name,
                enabled: Boolean(item.enable)
              }))
            }
            break
          case 'brand':
            if (Array.isArray(parsedInfo)) {
              formData.brand = parsedInfo
            }
            break
          case 'unit':
            if (Array.isArray(parsedInfo)) {
              formData.unit = parsedInfo
            }
            break
          case 'crCategory':
            if (Array.isArray(parsedInfo)) {
              formData.customerCategory = parsedInfo
            }
            break
          case 'crGrade':
            if (Array.isArray(parsedInfo)) {
              formData.customerGrade = parsedInfo
            }
            break
          case 'srCategory':
            if (Array.isArray(parsedInfo)) {
              formData.supplierCategory = parsedInfo
            }
            break
        }
      } catch (e) {
        console.error(`配置项${index} 处理失败:`, name, info, e)
      }
    })
    
    console.log('最终转换的表单数据:', formData)
    return formData
  }

  private static generateId(): string {
    return Math.random().toString(36).substr(2, 9)
  }

  private static mapCostRule(rule: string): string {
    const ruleMap: Record<string, string> = {
      'balance': 'def',
      'attr': 'aux',
      'batch': 'batch',
      'aab': 'attr_batch'
    }
    return ruleMap[rule] || 'def'
  }

  private static reverseMapCostRule(rule: string): string {
    const ruleMap: Record<string, string> = {
      'def': 'balance',
      'aux': 'attr',
      'batch': 'batch',
      'attr_batch': 'aab'
    }
    return ruleMap[rule] || 'balance'
  }
}

export default {
  getSystemSettings,
  saveSystemSettings,
  SystemConfigTransformer
}
