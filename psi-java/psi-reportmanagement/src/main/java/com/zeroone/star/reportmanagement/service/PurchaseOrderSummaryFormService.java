package com.zeroone.star.reportmanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseOrderSummaryFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseOrderSummaryFormQuery;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;


/**
 * 采购汇总表服务接口
 */
public interface PurchaseOrderSummaryFormService {

    /**
     * 查询采购汇总列表
     *
     * @param query 查询参数
     * @return 汇总列表
     */
    PageDTO<PurchaseOrderSummaryFormDTO> listPurchaseSummary(PurchaseOrderSummaryFormQuery query);

    /**
     * 导出采购汇总列表
     *
     * @param query 到处参数
     * @return ResponseEntity<byte[]>
     */
    public ResponseEntity<byte[]> export(PurchaseOrderSummaryFormQuery query) throws IOException;


}
