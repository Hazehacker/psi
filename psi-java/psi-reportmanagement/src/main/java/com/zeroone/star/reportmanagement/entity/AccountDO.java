package com.zeroone.star.reportmanagement.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资金账户数据对象
 *
 * @author toexpl
 * @since 2025/10/23
 */

@TableName("account")
@Getter
@Setter
public class AccountDO {

    /**
     * 唯一id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 账户名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 账户编号
     */
    @TableField(value = "number")
    private String number;

    /**
     * 所属组织
     */
    @TableField(value = "frame")
    private String frame;

    /**
     * 余额日期
     */
    @TableField(value = "time")
    private LocalDateTime time;

    /**
     * 期初日期
     */
    @TableField(value = "initial")
    private BigDecimal initial;

    /**
     * 账户余额
     */
    @TableField(value = "balance")
    private BigDecimal balance;

    /**
     * 备注信息
     */
    @TableField(value = "data")
    private String data;

}
