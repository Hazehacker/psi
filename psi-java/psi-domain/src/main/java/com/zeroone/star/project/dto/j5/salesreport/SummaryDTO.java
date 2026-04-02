package com.zeroone.star.project.dto.j5.salesreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 描述：用于对销售汇总表中汇总部分的单独封装
 */
@Data
@ApiModel(value = "汇总对象")
public class SummaryDTO {
    @ApiModelProperty(value = "总数量",example = "80")
    private Integer totalQuantity;

    @ApiModelProperty(value = "总金额",example = "479920.00")
    private BigDecimal totalAmount;
}
