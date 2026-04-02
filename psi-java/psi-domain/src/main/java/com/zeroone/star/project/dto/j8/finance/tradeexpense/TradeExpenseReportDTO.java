package com.zeroone.star.project.dto.j8.finance.tradeexpense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "TradeExpenseReportDTO", description = "购销费用报表查询响应DTO")
public class TradeExpenseReportDTO {
    @ApiModelProperty(value = "cost主键ID")
    private String id;

    @ApiModelProperty(value = "单据类型")
    private String documentType;

    @ApiModelProperty(value = "所属组织")
    private String organization;

    @ApiModelProperty(value = "往来单位")
    private String partnerUnit;

    @ApiModelProperty(value = "单据时间")
    private String documentDate;

    @ApiModelProperty(value = "单据编号")
    private String documentNumber;

    @ApiModelProperty(value = "支出类别（如：运输费、包装费、差旅费等）")
    private String expenseType;

    @ApiModelProperty(value = "结算状态（如：已结算、未结算、部分结算）")
    private String settlementStatus;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "cost详情列表")
    private List<ReportInfoDTO> infos;
}
