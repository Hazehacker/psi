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
 * 属性详情表
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
@Getter
@Setter
@TableName("attribute_info")
public class AttributeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属属性
     */
    @TableField("pid")
    private String pid;

    /**
     * 属性名称
     */
    @TableField("name")
    private String name;


}
