/*
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-29 23:20:00
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-12 09:39:26
 * @FilePath: \psi-frontend\src\apis\warehouse\check\index.ts
 * @Description: 仓库管理-库存盘点接口
 */
import { useHttp, type RequestCallback } from '@/plugins/http'
import type {
  PageDTO,
  InventoryVerifyDTO,
  OtherInListInfoDTO,
  OtherOutListInfoDTO,
  InventoryVerifyRequest,
  JsonVO
} from './type'

const currBaseUrl = '/j2-store/verify'
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
 * @description 导出库存盘点单
 * @param success 成功回调
 * @param fail 失败回调
 */
export const exportInventoryVerify = async (
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<any>(`${currBaseUrl}/export`, {}, {
      showLoading: true,
      responseType: 'blob'
    }),
    success,
    fail
  )
}

/**
 * @description 获取盘盈单数据
 * @param params 库存盘点请求参数
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getInventoryPlus = async (
  params: InventoryVerifyRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.post<JsonVO<PageDTO<OtherInListInfoDTO>>>(`${currBaseUrl}/inventoryPlus`, params, {
      showLoading: true
    }),
    success,
    fail
  )
}

/**
 * @description 获取盘亏单数据
 * @param params 库存盘点请求参数
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getInventoryReduce = async (
  params: InventoryVerifyRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.post<JsonVO<PageDTO<OtherOutListInfoDTO>>>(`${currBaseUrl}/inventoryReduce`, params, {
      showLoading: true
    }),
    success,
    fail
  )
}

// 盘点类型枚举
export const InventoryType = {
  PLUS: 'plus', // 盘盈
  REDUCE: 'reduce' // 盘亏
} as const

// 盘点状态枚举
export const InventoryStatus = {
  PENDING: 0, // 待盘点
  COUNTED: 1, // 已盘点
  VERIFIED: 2 // 已审核
} as const

// 差异类型枚举
export const DifferenceType = {
  NONE: 0, // 无差异
  PLUS: 1, // 盘盈
  REDUCE: -1 // 盘亏
} as const
