package com.zeroone.star.reportmanagement.entity;

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
 * @author imp
 * @since 2025-10-23
 */
@Getter
@Setter
@TableName("attribute_info")
public class AttributeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id")
    private String id;

    /**
     * 所属属性
     */
    private String pid;

    /**
     * 属性名称
     */
    private String name;


}
