package com.zeroone.star.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author fy
 * @since 2025-11-03
 */
@Getter
@Setter
@TableName("invoice_summary_view")
public class InvoiceSummaryView implements Serializable {

    private static final long serialVersionUID = 1L;

    private String docType;

    private String org;

    private String contact;

    private LocalDateTime docDate;

    private String docNum;

    private String invoiceStatus;

    private BigDecimal totalAmount;

    private BigDecimal invoicedAmount;

    private BigDecimal uninvoicedAmount;


}
