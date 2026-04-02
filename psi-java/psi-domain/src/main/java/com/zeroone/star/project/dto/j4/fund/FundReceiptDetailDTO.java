package com.zeroone.star.project.dto.j4.fund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("收款单详情数据对象")
public class FundReceiptDetailDTO {
    @ApiModelProperty(value = "收款单详情id", example = "1")
    private String id;
    @ApiModelProperty(value = "结算账户", example = "10086")
    private String account;
    @ApiModelProperty(value = "结算金额", example = "100")
    private BigDecimal money;
    @ApiModelProperty(value = "结算号", example = "10086")
    private String settle;
    @ApiModelProperty(value = "备注信息", example = "无")
    private String data;
}
