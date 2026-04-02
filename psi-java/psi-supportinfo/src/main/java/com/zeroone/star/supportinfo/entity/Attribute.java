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
 * 辅助属性[基础]
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
@Getter
@Setter
@TableName("attribute")
public class Attribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
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
