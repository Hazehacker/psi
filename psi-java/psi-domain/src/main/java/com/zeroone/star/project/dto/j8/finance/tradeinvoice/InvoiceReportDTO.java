package com.zeroone.star.project.dto.j8.finance.tradeinvoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("购销发票")
public class InvoiceReportDTO {
    @ApiModelProperty(value = "购销发票id", example = "##")
    private String id;

    @ApiModelProperty(value = "单据类型", example = "##")
    private String type;

    @ApiModelProperty(value = "所属组织", example = "##")
    private String frame;

    @ApiModelProperty(value = "往来单位", example = "##")
    private String businessUnit;

    @ApiModelProperty(value = "单据时间", example = "2025-10-22 ")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime time;

    @ApiModelProperty(value = "单据编号", example = "##")
    private String number;

    @ApiModelProperty(value = "单据金额", example = "8000")
    private BigDecimal total;

    @ApiModelProperty(value = "开票时间", example = "2025-10-22 ")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime invTime;

    @ApiModelProperty(value = "发票号码", example = "INV20251103BBB")
    private String invNumber;

    @ApiModelProperty(value = "发票状态", example = "已开票")
    private String invoice;

    @ApiModelProperty(value = "发票抬头", example = "3")
    private String title;

    @ApiModelProperty(value = "发票金额", example = "30000")
    private BigDecimal invMoney;

    @ApiModelProperty(value = "开票附件")
    private String file;

    @ApiModelProperty(value = "备注信息", example = "test")
    private String data;
}
