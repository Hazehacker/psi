package com.zeroone.star.reportmanagement.mapper.procurementreport;


import com.zeroone.star.project.dto.j5.procurementreport.PurchaseOrderTrackingFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseOrderTrackingFormQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yu
 * @date 2025/10/23
 */
public interface PurchaseOrderTrackingFormMapper {

    // Type=0 (按单据)
    List<PurchaseOrderTrackingFormDTO> selectPurchaseOrderTrackingForm(
            @Param("query") PurchaseOrderTrackingFormQuery query
    );
    List<PurchaseOrderTrackingFormDTO> selectAllForExport(
            @Param("query") PurchaseOrderTrackingFormQuery query
    );

    // Type=1 (按商品)
    List<PurchaseOrderTrackingFormDTO> selectPurchaseOrderTrackingFormByGoods(
            @Param("query") PurchaseOrderTrackingFormQuery query
    );
    List<PurchaseOrderTrackingFormDTO> selectAllForExportByGoods(
            @Param("query") PurchaseOrderTrackingFormQuery query
    );

}
