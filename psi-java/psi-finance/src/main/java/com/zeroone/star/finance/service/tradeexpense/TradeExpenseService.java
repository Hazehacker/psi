package com.zeroone.star.finance.service.tradeexpense;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.CostDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.TradeExpenseReportDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.TradeExpenseResponseDTO;
import com.zeroone.star.project.query.j8.finance.tradeexpense.TradeExpenseReportQuery;
import com.zeroone.star.project.query.j8.finance.tradeexpense.TradeExpenseQuery;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TradeExpenseService {
    PageDTO<TradeExpenseResponseDTO> queryTradeExpensePage(TradeExpenseQuery query);

    PageDTO<TradeExpenseReportDTO> queryTradeExpenseReportPage(TradeExpenseReportQuery query);

    List<CostDTO> getSettles(List<String> ids);

    ResponseEntity<byte[]> exportBillExcel(List<String> ids);

    ResponseEntity<byte[]> exportReportData(TradeExpenseReportQuery query);
}
