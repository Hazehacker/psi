package com.zeroone.star.reportmanagement.service;


import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseOrderTrackingFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseOrderTrackingFormQuery;

import java.util.List;

/**
 * @author yu
 * @date 2025/10/23
 */
public interface PurchaseOrderTrackingFormService{

    PageDTO<PurchaseOrderTrackingFormDTO> listPurchaseOrderTrackingForm(PurchaseOrderTrackingFormQuery query);

    List<PurchaseOrderTrackingFormDTO> listAllForExport(PurchaseOrderTrackingFormQuery query);
}
