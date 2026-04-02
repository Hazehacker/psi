package com.zeroone.star.project.dto.j8.finance.tradeinvoice;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeInvoiceDTO {
    @ApiModelProperty(value = "主单据ID", example = "invoice1")
    private String id;
    @ApiModelProperty(value = "单据类型", example = "buy")
    private String type;
    @ApiModelProperty(value = "所属组织", example = "默认组织")
    private String frame;
    @ApiModelProperty(value = "往来单位", example = "小王")
    private String unit;
    @ApiModelProperty(value = "单据时间", example = "2025-10-20 15:23:00")
    private LocalDateTime time;
    @ApiModelProperty(value = "单据编号", example = "QTZCD2510201810404")
    private String number;
    @ApiModelProperty(value = "发票状态", example = "已开票")
    private String status;
    @ApiModelProperty(value = "单据金额", example = "12345.67")
    private BigDecimal totalCount;
    @ApiModelProperty(value = "已开票金额", example = "12345.67")
    private BigDecimal invoicedCount;
    @ApiModelProperty(value = "未开票金额", example = "0.00")
    private BigDecimal disinvoicedCount;
    @ApiModelProperty(value = "开票金额", example = "0.00")
    private BigDecimal invoiceTotal;
}
