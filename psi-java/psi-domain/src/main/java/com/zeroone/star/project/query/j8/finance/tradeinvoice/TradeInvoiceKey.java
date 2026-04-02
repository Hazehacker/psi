package com.zeroone.star.project.query.j8.finance.tradeinvoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Data
@ApiModel("发票导出-Key")
public class TradeInvoiceKey {
    @ApiModelProperty(value = "单据类型", required = true, allowableValues = "buy,bre,sell,sre", example = "sell")
    @NotBlank(message = "type不能为空")
    @Pattern(regexp = "(?i)buy|bre|sell|sre", message = "type仅支持 buy/bre/sell/sre")
    private String type;

    @ApiModelProperty(value = "主单据ID", required = true, example = "SEL0002")
    @NotBlank(message = "id不能为空")
    private String id;
}