/**
 * 这是一个用于openapi-ts-request的shim文件
 */

import type { Method, AxiosRequestHeaders } from 'axios'
import { useHttp } from '@/plugins/http'
import type { CustomAxiosRequestConfig } from '@/apis/http'

/**
 * openapi-ts-request 的请求选项类型
 * 兼容 openapi-ts-request 生成的接口和本项目的 HTTP 客户端
 */
// @ts-ignore 忽略掉请求头的类型报错 自动生成的请求头最多是增加一些请求头字段罢了
export interface CustomRequestOptions extends Omit<CustomAxiosRequestConfig, 'method' | 'url'> {
  /**
   * 请求地址
   * @description
   * 不要求在实际请求的时候必填url，因为 openapi-ts-request 生成的接口函数，已经准备好了url
   * 所以传参时是不需要传入url的
   */
  url?: string

  /**
   * HTTP 请求方法
   * @description
   * 不要求在实际请求的时候必填method，因为 openapi-ts-request 生成的接口函数，已经准备好了method
   * 所以传参时是不需要传入method的
   */
  method?: Method

  /** 查询参数 (openapi-ts-request 使用 params，会被转换为 query) */
  params?: Record<string, unknown> | any
  /** 查询参数 (对应 Axios 的 params) */
  query?: any
  /** 请求体数据 */
  data?: any
  /** 请求头 (openapi-ts-request 使用 headers，会被转换为 header) */
  headers?: Partial<AxiosRequestHeaders>
  /** 请求头 (对应 Axios 的 headers) */
  header?: Record<string, unknown>
}

/**
 * openapi-ts-request 工具的 request 跨客户端适配方法
 * @description
 * 从 unibest 内抄的
 * @see https://github.com/unibest-tech/unibest
 * @param url 请求地址
 * @param options 请求选项
 * @returns Promise<JsonVO<T>> 返回符合项目规范的响应数据
 */
export default function requestOpenApiTsRequestShim<T = unknown>(
  url: string,
  options: Omit<CustomRequestOptions, 'url'>
) {
  // 获取 HTTP 客户端实例
  const http = useHttp()

  // 构建适配本项目 HTTP 客户端的请求配置
  const requestConfig: CustomAxiosRequestConfig = {
    ...options,
    // openapi-ts-request 使用 params 作为查询参数，需要转换为 Axios 的 params
    params: options.params || options.query,
    // openapi-ts-request 使用 headers，需要合并到 Axios 的 headers
    headers: {
      ...options.header,
      ...options.headers
    } as AxiosRequestHeaders
  }

  // 调用项目的 HTTP 客户端
  return http.request<T>(
    // 这里做类型忽略 因为 openapi-ts-request 生成的函数必定提供method
    options!.method!,
    url,
    requestConfig
  )
}
