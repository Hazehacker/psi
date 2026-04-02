package com.zeroone.star.project.vo.j1.sysconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("菜单详情数据对象")
public class MenuDetailVO {
    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id")
    private String id;
    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String name;

    /**
     * 菜单标识
     */
    @ApiModelProperty(value = "菜单标识")
    private String key;

    /**
     * 父菜单id
     */
    @ApiModelProperty(value = "父菜单id")
    private String pid;

    /**
     * 菜单模式
     *
     */
    @ApiModelProperty(value = "菜单模式[0:标签模式|1:新页模式]")
    private Integer model;

    /**
     * 菜单类型
     *
     */
    @ApiModelProperty(value = "菜单类型[0:独立菜单|1:附属菜单]")
    private Integer type;

    /**
     * 菜单地址
     */
    @ApiModelProperty(value = "菜单地址")
    private String resource;

    /**
     * 菜单排序
     */
    @ApiModelProperty(value = "菜单排序")
    private Integer sort;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    private String ico;

    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识")
    private String root;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String data;
}
