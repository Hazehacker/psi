package com.zeroone.star.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author author
 * @since 2025-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 用户名称
     */
    @TableField("name")
    private String name;

    /**
     * 拼音信息
     */
    @TableField("py")
    private String py;

    /**
     * 手机号码
     */
    @TableField("tel")
    private String tel;

    /**
     * 所属组织
     */
    @TableField("frame")
    private String frame;

    /**
     * 所属角色
     */
    @TableField("role")
    private String role;

    /**
     * 登陆账号
     */
    @TableField("user")
    private String user;

    /**
     * 登陆密码
     */
    @TableField("pwd")
    private String pwd;

    /**
     * 用户头像
     */
    @TableField("img")
    private String img;

    /**
     * 秘钥信息
     */
    @TableField("token")
    private String token;

    /**
     * 秘钥时效
     */
    @TableField("expire")
    private Integer expire;

    /**
     * 备注信息
     */
    @TableField("data")
    private String data;

    /**
     * 扩展信息
     */
    @TableField("more")
    private String more;


}
