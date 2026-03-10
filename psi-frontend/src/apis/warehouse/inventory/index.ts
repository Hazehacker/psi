/*
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-29 07:53:32
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-12 17:41:08
 * @FilePath: \psi-frontend\src\apis\warehouse\inventory\index.ts
 * @Description: 仓库管理-库存查询接口
 */
import { useHttp, type RequestCallback } from '@/plugins/http'
import type {
  PageDTO,
  InventoryList,
  DetailPageDTO,
  InventoryDetailDTO,
  InventoryListRequest,
  InventoryDetailRequest
} from './type'

const currBaseUrl = '/j2-store/inventory'
const http = useHttp()

// 通用请求处理函数
const handleRequest = async <T>(
  request: Promise<any>,
  success: RequestCallback,
  fail: RequestCallback
) => {
  try {
    const res = await request
    if (res.data) {
      success(res.data)
    } else {
      fail(res)
    }
  } catch (err) {
    fail(err)
  }
}

/**
 * @description 获取库存列表（条件+分页）
 * @param params 查询参数
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getInventoryList = async (
  params: InventoryListRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<PageDTO<InventoryList>>(`${currBaseUrl}/query/list/select`, params, {
      showLoading: true
    }),
    success,
    fail
  )
}

/**
 * @description 获取指定库存详情（条件+分页）
 * @param params 详情查询参数
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getInventoryDetailList = async (
  params: InventoryDetailRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<DetailPageDTO<InventoryDetailDTO>>(`${currBaseUrl}/query/list/select/detail`, params, {
      showLoading: true
    }),
    success,
    fail
  )
}

/**
 * @description 导出库存列表数据Excel
 * @param params 导出参数
 * @param success 成功回调
 * @param fail 失败回调
 */
export const exportInventoryExcel = async (
  params: InventoryListRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<any>(`${currBaseUrl}/export`, params, {
      showLoading: true,
      responseType: 'blob',
      timeout: 600000
    }),
    success,
    fail
  )
}

/**
 * @description 导出库存详情数据Excel
 * @param id 商品ID
 * @param warehouseIds 仓库ID列表
 * @param success 成功回调
 * @param fail 失败回调
 */
export const exportInventoryDetailExcel = async (
  id: string,
  warehouseIds: string[],
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<any>(
      `${currBaseUrl}/exportDetail`,
      {
        id,
        warehouseIds
      },
      {
        showLoading: true,
        responseType: 'blob'
      }
    ),
    success,
    fail
  )
}

// 库存类型枚举
export const StockState = {
  NORMAL: 0, // 常规库存
  NON_ZERO: 1, // 非零库存
  WARNING: 2 // 预警库存
} as const

// 操作类型枚举
export const DirectionType = {
  DECREASE: 0, // 减少
  INCREASE: 1 // 增加
} as const
