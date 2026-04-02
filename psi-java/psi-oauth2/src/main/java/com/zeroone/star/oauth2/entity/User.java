package com.zeroone.star.oauth2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 * @author 阿伟
 */
@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 用户名称
     */
    private String name;

    private String py;

    private String tel;

    private String frame;

    /**
     * 登录账号
     */
    private String user;

    /**
     * 密码
     */
    private String pwd;

    private String role;

    private String img;

    private String token;

    private Integer expire;

    private String data;

    private String more;

}
