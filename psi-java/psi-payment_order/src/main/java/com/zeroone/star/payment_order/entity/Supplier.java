package com.zeroone.star.payment_order.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 供应商
 * </p>
 *
 * @author 温白开
 * @since 2025-10-28
 */
@Getter
@Setter
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 供应商名称
     */
    @NotBlank(message = "供应商名称不能为空")
    private String name;

    /**
     * 拼音信息
     */
    @NotBlank(message = "拼音信息不能为空")
    private String py;

    /**
     * 供应商编号
     */
    @NotBlank(message = "供应商编号不能为空")
    private String number;

    /**
     * 所属组织
     */
    @NotBlank(message = "所属组织不能为空")
    private String frame;

    /**
     * 所属用户
     */
    @NotBlank(message = "所属用户不能为空")
    private String user;

    /**
     * 供应商类别
     */
    @NotBlank(message = "供应商类别不能为空")
    private String category;

    /**
     * 增值税税率
     */
    @NotNull(message = "增值税税率不能为空")
    @Digits(integer = 3,fraction = 2,message = "增值税税率格式不正确")
    private BigDecimal rate;

    /**
     * 开户银行
     */
    private String bank;

    /**
     * 银行账号
     */
    private String account;

    /**
     * 纳税号码
     */
    private String tax;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 联系资料
     */
    private String contacts;

    /**
     * 应付款余额
     */
    @NotNull(message = "应付款余额不能为空")
    @Digits(integer = 12,fraction = 4,message = "应付款余额格式不正确")
    private BigDecimal balance;

    /**
     * 扩展信息
     */
    private String more;


}
