package com.zeroone.star.report.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.report.wss_KazamataNeri.StockSummaryReportDTO;
import com.zeroone.star.project.query.j8.report.StockSummaryReportQuery;
import org.springframework.http.ResponseEntity;


public interface StockSummaryReportService {
    PageDTO<StockSummaryReportDTO> listGoodsReceiptAndDispatchSummary(StockSummaryReportQuery stockSummaryReportQuery);

    ResponseEntity<byte[]> exportWssExcel(StockSummaryReportQuery q);
}
