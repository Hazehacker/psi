package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DV{
    @ApiModelProperty(value = "日期",example = "2021-01-01")
    private String date;
    @ApiModelProperty(value = "金额",example = "100.00")
    private BigDecimal value;
}