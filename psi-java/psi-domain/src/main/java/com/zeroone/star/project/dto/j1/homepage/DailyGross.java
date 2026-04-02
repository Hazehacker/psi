package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("日销售毛利")
public class DailyGross {
    @ApiModelProperty(value = "总量", example = "4200")
    private Integer amount;
    @ApiModelProperty(value = "比值", example = "2.1")
    private Integer rate;
}
