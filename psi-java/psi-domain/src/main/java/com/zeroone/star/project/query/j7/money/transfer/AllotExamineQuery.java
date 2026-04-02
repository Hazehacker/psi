package com.zeroone.star.project.query.j7.money.transfer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 类名：AllotExamineQuery
 * 包名：com.zeroone.star.project.query.j7.money.transfer
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/22
 * 版本号：V1.0
 */
@Data
@ApiModel("审核状态参数")
public class AllotExamineQuery {
    @NotNull(message = "审核状态不能为空")
    @ApiModelProperty(value = "审核状态 0：未审核 1：已审核",example = "1",required = true)
    private Integer examine;
}
