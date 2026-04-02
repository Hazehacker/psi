package com.zeroone.star.project.j5.procurementreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseOrderTrackingFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseOrderTrackingFormQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 采购报表，采购订单跟踪表Api
 * @author yu
 * @date 2025/10/18
 */
public interface PurchaseOrderTrackingFormApi {

    /**
     * 查询采购订单跟踪表
     * @param query 查询参数
     * @return 采购订单跟踪列表
     */
    JsonVO<PageDTO<PurchaseOrderTrackingFormDTO>> listPurchaseOrderTrackingForm(PurchaseOrderTrackingFormQuery query);

    /**
     * 导出采购订单跟踪表
     * @param query 查询参数
     * @return 采购订单跟踪列表
     */
    ResponseEntity<byte[]> exportPurchaseOrderTrackingFormToExcel(PurchaseOrderTrackingFormQuery query);
}
