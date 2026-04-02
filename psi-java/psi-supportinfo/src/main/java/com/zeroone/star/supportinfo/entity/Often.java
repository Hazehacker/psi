package com.zeroone.star.supportinfo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("often")
public class Often implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属用户
     */
    @TableField("user")
    private String user;

    /**
     * 功能名称
     */
    @TableField("name")
    private String name;

    /**
     * 功能标识
     */
    @TableField("`key`")
    private String key;
}
