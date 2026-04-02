package com.zeroone.star.supportinfo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 菜单信息
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
@Getter
@Setter
@TableName("menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 所属菜单
     */
    @TableField("pid")
    private String pid;

    /**
     * 菜单名称
     */
    @TableField("name")
    private String name;

    /**
     * 菜单标识
     */
    @TableField("`key`")
    private String key;

    /**
     * 菜单模式[0:标签模式|1:新页模式]
     */
    @TableField("model")
    private Boolean model;

    /**
     * 菜单类型[0:独立菜单|1:附属菜单]
     */
    @TableField("type")
    private Boolean type;

    /**
     * 菜单地址
     */
    @TableField("resource")
    private String resource;

    /**
     * 菜单排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 菜单图标
     */
    @TableField("ico")
    private String ico;

    /**
     * 权限标识
     */
    @TableField("root")
    private String root;

    /**
     * 备注信息
     */
    @TableField("`data`")
    private String data;



}
