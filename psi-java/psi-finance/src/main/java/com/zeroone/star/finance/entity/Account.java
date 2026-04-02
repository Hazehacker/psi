package com.zeroone.star.finance.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 资金账户
 * </p>
 *
 * @author author
 * @since 2025-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 账户名称
     */
    @TableField("name")
    private String name;

    /**
     * 账户编号
     */
    @TableField("number")
    private String number;

    /**
     * 所属组织
     */
    @TableField("frame")
    private String frame;

    /**
     * 余额日期
     */
    @TableField("time")
    private LocalDate time;

    /**
     * 期初余额
     */
    @TableField("initial")
    private BigDecimal initial;

    /**
     * 账户余额
     */
    @TableField("balance")
    private BigDecimal balance;

    /**
     * 备注信息
     */
    @TableField("data")
    private String data;


}
