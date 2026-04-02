package com.zeroone.star.project.dto.j5.fundreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 应付款明细表对象
 */
@Data
@ApiModel("应付账明细表数据对象")
public class DetailedAccountPayableStatementDTO {

    @ApiModelProperty(value = "供应商", example = "公司")
    private String supplier;

    @ApiModelProperty(value = "单据类型", example = "销售单")
    private String documentType;

    @ApiModelProperty(value = "所属组织", example = "默认组织")
    private String frame;

    @ApiModelProperty(value = "单据时间", example = "2025-10-21")
    private String dateTime;

    @ApiModelProperty(value = "单据编号", example = "QTSRD250924171")
    private String documentNumber;

    @ApiModelProperty(value = "增加应付款", example = "1000.00")
    private BigDecimal accountsPayableIncrease;

    @ApiModelProperty(value = "增加预付款", example = "1000.00")
    private BigDecimal advancePaymentIncrease;

    @ApiModelProperty(value = "应付款余额", example = "1000.00")
    private BigDecimal accountsPayableBalance;

    @ApiModelProperty(value = "备注", example = "备注信息")
    private String remark;


}
