package com.zeroone.star.payment_order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 付款单详情
 * </p>
 *
 * @author 温白开
 * @since 2025-10-24
 */
@Getter
@Setter
@TableName("omy_info")
public class OmyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属ID
     */
    @NotBlank(message = "所属ID不能为空")
    private String pid;

    /**
     * 结算账户
     */
    //@NotBlank(message = "结算账户不能为空")
    private String account;

    /**
     * 结算金额
     */
    @NotNull(message = "结算金额不能为空")
    @DecimalMin(value = "0.01",message = "结算金额必须大于0")
    @Digits(integer = 12,fraction = 4,message = "结算金额格式不正确")
    private BigDecimal money;

    /**
     * 结算号
     */
    @NotBlank(message = "结算号不能为空")
    private String settle;

    /**
     * 备注信息
     */
    @TableField(value="data")
    private String data1;


}
