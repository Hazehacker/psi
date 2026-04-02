package com.zeroone.star.basic_information.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 属性实体类
 * 
 * @author 杨潇
 * @since 2025-10-20
 */
@Data
@TableName("attribute")
public class Attribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 属性名称
     */
    @TableField("name")
    private String name;

    /**
     * 属性排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 备注信息
     */
    @TableField("data")
    private String data;
}
