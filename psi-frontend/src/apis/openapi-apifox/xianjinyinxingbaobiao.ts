/* eslint-disable */
// @ts-ignore
import request from '@/apis/openapi-ts-request-shim';
import type { CustomRequestOptions } from '@/apis/openapi-ts-request-shim';

import * as API from './types';

/** 获取报表(条件+分页) GET /j5-reportmanagement/cash-bank-statement */
export function cashBankStatementUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.CashBankStatementUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<API.JsonVOPageDTO6>(
    '/j5-reportmanagement/cash-bank-statement',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 导出数据 GET /j5-reportmanagement/cash-bank-statement/export */
export function cashBankStatementOpenApiExportUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.CashBankStatementOpenApiExportUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<Blob>('/j5-reportmanagement/cash-bank-statement/export', {
    method: 'GET',
    responseType: 'blob',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
