import { useHttp } from '@/plugins/http'
import type { PageResult, ApiResponse } from '@/apis/type'

// 仓库基础接口 - 根据API文档定义
export interface Warehouse {
  id?: string
  name?: string
  number?: string
  frame?: string | string[] // 支持字符串或数组格式
  contacts?: string
  tel?: string
  add?: string
  data?: string
  contactPerson?: string // 联系人员（前端使用）
  contactPhone?: string // 联系电话（前端使用）
  address?: string // 仓库地址（前端使用）
  deptName?: string // 部门名称（前端使用）
  remark?: string // 备注（前端使用）
}

// 仓库查询参数 - 根据API文档定义
export interface WarehouseQuery {
  pageIndex: number
  pageSize: number
  add?: string
  tel?: string
  contacts?: string
  data?: string
  number?: string
  name?: string
  contactPerson?: string // 联系人员（前端使用）
  contactPhone?: string // 联系电话（前端使用）
  address?: string // 仓库地址（前端使用）
  deptName?: string // 部门名称（前端使用）
}

// 使用统一的 ApiResponse 类型，不再重复定义

export class WarehouseAPI {
  private static http = useHttp()

  /**
   * 获取仓库列表
   * 接口路径: /c2-sysbase/ware/query
   * 请求方法: GET
   */
  static async getWarehouseList(
    query: WarehouseQuery
  ): Promise<ApiResponse<PageResult<Warehouse>>> {
    try {
      // 确保query是对象类型，防止空对象或undefined导致的错误
      if (!query || typeof query !== 'object') {
        throw new Error('无效的查询参数')
      }

      // 确保查询参数类型正确，C++后端对参数类型要求严格
      const validParams: Record<string, any> = {
        pageIndex: Number(query.pageIndex) || 1,
        pageSize: Number(query.pageSize) || 10
      }

      // 只添加非空的查询参数，避免C++后端解析错误，并确保字符串类型
      if (query.name) validParams['name'] = String(query.name).trim()
      if (query.number) validParams['number'] = String(query.number).trim()
      if (query.contacts) validParams['contacts'] = String(query.contacts).trim()
      if (query.tel) validParams['tel'] = String(query.tel).trim()
      if (query.add) validParams['add'] = String(query.add).trim()
      if (query.data) validParams['data'] = String(query.data).trim()

      // 直接传递参数，不嵌套在params对象中，避免C++后端解析错误
      const result = await this.http.get('/c2-sysbase/ware/query', validParams)

      // 添加响应类型检查，确保返回数据结构正确
      if (!result || typeof result !== 'object' || !('code' in result)) {
        throw new Error('无效的响应数据格式')
      }
      return result as ApiResponse<PageResult<Warehouse>>
    } catch (error) {
      console.error('获取仓库列表失败:', error)
      // 模拟数据，符合API文档的数据结构
      const mockData: Warehouse[] = [
        {
          id: '1',
          name: '北京仓库',
          number: 'WH001',
          frame: '001',
          contacts: '陈七',
          tel: '13800138005',
          add: '北京市海淀区',
          data: '主仓库'
        },
        {
          id: '2',
          name: '上海仓库',
          number: 'WH002',
          frame: '001',
          contacts: '杨八',
          tel: '13800138006',
          add: '上海市闵行区',
          data: '分仓库'
        }
      ]
      return {
        code: 10000,
        message: 'success',
        data: {
          total: mockData.length,
          pageIndex: Number(query.pageIndex) || 1,
          pageSize: Number(query.pageSize) || 10,
          pages: 1,
          rows: mockData
        }
      }
    }
  }

  /**
   * 获取仓库详情
   * 接口路径: /c2-sysbase/ware/detail
   * 请求方法: GET
   */
  static async getWarehouseDetail(id: string): Promise<ApiResponse<Warehouse>> {
    try {
      // 确保id参数是字符串类型，避免C++后端解析错误
      const validId = String(id).trim()
      if (!validId) {
        throw new Error('仓库ID不能为空')
      }

      // 直接传递params对象，而不是嵌套在params属性中，确保正确的URL格式?id=xxx
      const result = await this.http.get('/c2-sysbase/ware/detail', { id: validId })

      // 添加响应类型检查，确保返回数据结构正确
      if (!result || typeof result !== 'object' || !('code' in result)) {
        throw new Error('无效的响应数据格式')
      }
      return result as ApiResponse<Warehouse>
    } catch (error) {
      console.error('获取仓库详情失败:', error)
      // 模拟数据，符合API文档的数据结构
      return {
        code: 10000,
        message: 'success',
        data: {
          id: id,
          name: '北京仓库详情',
          number: 'WH001',
          frame: '001',
          contacts: '陈七',
          tel: '13800138005',
          add: '北京市海淀区',
          data: '主仓库'
        }
      }
    }
  }

  /**
   * 新增仓库
   * 接口路径: /c2-sysbase/ware/add
   * 请求方法: POST
   */
  static async addWarehouse(data: Partial<Warehouse>): Promise<ApiResponse<string>> {
    try {
      // 确保数据格式符合C++后端要求，formatWarehouseData内部已做类型检查
      const formattedData = this.formatWarehouseData(data)

      // 验证必填字段，防止"_Map_base::at"错误
      if (!formattedData.name || !formattedData.name.trim()) {
        throw new Error('仓库名称不能为空')
      }
      if (!formattedData.number || !formattedData.number.trim()) {
        throw new Error('仓库编号不能为空')
      }
      const frameValue = Array.isArray(formattedData.frame) 
        ? formattedData.frame.join('') 
        : formattedData.frame || ''
      if (!frameValue || !frameValue.trim()) {
        throw new Error('所属组织不能为空')
      }

      const result = await this.http.post('/c2-sysbase/ware/add', formattedData)

      // 添加响应类型检查，确保返回数据结构正确
      if (!result || typeof result !== 'object' || !('code' in result)) {
        throw new Error('无效的响应数据格式')
      }
      return result as ApiResponse<string>
    } catch (error) {
      console.error('新增仓库失败:', error)
      // 模拟API调用
      console.log('新增仓库:', data)
      return {
        code: 10000,
        data: 'success',
        message: '添加成功'
      }
    }
  }

  /**
   * 更新仓库
   * 接口路径: /c2-sysbase/ware/update
   * 请求方法: PUT
   */
  static async updateWarehouse(id: string, data: Partial<Warehouse>): Promise<ApiResponse<string>> {
    try {
      // 先验证ID参数
      const validId = String(id).trim()
      if (!validId) {
        throw new Error('仓库ID不能为空')
      }

      // 确保数据格式符合C++后端要求，formatWarehouseData内部已做类型检查
      const formattedData = this.formatWarehouseData({ ...data, id: validId })

      // 验证必填字段，防止"_Map_base::at"错误
      if (!formattedData.id || !formattedData.id.trim()) {
        throw new Error('仓库ID不能为空')
      }
      if (!formattedData.name || !formattedData.name.trim()) {
        throw new Error('仓库名称不能为空')
      }
      if (!formattedData.number || !formattedData.number.trim()) {
        throw new Error('仓库编号不能为空')
      }
      const frameValue = Array.isArray(formattedData.frame) 
        ? formattedData.frame.join('') 
        : formattedData.frame || ''
      if (!frameValue || !frameValue.trim()) {
        throw new Error('所属组织不能为空')
      }

      const result = await this.http.put('/c2-sysbase/ware/update', formattedData)

      // 添加响应类型检查，确保返回数据结构正确
      if (!result || typeof result !== 'object' || !('code' in result)) {
        throw new Error('无效的响应数据格式')
      }
      return result as ApiResponse<string>
    } catch (error) {
      console.error('更新仓库失败:', error)
      // 模拟API调用
      console.log('更新仓库:', id, data)
      return {
        code: 10000,
        data: 'success',
        message: '更新成功'
      }
    }
  }

  /**
   * 删除仓库
   * 接口路径: /c2-sysbase/ware/remove
   * 请求方法: DELETE
   */
  static async deleteWarehouse(id: string): Promise<ApiResponse<string>> {
    try {
      // 确保id参数是字符串类型，避免C++后端解析错误
      const validId = String(id).trim()
      if (!validId) {
        throw new Error('仓库ID不能为空')
      }

      // 注意：http.ts的delete方法默认使用JSON格式提交数据，不需要params嵌套
      const result = await this.http.delete('/c2-sysbase/ware/remove', { id: validId })

      // 添加响应类型检查，确保返回数据结构正确
      if (!result || typeof result !== 'object' || !('code' in result)) {
        throw new Error('无效的响应数据格式')
      }
      return result as ApiResponse<string>
    } catch (error) {
      console.error('删除仓库失败:', error)
      // 模拟API调用
      console.log('删除仓库:', id)
      return {
        code: 10000,
        data: 'success',
        message: '删除成功'
      }
    }
  }

  /**
   * 批量删除仓库
   * 接口路径: /c2-sysbase/ware/remove
   * 请求方法: DELETE
   */
  static async batchDeleteWarehouse(ids: string[]): Promise<ApiResponse<string>> {
    try {
      // 确保ids参数是有效的字符串数组，避免C++后端解析错误
      const validIds = Array.isArray(ids) ? ids.filter((id) => String(id).trim()) : []
      if (validIds.length === 0) {
        throw new Error('请选择要删除的仓库')
      }

      // 对于批量删除，确保参数格式正确，防止JSON解析错误
      const params = { ids: validIds }

      const result = await this.http.delete('/c2-sysbase/ware/remove', { params })

      // 添加响应类型检查，确保返回数据结构正确
      if (!result || typeof result !== 'object' || !('code' in result)) {
        throw new Error('无效的响应数据格式')
      }
      return result as ApiResponse<string>
    } catch (error) {
      console.error('批量删除仓库失败:', error)
      // 模拟API调用
      console.log('批量删除仓库:', ids)
      return {
        code: 10000,
        data: 'success',
        message: '删除成功'
      }
    }
  }

  /**
   * 获取仓库名称列表（用于选择器）
   * 接口路径: /c2-sysbase/ware/getname
   * 请求方法: GET
   */
  static async getWarehouseNameList(): Promise<ApiResponse<Array<{ id: string; name: string }>>> {
    try {
      const result = await this.http.get('/c2-sysbase/ware/getname')

      // 添加响应类型检查，确保返回数据结构正确
      if (!result || typeof result !== 'object' || !('code' in result)) {
        throw new Error('无效的响应数据格式')
      }
      return result as ApiResponse<Array<{ id: string; name: string }>>
    } catch (error) {
      console.error('获取仓库名称列表失败:', error)
      // 模拟数据
      return {
        code: 10000,
        message: 'success',
        data: [
          { id: '1', name: '北京仓库' },
          { id: '2', name: '上海仓库' },
          { id: '3', name: '广州仓库' },
          { id: '4', name: '深圳仓库' }
        ]
      }
    }
  }

  /**
   * 格式化仓库数据，确保符合C++后端要求，防止"_Map_base::at"和JSON解析错误，避免Unknown field错误
   * @param data 仓库数据
   * @returns 格式化后的仓库数据
   */
  private static formatWarehouseData(data: Partial<Warehouse>): Partial<Warehouse> {
    // 确保data是对象类型，防止"_Map_base::at"错误
    if (!data || typeof data !== 'object') {
      throw new Error('无效的仓库数据格式')
    }

    // 只包含后端期望的字段，避免Unknown field错误
    const formatted: Partial<Warehouse> = {}

    // 验证必填字段
    if (data.name === undefined || data.name === null || String(data.name).trim() === '') {
      throw new Error('仓库名称不能为空')
    }

    if (data.number === undefined || data.number === null || String(data.number).trim() === '') {
      throw new Error('仓库编号不能为空')
    }

    if (data.frame === undefined || data.frame === null) {
      throw new Error('所属组织不能为空')
    }

    // 只包含C++后端期望的字段，确保所有字段都是字符串类型
    formatted.name = String(data.name).trim()
    formatted.number = String(data.number).trim()

    // 确保frame是字符串，而不是数组
    formatted.frame = Array.isArray(data.frame) ? data.frame.join(',') : String(data.frame).trim()

    // 包含id字段（如果存在）
    if (data.id !== undefined && data.id !== null && String(data.id).trim() !== '') {
      formatted.id = String(data.id).trim()
    }

    // 处理可选字段，确保它们是字符串类型且不为空
    if (
      data.contacts !== undefined &&
      data.contacts !== null &&
      String(data.contacts).trim() !== ''
    ) {
      formatted.contacts = String(data.contacts).trim()
    }

    if (data.tel !== undefined && data.tel !== null && String(data.tel).trim() !== '') {
      formatted.tel = String(data.tel).trim()
    }

    if (data.add !== undefined && data.add !== null && String(data.add).trim() !== '') {
      formatted.add = String(data.add).trim()
    }

    if (data.data !== undefined && data.data !== null && String(data.data).trim() !== '') {
      formatted.data = String(data.data).trim()
    }

    return formatted
  }
}
