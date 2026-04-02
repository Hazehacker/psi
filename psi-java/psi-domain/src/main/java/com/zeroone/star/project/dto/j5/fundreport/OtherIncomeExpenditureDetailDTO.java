package com.zeroone.star.project.dto.j5.fundreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 其它收支明细表对象
 */
@Data
@ApiModel("其它收支明细表数据对象")
public class OtherIncomeExpenditureDetailDTO {

    @ApiModelProperty(value = "单据类型", example = "其他收入单")
    private String documentType;

    @ApiModelProperty(value = "所属组织", example = "默认组织")
    private String frame;

    @ApiModelProperty(value = "往来单位", example = "公司")
    private String counterparty;

    @ApiModelProperty(value = "单据时间", example = "2025-10-21")
    private String dateTime;

    @ApiModelProperty(value = "单据编号", example = "QTSRD250924171")
    private String documentNumber;

    @ApiModelProperty(value = "收支类别", example = "销售收入")
    private String category;

    @ApiModelProperty(value = "收入", example = "30.00")
    private BigDecimal income;

    @ApiModelProperty(value = "支出", example = "30.00")
    private BigDecimal expenditure;

    @ApiModelProperty(value = "结算账户", example = "默认用户")
    private String settlementAccount;

    @ApiModelProperty(value = "备注信息", example = "备注信息")
    private String remark;

}
