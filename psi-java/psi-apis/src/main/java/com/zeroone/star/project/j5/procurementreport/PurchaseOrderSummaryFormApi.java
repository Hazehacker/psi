package com.zeroone.star.project.j5.procurementreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseOrderSummaryFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseOrderSummaryFormQuery;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseOrderTrackingFormQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 采购汇总表Api
 */
public interface PurchaseOrderSummaryFormApi {
    /**
     * 查询采购汇总表
     * @param query 查询参数
     * @return 采购订单跟踪列表
     */
    JsonVO<PageDTO<PurchaseOrderSummaryFormDTO>> listPurchaseOrderSummaryForm(PurchaseOrderSummaryFormQuery query);

    /**
     * 导出采购汇总表
     * @param query 查询参数
     * @return 采购订单跟踪列表
     */
    ResponseEntity<byte[]> exportPurchaseOrderSummaryFormToExcel(PurchaseOrderSummaryFormQuery query);
}
