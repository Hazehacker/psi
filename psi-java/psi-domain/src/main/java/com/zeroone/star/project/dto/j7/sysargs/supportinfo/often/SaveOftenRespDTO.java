package com.zeroone.star.project.dto.j7.sysargs.supportinfo.often;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 保存用户常用功能的响应
 */
@Data
@ApiModel("保存用户常用功能的响应数据对象")
public class SaveOftenRespDTO {
    @ApiModelProperty(value = "操作是否成功", example = "true")
    private Boolean success;

    @ApiModelProperty(value = "提示信息", example = "保存成功!")
    private String message;
}