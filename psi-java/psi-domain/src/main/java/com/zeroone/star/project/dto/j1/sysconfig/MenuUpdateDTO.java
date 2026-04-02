package com.zeroone.star.project.dto.j1.sysconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * <p>
 * 描述：修改目录数据对象
 * </p>
 * @author hui_cheng12
 * @version 1.0.0
 */
@Data
@ApiModel("目录类型传输对象")
public class MenuUpdateDTO {
    /**
     * 目录id
     */
    @ApiModelProperty(value = "目录id",example = "70", required = true)
    @NotBlank(message = "未识别到当前目录id")
    private String id;

    /**
     * 所属菜单id
     */
    @ApiModelProperty(value = "所属菜单id",example = "11",required = true)
    @NotBlank(message = "未识别到所属菜单的id")
    private String pid;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称",example = "客户管理",required = true)
    @NotBlank(message = "未识别到菜单名称")
    private String name;

    /**
     * 菜单标识
     */
    @ApiModelProperty(value = "菜单标识",example = "system",required = true)
    @NotBlank(message = "未识别到菜单标识")
    private String key;


    /**
     * 菜单模式
     * [0:标签模式|1:新页模式]
     */
    @ApiModelProperty(value = "菜单模式([0:标签模式|1:新页模式])",example = "0",required = true)
    @NotBlank(message = "未识别到菜单模式")
    private Integer model;

    /**
     * 菜单类型
     * [0:独立菜单|1:附属菜单]
     */
    @ApiModelProperty(value = "菜单类型([0:独立菜单|1:附属菜单])",example = "1",required = true)
    @NotBlank(message = "未识别到菜单类型")
    private Integer type;

    /**
     * 菜单地址
     */
    @ApiModelProperty(value = "菜单地址",example = "/system/customer",required = true)
    @NotBlank(message = "未识别到菜单地址")
    private String resource;

    /**
     * 菜单排序
     */
    @ApiModelProperty(value = "菜单排序",example = "1")
    private Integer sort;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标",example = "el-icon-menu",required = true)
    @NotBlank(message = "未识别到菜单图标")
    private String ico;

    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识",example = "admin",required = true)
    @NotBlank(message = "未识别到权限标识")
    private String root;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息",example = "管理专属",required = true)
    @NotBlank(message = "未识别到备注信息")
    private String data;
}
