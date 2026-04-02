package com.zeroone.star.reportmanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesReceiptDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesReceiptQuery;

/**
 * 销售收款表服务
 *
 * @author leyu
 * @date 2025-10-26
 */
public interface SalesReceiptService {
    PageDTO<SalesReceiptDTO> listAllPage(SalesReceiptQuery query);

    byte[] exportExcel(SalesReceiptQuery query);
}
