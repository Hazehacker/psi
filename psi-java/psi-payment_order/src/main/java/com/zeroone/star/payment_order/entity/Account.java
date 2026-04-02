package com.zeroone.star.payment_order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 资金账户
 * </p>
 *
 * @author 温白开
 * @since 2025-10-28
 */
@Getter
@Setter
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 账户名称
     */
    @NotBlank(message = "账户名称不能为空")
    private String name;

    /**
     * 账户编号
     */
    @NotBlank(message = "账户编号不能为空")
    private String number;

    /**
     * 所属组织
     */
    @NotBlank(message = "所属不能为空")
    private String frame;

    /**
     * 余额日期
     */
    @NotNull(message = "余额日期不能为空")
    private LocalDate time;

    /**
     * 期初余额
     */
    @NotNull(message = "期初余额不能为空")
    @Digits(integer = 12,fraction = 4,message = "期初余额格式不正确")
    private BigDecimal initial;

    /**
     * 账户余额
     */
    @NotNull(message = "账户余额不能为空")
    @Digits(integer = 12,fraction = 4,message = "账户余额格式不正确")
    private BigDecimal balance;

    /**
     * 备注信息
     */
    private String data;


}
