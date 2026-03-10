/* eslint-disable */
// @ts-ignore
import request from '@/apis/openapi-ts-request-shim';
import type { CustomRequestOptions } from '@/apis/openapi-ts-request-shim';

import * as API from './types';

/** 导出数据 GET /j5-reportmanagement/other-income-expenditure/export */
export function otherIncomeExpenditureOpenApiExportUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.OtherIncomeExpenditureOpenApiExportUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<string>(
    '/j5-reportmanagement/other-income-expenditure/export',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 获取报表（条件+分页） GET /j5-reportmanagement/other-income-expenditure/query */
export function otherIncomeExpenditureQueryUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.OtherIncomeExpenditureQueryUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<API.JsonVOPageDTO_>(
    '/j5-reportmanagement/other-income-expenditure/query',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
