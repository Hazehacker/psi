/* eslint-disable */
// @ts-ignore
import request from '@/apis/openapi-ts-request-shim';
import type { CustomRequestOptions } from '@/apis/openapi-ts-request-shim';

import * as API from './types';

/** 获取报表(条件+分页) GET /j5-reportmanagement/detailed-account-receivable-statement */
export function detailedAccountReceivableStatementUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.DetailedAccountReceivableStatementUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<API.JsonVOPageDTO4>(
    '/j5-reportmanagement/detailed-account-receivable-statement',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 导出数据 GET /j5-reportmanagement/detailed-account-receivable-statement/export */
export function detailedAccountReceivableStatementOpenApiExportUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.DetailedAccountReceivableStatementOpenApiExportUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<Blob>(
    '/j5-reportmanagement/detailed-account-receivable-statement/export',
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
