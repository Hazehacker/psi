package com.zeroone.star.reportmanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.UnitArrearsReportDTO;
import com.zeroone.star.project.query.j5.fundreport.UnitReceiptQuery;

public interface UnitArrearsReportService {

    PageDTO<UnitArrearsReportDTO> query(UnitReceiptQuery query);

}
