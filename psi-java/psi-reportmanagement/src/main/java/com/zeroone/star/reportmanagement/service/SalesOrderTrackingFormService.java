package com.zeroone.star.reportmanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesOrderTrackingFormDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesOrderTrackingFormQuery;

/**
 * 销售订单跟踪表服务
 *
 * @author leyu
 * @date 2025-10-23
 */

public interface SalesOrderTrackingFormService {
    PageDTO<SalesOrderTrackingFormDTO> listAllPage(SalesOrderTrackingFormQuery query);


    byte[] exportExcel(SalesOrderTrackingFormQuery query);
}
