package com.zeroone.star.finance.entity;

import java.math.BigDecimal;

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
 * 供应商
 * </p>
 *
 * @author author
 * @since 2025-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("supplier")
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 供应商名称
     */
    @TableField("name")
    private String name;

    /**
     * 拼音信息
     */
    @TableField("py")
    private String py;

    /**
     * 供应商编号
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
     * 供应商类别
     */
    @TableField("category")
    private String category;

    /**
     * 增值税税率
     */
    @TableField("rate")
    private BigDecimal rate;

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
    private BigDecimal balance;

    /**
     * 扩展信息
     */
    @TableField("more")
    private String more;


}
