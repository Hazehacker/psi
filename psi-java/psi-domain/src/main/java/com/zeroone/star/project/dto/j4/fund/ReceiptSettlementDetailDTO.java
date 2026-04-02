package com.zeroone.star.project.dto.j4.fund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("收款单结算明细DTO")
public class ReceiptSettlementDetailDTO {
    @ApiModelProperty(value = "结账用户",example = "1")
    private String account;
    
    @ApiModelProperty(value = "金额",example = "1")
    private BigDecimal amount;
    
    @ApiModelProperty(value = "结算号",example = "1")
    private String settle;
    
    @ApiModelProperty(value = "备注",example = "1")
    private String data;
}
