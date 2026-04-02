package com.zeroone.star.homepage.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 常用功能
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Getter
@Setter
public class Often implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 所属用户
     */
    private String user;

    /**
     * 功能名称
     */
    private String name;

    /**
     * 功能标识
     */
    private String key;


}
