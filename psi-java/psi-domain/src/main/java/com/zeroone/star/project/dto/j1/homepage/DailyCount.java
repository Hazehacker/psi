package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("日销售笔数")
public class DailyCount {
    @ApiModelProperty(value = "总量", example = "86")
    private Integer amount;
    @ApiModelProperty(value = "比值", example = "12")
    private Integer rate;
}
