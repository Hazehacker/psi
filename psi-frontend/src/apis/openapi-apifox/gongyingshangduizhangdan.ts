/* eslint-disable */
// @ts-ignore
import request from '@/apis/openapi-ts-request-shim';
import type { CustomRequestOptions } from '@/apis/openapi-ts-request-shim';

import * as API from './types';

/** 获取报表（条件+分页） GET /j5-reportmanagement/supplier-statement */
export function supplierStatementUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.SupplierStatementUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<API.JsonVOPageDTO__>(
    '/j5-reportmanagement/supplier-statement',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 导出报表 GET /j5-reportmanagement/supplier-statement/export */
export function supplierStatementOpenApiExportUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.SupplierStatementOpenApiExportUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<Blob>('/j5-reportmanagement/supplier-statement/export', {
    method: 'GET',
    responseType: 'blob',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
