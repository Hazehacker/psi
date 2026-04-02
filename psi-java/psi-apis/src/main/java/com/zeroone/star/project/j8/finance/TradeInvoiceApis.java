package com.zeroone.star.project.j8.finance;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.tradeinvoice.AddTradeInvoiceDTO;
import com.zeroone.star.project.dto.j8.finance.tradeinvoice.InvoiceReportDTO;
import com.zeroone.star.project.dto.j8.finance.tradeinvoice.TradeInvoiceDTO;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.InvoiceReportQuery;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.TradeInvoiceKey;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.TradeInvoiceQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 购销发票接口
 * ---------------------------------
 * 功能模块：
 * 1. 获取发票列表（支持条件查询 + 分页）
 * 2. 开票（新增发票数据）
 * 3. 导出发票数据（Excel/CSV）
 * 4. 获取购销发票报表（支持条件查询 + 分页）
 * 5. 导出报表数据（Excel/CSV）
 */
public interface TradeInvoiceApis {
    JsonVO<PageDTO<TradeInvoiceDTO>> getTradeInvoice(TradeInvoiceQuery tradeInvoiceQuery);
    JsonVO<String> addTradeInvoice(AddTradeInvoiceDTO addTradeInvoiceDTO);
    public ResponseEntity<byte[]> export(List<TradeInvoiceKey> keys);
    JsonVO<PageDTO<InvoiceReportDTO>> getInvoiceReportList(InvoiceReportQuery query);
    ResponseEntity<byte[]> exportInvoiceReportExcel(List<String> ids);
}
