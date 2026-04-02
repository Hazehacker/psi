package com.zeroone.star.project.query.j6.basic_information.customer_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("获取组织树")
public class queryOrganizationTree {
    @ApiModelProperty(value = "组织编号",example = "0")
    private int id;
    @ApiModelProperty(value = "组织名称",example = "默认组织")
    private String name;

}
