package com.zeroone.star.moneytransfer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 资金账户
 * </p>
 *
 * @author hh
 * @since 2025-10-29
 */
@Getter
@Setter
@TableName("account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
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
