package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户
 * @author rainsilent
 * @date 2025/10/22
 */
@Getter
@Setter
@TableName("user")
public class UserDO {
    /**
     * 唯一id
     */
    @TableId(value = "id")
    private String id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 拼音信息
     */
    private String py;
    /**
     * 手机号码
     */
    private String tel;
    /**
     * 所属组织
     */
    private String frame;
    /**
     * 所属角色
     */
    private String role;
    /**
     * 登陆账号
     */
    private String user;
    /**
     * 登陆密码
     */
    private String pwd;
    /**
     * 用户头像
     */
    private String img;
    /**
     * token
     */
    private String token;
    /**
     * 密钥时效
     */
    private Integer expire;
    /**
     * 备注信息
     */
    private String data;
    /**
     * 拓展信息
     */
    private String more;
}
