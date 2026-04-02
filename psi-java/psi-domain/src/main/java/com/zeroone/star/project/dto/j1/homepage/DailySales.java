package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("日销售额")
public class DailySales {
    @ApiModelProperty(value = "总量", example = "10500")
    private Integer amount;
    @ApiModelProperty(value = "比值", example = "35")
    private Integer rate;
}
