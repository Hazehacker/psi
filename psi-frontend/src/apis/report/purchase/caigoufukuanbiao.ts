/* eslint-disable */
// @ts-ignore
import request from '@/apis/openapi-ts-request-shim';
import type { CustomRequestOptions } from '@/apis/openapi-ts-request-shim';

import * as API from './types';

/** 获取报表（条件+分页） GET /j5-reportmanagement/procurement-payment-form */
export function procurementPaymentFormUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.ProcurementPaymentFormUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<API.JsonVOPageDTO7>(
    '/j5-reportmanagement/procurement-payment-form',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 导出数据 GET /j5-reportmanagement/procurement-payment-form/export */
export function procurementPaymentFormOpenApiExportUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.ProcurementPaymentFormOpenApiExportUsingGetParams;
  options?: CustomRequestOptions;
}) {
  return request<string>(
    '/j5-reportmanagement/procurement-payment-form/export',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
