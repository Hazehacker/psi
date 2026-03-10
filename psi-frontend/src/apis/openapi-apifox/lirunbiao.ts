/* eslint-disable */
// @ts-ignore
import request from '@/apis/openapi-ts-request-shim';
import type { CustomRequestOptions } from '@/apis/openapi-ts-request-shim';

import * as API from './types';

/** 获取报表（条件+分页） GET /j5-reportmanagement/profit */
export function profitUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.ProfitUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<API.JsonVOPageDTO2>('/j5-reportmanagement/profit', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 导出数据 GET /j5-reportmanagement/profit/export */
export function profitOpenApiExportUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.ProfitOpenApiExportUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<string>('/j5-reportmanagement/profit/export', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
