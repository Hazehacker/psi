package com.zeroone.star.project.dto.j5.salesreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 描述：用于表示含单价、数量、金额的通用结果
 */
@Data
@ApiModel("销售项信息(单价、数量、金额)")
public class SalesItemDTO {
    @ApiModelProperty(value = "单价",example = "99.8")
    private BigDecimal price;

    @ApiModelProperty(value = "数量",example = "10")
    private Integer quantity;

    @ApiModelProperty(value = "金额",example = "2999.00")
    private BigDecimal amount;
}
