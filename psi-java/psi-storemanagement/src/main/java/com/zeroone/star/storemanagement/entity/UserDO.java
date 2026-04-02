package com.zeroone.star.storemanagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "user")
public class UserDO {
    /**
     * 用户编号
     */
    private String id;
    /**
     * 用户名称
     */
    private String name;

    private String py;

    private String tel;

    private String frame;

    private String role;
}

