package com.zeroone.star.project.dto.j8.finance.tradeexpense;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReportInfoDTO {
    @ApiModelProperty(value = "cost_info.ID")
    private String id;

    @ApiModelProperty(value = "单据类型")
    private String documentType = "其他支出单";

    @ApiModelProperty(value = "所属组织")
    private String organization;

    @ApiModelProperty(value = "往来单位")
    private String partnerUnit;

    @ApiModelProperty(value = "单据时间")
    private String documentDate;

    @ApiModelProperty(value = "单据编号")
    private String documentNumber;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;
}
