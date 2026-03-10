import { useHttp } from '@/plugins/http'
import type { JsonVO, PageDTO } from '../type'
import type {
  MenuItem,
  UserConfiguredMenu,
  SaveOftenMenusRequest,
  SaveOftenMenusResponse,
  OftenMenuQuery
} from './type'

// 定义基础URL - 根据接口文档，常用功能服务在j7组，虚拟前缀为/j7-sysargs
const baseUrl = '/j7-sysargs/often'

/**
 * 获取可设置的常用功能
 * @param userId 用户ID
 * @returns 可设置的菜单列表
 */
export const getAvailableOftenMenus = async (userId: string): Promise<MenuItem[]> => {
  try {
    const http = useHttp()
    const response = await http.get<JsonVO<MenuItem[]>>(`${baseUrl}/available`, { userId })
    // 修复类型问题：response.data 是 JsonVO<MenuItem[]> 的 data 属性
    return (response as any).data || []
  } catch (error) {
    console.error('获取可设置常用功能失败:', error)
    throw error
  }
}

/**
 * 获取已设置的常用功能
 * @param query 查询参数
 * @returns 已设置的常用功能分页数据
 */
export const getConfiguredOftenMenus = async (
  query: OftenMenuQuery
): Promise<PageDTO<UserConfiguredMenu>> => {
  try {
    const http = useHttp()
    const response = await http.get<JsonVO<PageDTO<UserConfiguredMenu>>>(
      `${baseUrl}/configured`,
      query
    )
    // 修复类型问题：response.data 是 JsonVO<PageDTO<UserConfiguredMenu>> 的 data 属性
    const data = (response as any).data
    return (
      data || {
        pageIndex: query.pageIndex,
        pageSize: query.pageSize,
        total: 0,
        pages: 0,
        rows: []
      }
    )
  } catch (error) {
    console.error('获取已设置常用功能失败:', error)
    throw error
  }
}

/**
 * 保存常用功能设置
 * @param request 保存请求数据
 * @returns 保存结果
 */
export const saveOftenMenus = async (
  request: SaveOftenMenusRequest
): Promise<SaveOftenMenusResponse> => {
  try {
    const http = useHttp()
    const response = await http.post<JsonVO<SaveOftenMenusResponse>>(`${baseUrl}/save`, request)
    // 修复类型问题：response.data 是 JsonVO<SaveOftenMenusResponse> 的 data 属性
    const data = (response as any).data
    return data || { success: false, message: '保存失败' }
  } catch (error) {
    console.error('保存常用功能设置失败:', error)
    throw error
  }
}
