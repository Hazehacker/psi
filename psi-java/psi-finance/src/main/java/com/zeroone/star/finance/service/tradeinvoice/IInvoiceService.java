package com.zeroone.star.finance.service.tradeinvoice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.finance.entity.Invoice;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.tradeinvoice.AddTradeInvoiceDTO;
import com.zeroone.star.project.dto.j8.finance.tradeinvoice.InvoiceReportDTO;
import com.zeroone.star.project.dto.j8.finance.tradeinvoice.TradeInvoiceDTO;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.InvoiceReportQuery;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.TradeInvoiceKey;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.TradeInvoiceQuery;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * <p>
 * 发票详情 服务类
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
public interface IInvoiceService extends IService<Invoice> {

    PageDTO<TradeInvoiceDTO> listTradeInvoice(TradeInvoiceQuery query);

    void addTradeInvoice(AddTradeInvoiceDTO dto);

    ResponseEntity<byte[]> exportInvoiceExcel(List<TradeInvoiceKey> keys);

    PageDTO<InvoiceReportDTO> getInvoiceReportList(InvoiceReportQuery query);

    ResponseEntity<byte[]> exportInvoiceReportExcel(List<String> ids);
}
