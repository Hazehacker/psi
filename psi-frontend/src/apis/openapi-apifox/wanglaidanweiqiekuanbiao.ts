/* eslint-disable */
// @ts-ignore
import request from '@/apis/openapi-ts-request-shim';
import type { CustomRequestOptions } from '@/apis/openapi-ts-request-shim';

import * as API from './types';

/** 获取报表（条件+分页） GET /j5-reportmanagement/unit-arrears-report-form */
export function unitArrearsReportFormUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.UnitArrearsReportFormUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<API.JsonVOPageDTO5>(
    '/j5-reportmanagement/unit-arrears-report-form',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 导出数据 GET /j5-reportmanagement/unit-arrears-report-form/export */
export function unitArrearsReportFormOpenApiExportUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.UnitArrearsReportFormOpenApiExportUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<string>(
    '/j5-reportmanagement/unit-arrears-report-form/export',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
