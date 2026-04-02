package com.zeroone.star.project.vo.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "BalanceVo", description = "余额数据")
public class BalanceVO {

    @ApiModelProperty(value = "资产名称",example = "资产余额")
    private String label;
    @ApiModelProperty(value = "资产数据",example = "1000元")
    private String value;
    @ApiModelProperty(value = "跳转路径",example = "/assets/balance")
    private String to;
}
