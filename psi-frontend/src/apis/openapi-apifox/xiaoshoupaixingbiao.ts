/* eslint-disable */
// @ts-ignore
import request from '@/apis/openapi-ts-request-shim';
import type { CustomRequestOptions } from '@/apis/openapi-ts-request-shim';

import * as API from './types';

/** 获取报表（条件+分页） GET /j5-reportmanagement/sales-ranking-table */
export function salesRankingTableUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.SalesRankingTableUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<API.JsonVOPageDTO12>(
    '/j5-reportmanagement/sales-ranking-table',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 导出数据 GET /j5-reportmanagement/sales-ranking-table/export */
export function salesRankingTableOpenApiExportUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.SalesRankingTableOpenApiExportUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<Blob>('/j5-reportmanagement/sales-ranking-table/export', {
    method: 'GET',
    responseType: 'blob',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
