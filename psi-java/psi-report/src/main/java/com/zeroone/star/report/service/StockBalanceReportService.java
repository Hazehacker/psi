package com.zeroone.star.report.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.report.ProductStockBalanceDTO;
import com.zeroone.star.project.query.j8.report.ProductStockBalanceQuery;
import org.springframework.http.ResponseEntity;

public interface StockBalanceReportService {
    PageDTO<ProductStockBalanceDTO> getProductStocksByCondition(ProductStockBalanceQuery queryDTO);

    ResponseEntity<byte[]> export(ProductStockBalanceQuery query);
}
