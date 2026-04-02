package com.zeroone.star.reportmanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesDetailFormDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesDetailFormQuery;

import java.util.List;


/**
 * 销售明细表Service接口
 * @author rainsilent
 * @date 2025/10/26
 */
public interface SalesDetailFormService {
    PageDTO<SalesDetailFormDTO> listSalesDetailForm(SalesDetailFormQuery query);

    List<SalesDetailFormDTO> listAllForExcel(SalesDetailFormQuery query);
}
