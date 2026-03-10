/* eslint-disable */
// @ts-ignore
import request from '@/apis/openapi-ts-request-shim';
import type { CustomRequestOptions } from '@/apis/openapi-ts-request-shim';

import * as API from './types';

/** 导出数据 GET /j5-reportmanagement/detailed-account-payable-statement/export */
export function detailedAccountPayableStatementOpenApiExportUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.DetailedAccountPayableStatementOpenApiExportUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<string>(
    '/j5-reportmanagement/detailed-account-payable-statement/export',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 获取报表（条件+分页） GET /j5-reportmanagement/detailed-account-payable-statement/query */
export function detailedAccountPayableStatementQueryUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.DetailedAccountPayableStatementQueryUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<API.JsonVOPageDTO3>(
    '/j5-reportmanagement/detailed-account-payable-statement/query',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
