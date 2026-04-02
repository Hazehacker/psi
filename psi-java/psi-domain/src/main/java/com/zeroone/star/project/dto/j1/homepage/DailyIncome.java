package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("日资金收入")
public class DailyIncome {
    @ApiModelProperty(value = "总量", example = "8900")
    private BigDecimal amount;
    @ApiModelProperty(value = "比值", example = "58")
    private Integer rate;
}