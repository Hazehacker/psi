package com.zeroone.star.project.dto.j5.fundreport;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("客户对账表-明细项数据对象")
public class CustomerStatementDetailDTO {

    @ApiModelProperty(value = "商品名(所属商品)",example = "商品a")
    private String goods;

    @ApiModelProperty(value = "单价",example = "12345678.1234")
    private BigDecimal price ;

    @ApiModelProperty(value = "数量",example = "12345678.1234")
    private BigDecimal nums;


}
