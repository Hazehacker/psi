package com.zeroone.star.reportmanagement.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 辅助属性[基础]
 * </p>
 *
 * @author imp
 * @since 2025-10-23
 */
@Getter
@Setter
@TableName("attribute")
public class Attribute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id")
    private String id;

    /**
     * 属性名称
     */
    private String name;

    /**
     * 属性排序
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 备注信息
     */
    @TableField(value = "data")
    private String data;


}
