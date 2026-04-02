package com.zeroone.star.project.query.j7.sysargs.supportinfo.often;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询用户常用功能对象
 */
@Data
@ApiModel("查询用户常用功能的参数")
public class UserOftenQuery extends PageQuery {
    @ApiModelProperty(value = "用户ID", example = "1001", required = true)
    private String userId;
}