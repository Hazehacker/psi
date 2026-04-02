package com.zeroone.star.project.dto.j1.sysconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 描述：菜单列表
 * </p>
 * @author ye
 * @version 1.0.0
 */
@Data
@ApiModel("菜单列表")
public class MenuListDTO {

    @ApiModelProperty(value = "菜单名称",example = "首页")
    private String name;
    @ApiModelProperty(value = "菜单标识",example = "log")
    private String key;
    @ApiModelProperty(value = "菜单类型",example = "1")
    private Integer type;
    @ApiModelProperty(value = "菜单地址",example = "/home")
    private String resource;
    @ApiModelProperty(value = "菜单排序",example = "1")
    private Integer sort;
    @ApiModelProperty(value = "备注信息",example = "主页")
    private String data;

}
