package com.zeroone.star.project.dto.j6.basic_information.customer_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户查询数据传输对象")
public class userDto {
    @ApiModelProperty(value = "用户名称",example = "管理员")
    private String name;
    @ApiModelProperty(value = "用户账号",example = "admin")
    private String user;
    @ApiModelProperty(value = "组织",example = "默认组织")
    private String frame;
    @ApiModelProperty(value = "用户角色",example = "管理员")
    private String role;
    @ApiModelProperty(value = "用户电话",example = "12345678910")
    private String tel;
    @ApiModelProperty(value = "用户备注",example = "任意备注")
    private String data;
}
