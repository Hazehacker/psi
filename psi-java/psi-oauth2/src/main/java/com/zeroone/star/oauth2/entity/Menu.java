package com.zeroone.star.oauth2.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 菜单信息
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Getter
@Setter
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 所属菜单
     */
    private String pid;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单标识
     */
    private String key;

    /**
     * 菜单模式[0:标签模式|1:新页模式]
     */
    private Boolean model;

    /**
     * 菜单类型[0:独立菜单|1:附属菜单]
     */
    private Boolean type;

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
