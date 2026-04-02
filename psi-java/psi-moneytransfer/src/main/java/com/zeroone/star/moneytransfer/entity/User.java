package com.zeroone.star.moneytransfer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 *  精简版的user类
 */
@Data
public class User {
    @TableId(value ="id",type = IdType.ASSIGN_ID)
    String id;
    @TableField("name")
    private String name;
    @TableField("frame")
    private String frame;
}