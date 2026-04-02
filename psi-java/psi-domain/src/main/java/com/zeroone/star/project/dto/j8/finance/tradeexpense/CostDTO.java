package com.zeroone.star.project.dto.j8.finance.tradeexpense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "CostDTO", description = "获取结算单数据DTO")
public class CostDTO {
    @ApiModelProperty(value = "cost主键")
    private String id;

    @ApiModelProperty(value = "支出类别",example = "生活")
    private String type;

    @ApiModelProperty(value = "结算金额",example = "生活")
    private BigDecimal money;

    @ApiModelProperty(value = "备注信息",example = "生活")
    private String data;
}
