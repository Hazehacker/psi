package com.zeroone.star.project.dto.j1.sysconfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("菜单新增数据对象")
public class MenuAddDTO {
    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称",example = "客户管理",required = true)
    @NotBlank(message = "菜单名称不为空")
    private String name;

    /**
     * 菜单标识
     */
    @ApiModelProperty(value = "菜单标识",example = "system",required = true)
    @NotBlank(message = "菜单标识不应为空")
    private String key;

    /**
     * 父菜单id
     */
    @ApiModelProperty(value = "父菜单id",example = "1",required = true)
    @NotNull(message = "父菜单id不应为空")
    private String pid;

    /**
     * 菜单模式
     *
     */
    @ApiModelProperty(value = "菜单模式[0:标签模式|1:新页模式]",example = "0",required = true)
    private Integer model;

    /**
     * 菜单类型
     *
     */
    @ApiModelProperty(value = "菜单类型[0:独立菜单|1:附属菜单]",example = "0",required = true)
    private Integer type;

    /**
     * 菜单地址
     */
    @ApiModelProperty(value = "菜单地址",example = "/shop",required = true)
    private String resource;

    /**
     * 菜单排序
     */
    @ApiModelProperty(value = "菜单排序",example = "1",required = true)
    @NotNull(message = "菜单排序不能为空")
    private Integer sort;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标",example = "el-icon-shopping-cart-2",required = false)
    private String ico;

    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识",example = "admin",required = false)
    private String root;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息",example = "辅助资料",required = false)
    private String data;
}
