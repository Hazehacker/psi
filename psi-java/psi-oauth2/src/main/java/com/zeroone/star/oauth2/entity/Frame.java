package com.zeroone.star.oauth2.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 组织机构
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-30
 */
@Getter
@Setter
public class Frame implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 所属ID
     */
    private String pid;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织排序
     */
    private Integer sort;

    /**
     * 备注信息
     */
    private String data;


}
