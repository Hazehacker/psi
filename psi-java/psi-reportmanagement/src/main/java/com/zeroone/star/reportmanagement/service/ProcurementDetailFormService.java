package com.zeroone.star.reportmanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.ProcurementDetailFromDTO;
import com.zeroone.star.project.query.j5.procurementreport.ProcurementDetailFormQuery;

import java.util.List;

/**
 * 采购明细表服务接口
 * @author chuming_7
 * @since 2025-10-27
 */
public interface ProcurementDetailFormService {
    PageDTO<ProcurementDetailFromDTO> listProcurementDetailForm(ProcurementDetailFormQuery query);

    List<ProcurementDetailFromDTO> listAllForExport(ProcurementDetailFormQuery query);
}
