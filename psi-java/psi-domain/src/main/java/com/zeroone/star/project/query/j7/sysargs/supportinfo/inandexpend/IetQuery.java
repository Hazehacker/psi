package com.zeroone.star.project.query.j7.sysargs.supportinfo.inandexpend;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("收支类别分页查询类别")
public class IetQuery extends PageQuery {
    @ApiModelProperty(value = "类别名称", example = "销售收入")
    private String name;
    @ApiModelProperty(value = "收支类型", example = "0")
    private Integer type;
}
