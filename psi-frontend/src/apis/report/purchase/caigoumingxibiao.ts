/* eslint-disable */
// @ts-ignore
import request from '@/apis/openapi-ts-request-shim';
import type { CustomRequestOptions } from '@/apis/openapi-ts-request-shim';

import * as API from './types';

/** 导出采购明细表为Excel GET /j5-reportmanagement/procurement-detail-form/export */
export function procurementDetailFormOpenApiExportUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.ProcurementDetailFormOpenApiExportUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<Blob>('/j5-reportmanagement/procurement-detail-form/export', {
    method: 'GET',
    responseType: 'blob',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取报表（条件+分页） GET /j5-reportmanagement/procurement-detail-form/query */
export function procurementDetailFormQueryUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.ProcurementDetailFormQueryUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<API.JsonVOPageDTO9>(
    '/j5-reportmanagement/procurement-detail-form/query',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
