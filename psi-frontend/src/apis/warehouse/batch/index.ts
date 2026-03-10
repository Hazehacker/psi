/*
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-29 23:20:00
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-10-29 23:43:50
 * @FilePath: \psi-frontend\src\apis\warehouse\batch\index.ts
 * @Description: 仓库管理-批次查询接口
 */
import { useHttp, type RequestCallback } from '@/plugins/http'
import type {
  PageDTO,
  BatchDTO,
  DetailPageDTO,
  BatchDetailDTO,
  BatchListRequest,
  BatchDetailRequest
} from './type'

const currBaseUrl = '/j2-store/batch'
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
 * @description 获取批次列表（条件+分页）
 * @param params 批次查询参数
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getBatchList = async (
  params: BatchListRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<PageDTO<BatchDTO>>(`${currBaseUrl}/list`, params, {
      showLoading: true
    }),
    success,
    fail
  )
}

/**
 * @description 获取指定批次详情数据
 * @param params 批次详情查询参数
 * @param success 成功回调
 * @param fail 失败回调
 */
export const getBatchDetailList = async (
  params: BatchDetailRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<DetailPageDTO<BatchDetailDTO>>(`${currBaseUrl}/detail`, params, {
      showLoading: true
    }),
    success,
    fail
  )
}

/**
 * @description 导出批次数据
 * @param params 导出参数
 * @param success 成功回调
 * @param fail 失败回调
 */
export const exportBatchExcel = async (
  params: BatchListRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<any>(`${currBaseUrl}/export`, params, {
      showLoading: true,
      responseType: 'blob'
    }),
    success,
    fail
  )
}

/**
 * @description 导出批次详情数据
 * @param params 导出详情参数
 * @param success 成功回调
 * @param fail 失败回调
 */
export const exportBatchDetailExcel = async (
  params: BatchDetailRequest,
  success: RequestCallback,
  fail: RequestCallback
) => {
  return handleRequest(
    http.get<any>(`${currBaseUrl}/exportDetail`, params, {
      showLoading: true,
      responseType: 'blob'
    }),
    success,
    fail
  )
}

// 批次类型枚举
export const BatchState = {
  NORMAL: 0, // 常规批次
  WARNING: 1 // 预警批次
} as const

// 操作类型枚举
export const OperationType = {
  IN: '入库',
  OUT: '出库'
} as const

// 单据类型枚举
export const DocumentType = {
  BUY: 'buy', // 采购单
  SELL: 'sell', // 销售单
  BRE: 'bre', // 采购退货单
  SRE: 'sre', // 销售退货单
  SWAP_OUT: 'swapOut', // 调拨单-出
  SWAP_ENTER: 'swapEnter', // 调拨单-入
  ENTRY: 'entry', // 其它入库单
  EXTRY: 'extry' // 其它出库单
} as const
