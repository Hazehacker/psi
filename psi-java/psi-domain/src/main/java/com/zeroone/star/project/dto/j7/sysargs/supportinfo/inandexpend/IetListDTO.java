package com.zeroone.star.project.dto.j7.sysargs.supportinfo.inandexpend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("收支类别列表数据对象")
public class IetListDTO extends IetDetailDTO {
    @ApiModelProperty(value = "类别id", example = "1")
    private String id;
    @ApiModelProperty(value = "类别名称", example = "销售收入")
    private String name;
    @ApiModelProperty(value = "收支类型", example = "0")
    private Integer type;
    @ApiModelProperty(value = "类别排序", example = "1")
    private Integer sort;
    @ApiModelProperty(value = "备注信息", example = "提供服务所得收入")
    private String data;
}
