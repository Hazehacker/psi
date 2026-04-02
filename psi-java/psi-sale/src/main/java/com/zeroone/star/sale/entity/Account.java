package com.zeroone.star.sale.entity;

import java.math.BigDecimal;
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
 * @since 2025-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private LocalDate time;

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
