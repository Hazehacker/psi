package com.zeroone.star.basic_information.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("is_user")
@Data
public class User {
    // 用户id
    private Integer id;
    // 用户名称
    private String name;
    // 用户拼音
    private String py;
    // 用户电话
    private String tel;
    // 用户组织
    private Integer frame;
    // 用户角色
    private Integer role;
    // 用户账号
    private String user;
    // 用户密码
    private String pwd;
}
