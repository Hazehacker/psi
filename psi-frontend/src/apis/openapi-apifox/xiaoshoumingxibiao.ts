/* eslint-disable */
// @ts-ignore
import request from '@/apis/openapi-ts-request-shim';
import type { CustomRequestOptions } from '@/apis/openapi-ts-request-shim';

import * as API from './types';

/** 导出数据 GET /j5-reportmanagement/sales-detail-form/export */
export function salesDetailFormOpenApiExportUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.SalesDetailFormOpenApiExportUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<Blob>('/j5-reportmanagement/sales-detail-form/export', {
    method: 'GET',
    responseType: 'blob',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取报表(条件+分页) GET /j5-reportmanagement/sales-detail-form/query */
export function salesDetailFormQueryUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.SalesDetailFormQueryUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<API.JsonVOPageDTO14>(
    '/j5-reportmanagement/sales-detail-form/query',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
