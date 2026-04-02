package com.zeroone.star.finance.mapper.tradeinvoice;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.finance.entity.BillSnapshot;
import com.zeroone.star.finance.entity.Invoice;
import com.zeroone.star.project.dto.j8.finance.tradeinvoice.InvoiceReportDTO;
import com.zeroone.star.project.dto.j8.finance.tradeinvoice.TradeInvoiceDTO;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.InvoiceReportQuery;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.TradeInvoiceKey;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.TradeInvoiceQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 发票详情 Mapper 接口
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
@Mapper
public interface InvoiceMapper extends BaseMapper<Invoice> {
    IPage<TradeInvoiceDTO> selectInvoiceWithConditions(Page<TradeInvoiceDTO> page, @Param("q") TradeInvoiceQuery query);


    List<BillSnapshot> selectBillSnapshot(@Param("buyIds") List<String> buyIds,
                                          @Param("breIds") List<String> breIds,
                                          @Param("sellIds") List<String> sellIds,
                                          @Param("sreIds") List<String> sreIds);

    List<TradeInvoiceDTO> selectInvoiceListByKeys(@Param("keys") List<TradeInvoiceKey> keys);

    Page<InvoiceReportDTO> getInvoiceReportList(Page<?> page, @Param("q") InvoiceReportQuery query);

    List<InvoiceReportDTO> selectInvoiceReportByIds(@Param("ids") List<String> ids);
}
