/* eslint-disable */
// @ts-ignore
import request from '@/apis/openapi-ts-request-shim';
import type { CustomRequestOptions } from '@/apis/openapi-ts-request-shim';

import * as API from './types';

/** 获取报表（条件+分页） GET /j5-reportmanagement/sales-summary */
export function salesSummaryUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.SalesSummaryUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<API.JsonVOPageDTO15>('/j5-reportmanagement/sales-summary', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 导出数据 GET /j5-reportmanagement/sales-summary/export */
export function salesSummaryOpenApiExportUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.SalesSummaryOpenApiExportUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<Blob>('/j5-reportmanagement/sales-summary/export', {
    method: 'GET',
    responseType: 'blob',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
