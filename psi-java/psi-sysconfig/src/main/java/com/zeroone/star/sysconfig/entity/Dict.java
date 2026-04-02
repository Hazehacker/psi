package com.zeroone.star.sysconfig.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 字典
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Getter
@Setter
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 所属字典类型
     */
    private String tid;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典值
     */
    private String value;

    /**
     * 备注
     */
    private String remark;


}
