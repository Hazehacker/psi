package com.zeroone.star.project.dto.j8.finance.tradeexpense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TradeExpenseResponseDTO", description = "购销费用查询响应DTO")
public class TradeExpenseResponseDTO {
    @ApiModelProperty(value = "cost 主键ID")
    private String id;

    @ApiModelProperty(value = "单据类型")
    private String documentType;

    @ApiModelProperty(value = "所属组织")
    private String organization;

    @ApiModelProperty(value = "往来单位（供应商/客户）")
    private String partnerUnit;

    @ApiModelProperty(value = "单据日期（yyyy-MM-dd）")
    private String documentDate;

    @ApiModelProperty(value = "单据编号")
    private String documentNumber;

    @ApiModelProperty(value = "支出类别（iet.name）")
    private String expenseType;

    @ApiModelProperty(value = "结算状态（已结算/未结算/部分结算）")
    private String settlementStatus;

    @ApiModelProperty(value = "金额（费用应结）")
    private Double amount;

    @ApiModelProperty(value = "已结算金额")
    private Double settledAmount;

    @ApiModelProperty(value = "未结算金额")
    private Double unsettledAmount;

    @ApiModelProperty(value = "结算金额（=已结算金额）")
    private Double settlementAmount;
}
