package com.zeroone.star.project.j5.procurementreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseRankingFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseRankingFormQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 采购排行表Api
 * @author impovo123
 * @date 2025/10/20
 */
public interface PurchaseRankingFormApi {

    /**
     * 查询采购排行表
     * @param query 查询参数
     * @return 采购排行跟踪列表
     */
    JsonVO<PageDTO<PurchaseRankingFormDTO>> listPurchaseRankingForm(PurchaseRankingFormQuery query);

    /**
     * 导出采购排行表
     * @param query 查询参数
     * @return 采购排行跟踪列表
     */
    ResponseEntity<byte[]> exportPurchaseRankingFormToExcel(PurchaseRankingFormQuery query);
}
