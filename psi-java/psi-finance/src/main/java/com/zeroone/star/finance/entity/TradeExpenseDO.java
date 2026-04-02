package com.zeroone.star.finance.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 购销费用-数据对象（与查询列一致）
 */
@Data
public class TradeExpenseDO {
    @ApiModelProperty(value = "cost 主键ID")
    private String id;

    @ApiModelProperty(value = "所属组织")
    private String organization;

    @ApiModelProperty(value = "单据类型")
    private String documentType;

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
    private BigDecimal amount;

    @ApiModelProperty(value = "已结算金额")
    private BigDecimal settledAmount;

    @ApiModelProperty(value = "未结算金额")
    private BigDecimal unsettledAmount;

    @ApiModelProperty(value = "结算金额（=已结算金额）")
    private BigDecimal settlementAmount;
}
