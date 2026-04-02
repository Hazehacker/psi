package com.zeroone.star.project.dto.j7.sysargs.supportinfo.inandexpend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("收支类别名称列表数据对象")
public class IetNameDTO {
    @ApiModelProperty(value = "类别id", example = "1")
    String id;
    @ApiModelProperty(value = "类别名称", example = "销售收入")
    String name;
}
