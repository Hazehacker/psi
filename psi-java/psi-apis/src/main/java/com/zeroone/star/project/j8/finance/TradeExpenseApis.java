package com.zeroone.star.project.j8.finance;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.CostDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.TradeExpenseReportDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.TradeExpenseResponseDTO;
import com.zeroone.star.project.query.j8.finance.tradeexpense.TradeExpenseQuery;
import com.zeroone.star.project.query.j8.finance.tradeexpense.TradeExpenseReportQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 购销费用接口
 * ---------------------------------
 * 功能模块：
 * 1. 获取费用列表（支持条件查询 + 分页）
 * 2. 获取结算单数据
 * 3. 导出费用数据（Excel/CSV）
 * 4. 获取购销费用报表（支持条件查询 + 分页）
 * 5. 导出报表数据（Excel/CSV）
 */
public interface TradeExpenseApis {
    JsonVO<PageDTO<TradeExpenseResponseDTO>> queryTradeExpensePage(TradeExpenseQuery query);
    JsonVO<List<CostDTO>> getSettles(List<String> ids);
    ResponseEntity<byte[]> exportBillExcel(List<String> ids);
    JsonVO<PageDTO<TradeExpenseReportDTO>> listBill(TradeExpenseReportQuery query);
    ResponseEntity<byte[]> exportReportData(TradeExpenseReportQuery query);
}
