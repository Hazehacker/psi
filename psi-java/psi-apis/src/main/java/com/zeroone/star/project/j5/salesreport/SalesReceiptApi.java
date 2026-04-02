package com.zeroone.star.project.j5.salesreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesReceiptDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesSummaryDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesReceiptQuery;
import com.zeroone.star.project.query.j5.salesreport.SalesSummaryQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 销售收款表Api
 * @author yu
 * @date 2025/10/21
 */
public interface SalesReceiptApi {
    /**
     * 查询销售收款表
     * @param query 查询参数
     * @return 销售汇总表数据对象
     */
    JsonVO<PageDTO<SalesReceiptDTO>> listSalesReceipt(SalesReceiptQuery query);

    /**
     * 导出销售收款表
     * @param query 查询参数
     * @return 销售收款表下载字节流
     */
    ResponseEntity<byte[]> exportSalesReceiptToExcel(SalesReceiptQuery query);
}
