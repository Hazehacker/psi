package com.zeroone.star.project.vo.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel(value = "AccountBalanceVO", description = "返回账户数据")
public class AccountBalanceVO {
    @ApiModelProperty(value = "账户名称",example ="测试账户")
    private String name;
    @ApiModelProperty(value = "账户余额",example = "1000")
    private BigDecimal value;
}
