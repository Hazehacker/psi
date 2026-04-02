package com.zeroone.star.project.dto.j7.sysargs.supportinfo.inandexpend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("收支类别修改数据对象")
public class IetDTO {
    @ApiModelProperty(value = "类别名称", example = "销售收入")
    @NotBlank(message = "类别名称不能为空")
    private String name;
    @ApiModelProperty(value = "收支类型", example = "0")
    @NotBlank(message = "收支类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "类别排序", example = "1")
    @NotNull(message = "类别排序不能为空")
    private Integer sort;
    @ApiModelProperty(value = "备注信息", example = "提供服务所得收入")
    private String data;
}
