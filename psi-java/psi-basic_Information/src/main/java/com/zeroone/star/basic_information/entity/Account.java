package com.zeroone.star.basic_information.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 描述: 资金账户实体
 * </p>
 *
 * @author goldenHour
 * @version 1.0.0
 */
@Data
@TableName("account")
@Builder
public class Account {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
     * 余额
     */
    private BigDecimal balance;

    /**
     * 备注信息
     */
    private String data;
}
