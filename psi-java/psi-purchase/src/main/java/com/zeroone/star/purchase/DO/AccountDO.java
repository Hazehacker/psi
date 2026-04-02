package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: a
 * @CreateTime: 2025-10-25
 * @Description: 账户实体类
 * @Version: 1.0
 */
@Getter
@Setter
@TableName("account")
public class AccountDO {

    /**
     * 账户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 账户名称
     */
    private String name;

    /**
     * 账户编号
     */
    private String number;

    /**
     * 所属组织
     */
    private String frame;

    /**
     * 余额日期
     */
    private Date time;

    /**
     * 期初余额
     */
    private BigDecimal initial;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 备注信息
     */
    private String data;
}
