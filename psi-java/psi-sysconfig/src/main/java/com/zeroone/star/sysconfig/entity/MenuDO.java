package com.zeroone.star.sysconfig.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;


/**
 * <p>
 * 描述：修改目录数据对象
 * </p>
 * @author hui_cheng12
 * @version 1.0.0
 */
@Data
@TableName("menu")
@ToString
public class MenuDO {
    /**
     * 目录id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属菜单id
     */
    private String pid;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单标识
     */
    @TableField(value = "`key`")
    private String key;


    /**
     * 菜单模式
     * [0:标签模式|1:新页模式]
     */
    private Integer model;

    /**
     * 菜单类型
     * [0:独立菜单|1:附属菜单]
     */
    private Integer type;

    /**
     * 菜单地址
     */
    private String resource;

    /**
     * 菜单排序
     */
    private Integer sort;

    /**
     * 菜单图标
     */
    private String ico;

    /**
     * 权限标识
     */
    private String root;

    /**
     * 备注信息
     */
    private String data;
}

