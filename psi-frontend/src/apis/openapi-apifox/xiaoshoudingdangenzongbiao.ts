/* eslint-disable */
// @ts-ignore
import request from '@/apis/openapi-ts-request-shim';
import type { CustomRequestOptions } from '@/apis/openapi-ts-request-shim';

import * as API from './types';

/** 获取报表（条件+分页） GET /j5-reportmanagement/sales-order-tracking-form */
export function salesOrderTrackingFormUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.SalesOrderTrackingFormUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<API.JsonVOPageDTO16>(
    '/j5-reportmanagement/sales-order-tracking-form',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 导出数据 GET /j5-reportmanagement/sales-order-tracking-form/export */
export function salesOrderTrackingFormOpenApiExportUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.SalesOrderTrackingFormOpenApiExportUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<Blob>(
    '/j5-reportmanagement/sales-order-tracking-form/export',
    {
      method: 'GET',
      responseType: 'blob',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
