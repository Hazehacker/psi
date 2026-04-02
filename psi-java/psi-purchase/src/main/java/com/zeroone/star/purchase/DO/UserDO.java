package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户表 实体类
 * 对应表名：user
 */
@Getter
@Setter
@TableName("user") // 指定对应的数据表名
public class UserDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
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
     * 所属组织（默认值：0）
     */
    private String frame = "0";

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
     * 秘钥信息
     */
    private String token;

    /**
     * 秘钥时效
     */
    private Integer expire;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 扩展信息
     */
    private String more;
}