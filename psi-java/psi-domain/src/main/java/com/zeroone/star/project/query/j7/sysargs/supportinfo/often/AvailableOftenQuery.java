package com.zeroone.star.project.query.j7.sysargs.supportinfo.often;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("获取可设置的常用功能请求参数")
public class AvailableOftenQuery {
    @ApiModelProperty(value = "用户ID", example = "1001", required = true)
    @NotBlank(message = "用户ID不能为空")
    private String userId;
}