package com.zeroone.star.reportmanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseRankingFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseRankingFormQuery;

public interface PurchaseRankingService{

    PageDTO<PurchaseRankingFormDTO> listPurchaseRankingForm(PurchaseRankingFormQuery query);
}
