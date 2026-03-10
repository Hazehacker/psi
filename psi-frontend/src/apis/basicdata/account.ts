import { useHttp } from '@/plugins/http'
import type { PageQuery } from '@/apis/type'

// 资金账户基础接口 - 根据API定义
export interface FundsDTO {
  name: string
  number: string
  frame: number
  time: number
  balance: number
  data: string
}

// 账户详情接口
export interface AccountDetailDTO {
  accountname: string
  accountnumber: string
  frame: string
  time: string
  initial: number
  balance: number
  data: string
  id: string
}

// 添加账户接口
export interface AddAccountDTO {
  accountname: string
  accountnumber: string
  frame: string
  time: string
  initial: number
  balance: number
  data: string
}

// API响应格式
export interface ApiResponse<T = any> {
  code: number
  data: T
  message: string
}

// 分页响应格式
export interface PageResponse<T> {
  pageIndex: number
  pageSize: number
  total: number
  pages: number
  rows: T[]
}

// 资金账户查询参数
export interface AccountQuery extends PageQuery {
  name?: string
  number?: string
  data?: string // 搜索关键词
}

export class AccountAPI {
  private static http = useHttp()

  /**
   * 获取资金账户列表 - 使用/c2-sysbase/funds/get接口
   * 注意：http.ts的get方法默认使用form格式提交参数，通过params属性传递
   */
  static async getAccountList(params: AccountQuery) {
    try {
      // 确保参数格式正确，转换为string类型避免JSON解析错误
      const queryParams = {
        pageIndex: params.pageIndex || 1,
        pageSize: params.pageSize || 10,
        name: params.name || '',
        number: params.number || ''
      }
      
      // GET请求直接传递参数，不需要嵌套在params中
      const response = await this.http.get('/c2-sysbase/funds/get', queryParams)
      
      // 验证响应的有效性
      if (response === null || response === undefined) {
        throw new Error('获取资金账户列表：响应为空')
      }
      
      return response
    } catch (error) {
      console.error('获取资金账户列表失败:', error)
      // 包装错误，提供更详细的信息
      const errorMessage = error instanceof Error ? error.message : '未知错误'
      throw new Error(`获取资金账户列表失败: ${errorMessage}`)
    }
  }

  /**
   * 获取资金账户详情 - 使用/c2-sysbase/funds/query-fund-detail接口
   * 注意：http.ts的get方法默认使用form格式提交参数
   */
  static async getAccountDetail(id: number | string) {
    try {
      // 确保id为字符串类型，防止参数格式错误
      const validId = String(id).trim()
      if (!validId) {
        throw new Error('账户ID不能为空')
      }
      
      // GET请求直接传递id参数，不需要嵌套在params中
      const response = await this.http.get('/c2-sysbase/funds/query-fund-detail', { id: validId })
      
      // 验证响应的有效性
      if (response === null || response === undefined) {
        throw new Error('获取资金账户详情：响应为空')
      }
      
      return response
    } catch (error) {
      console.error('获取资金账户详情失败:', error)
      // 包装错误，提供更详细的信息
      const errorMessage = error instanceof Error ? error.message : '未知错误'
      throw new Error(`获取资金账户详情失败: ${errorMessage}`)
    }
  }

  /**
   * 新增资金账户 - 使用/c2-sysbase/funds/add-account接口
   * 注意：http.ts的post方法默认使用JSON格式提交数据
   */
  static async addAccount(data: Partial<AddAccountDTO>) {
    try {
      // 确保数据格式正确，转换为API需要的字段名和类型
      const accountData: AddAccountDTO = {
        accountname: data.accountname || '',
        accountnumber: data.accountnumber || '',
        frame: data.frame || '',
        time: data.time || '',
        initial: data.initial || 0,
        balance: data.balance || 0,
        data: data.data || ''
      }
      
      // POST请求直接传递数据，不需要嵌套
      const response = await this.http.post('/c2-sysbase/funds/add-account', accountData)
      
      // 验证响应的有效性
      if (response === null || response === undefined) {
        throw new Error('新增资金账户：响应为空')
      }
      
      return response
    } catch (error) {
      console.error('新增资金账户失败:', error)
      // 包装错误，提供更详细的信息
      const errorMessage = error instanceof Error ? error.message : '未知错误'
      throw new Error(`新增资金账户失败: ${errorMessage}`)
    }
  }

  /**
   * 更新资金账户 - 根据API文档调整接口
   * 注意：确保数据格式正确，防止JSON解析错误
   */
  static async updateAccount(id: number | string, data: Partial<AddAccountDTO>) {
    try {
      // 确保id为字符串类型
      const validId = String(id).trim()
      if (!validId) {
        throw new Error('账户ID不能为空')
      }
      
      // 确保数据格式正确
      const updateData = {
        id: validId,
        accountname: data.accountname || '',
        accountnumber: data.accountnumber || '',
        frame: data.frame || '',
        time: data.time || '',
        initial: data.initial || 0,
        balance: data.balance || 0,
        data: data.data || ''
      }
      
      // PUT请求直接传递数据
      const response = await this.http.put('/c2-sysbase/funds/update-account', updateData)
      
      // 验证响应的有效性
      if (response === null || response === undefined) {
        throw new Error('更新资金账户：响应为空')
      }
      
      return response
    } catch (error) {
      console.error('更新资金账户失败:', error)
      // 包装错误，提供更详细的信息
      const errorMessage = error instanceof Error ? error.message : '未知错误'
      throw new Error(`更新资金账户失败: ${errorMessage}`)
    }
  }

  /**
   * 删除资金账户 - 使用/c2-sysbase/funds/delete接口
   * 注意：http.ts的delete方法默认使用JSON格式提交数据，不要使用params嵌套
   */
  static async deleteAccount(id: number | string) {
    try {
      // 确保id为字符串类型
      const validId = String(id).trim()
      if (!validId) {
        throw new Error('账户ID不能为空')
      }
      
      // DELETE请求直接传递id参数，不需要嵌套在params中
      const response = await this.http.delete('/c2-sysbase/funds/delete', { id: validId })
      
      // 验证响应的有效性
      if (response === null || response === undefined) {
        throw new Error('删除资金账户：响应为空')
      }
      
      return response
    } catch (error) {
      console.error('删除资金账户失败:', error)
      // 包装错误，提供更详细的信息
      const errorMessage = error instanceof Error ? error.message : '未知错误'
      throw new Error(`删除资金账户失败: ${errorMessage}`)
    }
  }
  
  /**
   * 批量删除资金账户 - 根据API文档调整
   * 注意：确保数据格式正确，防止JSON解析错误
   */
  static async batchDeleteAccount(ids: (number | string)[]) {
    try {
      // 确保ids数组格式正确，所有元素转换为字符串
      const validIds = ids.map(id => String(id).trim()).filter(id => id)
      if (validIds.length === 0) {
        throw new Error('账户ID列表不能为空')
      }
      
      // 批量删除使用delete方法，直接传递ids数组
      const response = await this.http.delete('/c2-sysbase/funds/batch-delete', { ids: validIds })
      
      // 验证响应的有效性
      if (response === null || response === undefined) {
        throw new Error('批量删除资金账户：响应为空')
      }
      
      return response
    } catch (error) {
      console.error('批量删除资金账户失败:', error)
      // 包装错误，提供更详细的信息
      const errorMessage = error instanceof Error ? error.message : '未知错误'
      throw new Error(`批量删除资金账户失败: ${errorMessage}`)
    }
  }
}
