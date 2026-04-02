package com.zeroone.star.reportmanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesQuery;

public interface SalesProfitService {

    PageDTO<Object> query(SalesQuery query);

}
