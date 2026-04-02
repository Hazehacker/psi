package com.zeroone.star.project.dto.j2.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("LogisticsDTO")
public class LogisticsDTO {
    @ApiModelProperty(value = "物流id", example = "shunfeng")
    private String key;
    @ApiModelProperty(value = "物流名称", example = "顺丰")
    private String name;
    @ApiModelProperty(value = "物流单号", example = "111")
    private String number;
}
