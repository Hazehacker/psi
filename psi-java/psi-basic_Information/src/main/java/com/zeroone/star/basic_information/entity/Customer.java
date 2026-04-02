package com.zeroone.star.basic_information.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("customer")
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private String id;

    /**
     * 客户名称
     */
    @TableField("name")
    private String name;

    /**
     * 拼音信息
     */
    @TableField("py")
    private String py;

    /**
     * 客户编号
     */
    @TableField("number")
    private String number;

    /**
     * 所属组织
     */
    @TableField("frame")
    private String frame;

    /**
     * 所属用户
     */
    @TableField("user")
    private String user;

    /**
     * 客户类别
     */
    @TableField("category")
    private String category;

    /**
     * 客户等级
     */
    @TableField("grade")
    private String grade;

    /**
     * 开户银行
     */
    @TableField("bank")
    private String bank;

    /**
     * 银行账号
     */
    @TableField("account")
    private String account;

    /**
     * 纳税号码
     */
    @TableField("tax")
    private String tax;

    /**
     * 备注信息
     */
    @TableField("data")
    private String data;

    /**
     * 联系资料
     */
    @TableField("contacts")
    private String contacts;

    /**
     * 应付款余额
     */
    @TableField("balance")
    private Double balance;

    /**
     * 扩展信息
     */
    @TableField("more")
    private String more;

}
