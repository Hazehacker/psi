package com.zeroone.star.payment_order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 付款单核销详情
 * </p>
 *
 * @author 温白开
 * @since 2025-10-24
 */
@Getter
@Setter
@TableName("omy_bill")
public class OmyBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属单据
     */
    @NotBlank(message = "所属单据不能为空")
    private String pid;

    /**
     * 核销类型
     */
    @NotBlank(message = "核销类型不能为空")
    private String type;

    /**
     * 关联单据
     */
    @NotBlank(message = "关联单据不能为空")
    private String source;

    /**
     * 单据时间
     */
    @NotNull(message = "核销时间不能为空")
    private LocalDate time;

    /**
     * 核销金额
     */
    @NotNull(message = "核销金额不能为空")
    //@DecimalMin(value = "0",message = "核销金额不能为负数")
    @Digits(integer = 12,fraction = 4,message = "核销金额格式不正确")
    private BigDecimal money;


}
