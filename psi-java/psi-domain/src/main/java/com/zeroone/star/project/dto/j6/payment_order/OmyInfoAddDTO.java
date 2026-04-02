package com.zeroone.star.project.dto.j6.payment_order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OmyInfoAddDTO {
    @ApiModelProperty(value = "所属单据")
    private String pid;

    @NotBlank(message = "数据表格第1行结算账户不可为空")
    @ApiModelProperty(value = "结算账户",example = "中创资金")
    private String account;

    @NotNull(message="数据表格第1行结算金额不正确")
    @ApiModelProperty(value = "结算金额",example = "1000")
    private BigDecimal money;

    //@NotBlank(message="结算号不能为空")
    @ApiModelProperty(value = "结算号",example = "S111")
    private String settle;

    @ApiModelProperty(value = "备注信息",example = "新增付款单示例")
    private String data1;
}
