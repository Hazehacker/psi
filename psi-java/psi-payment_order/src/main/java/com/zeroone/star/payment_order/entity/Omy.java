package com.zeroone.star.payment_order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.*;

/**
 * <p>
 * 付款单
 * </p>
 *
 * @author 温白开
 * @since 2025-10-24
 */
@Getter
@Setter
@TableName("omy")
public class Omy implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属组织
     */
    @TableField(fill = FieldFill.INSERT)
    @NotBlank(message = "所属组织不能为空")
    private String frame;

    /**
     * 供应商
     */
    //@NotBlank(message = "供应商不能为空")
    private String supplier;

    /**
     * 单据时间
     */
    @NotNull(message = "单据时间不能为空")
    //@Future(message = "单据日期必须大于结账日期")
    private LocalDate time;

    /**
     * 单据编号
     */
    @NotBlank(message = "单据编号不能为空")
    private String number;

    /**
     * 单据金额
     */
    @NotNull(message = "单据金额不能为空")
    @DecimalMin(value = "0.01",message = "单据金额必须大于0")
    @Digits(integer = 12,fraction = 4,message = "单据金额格式不正确")
    private BigDecimal total;

    /**
     * 关联人员
     */
    private String people;

    /**
     * 单据附件
     */
    private String file;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 扩展信息
     */
    private String more;

    /**
     * 审核状态[0:未审核|1:已审核]
     */
    @TableField(fill = FieldFill.INSERT)
    @NotNull(message = "审核状态不能为空")
    @Range(min = 0,max = 1,message = "审核状态值不正确")
    private Integer examine;

    /**
     * 核销状态[0:未核销|1:部分核销|2:已核销]
     */
    @TableField(fill = FieldFill.INSERT)
    @NotNull(message = "核销状态不能为空")
    @Range(min = 0,max = 2,message = "核销状态值不正确")
    private Integer nucleus;

    /**
     * 制单人
     */
    @TableField(fill = FieldFill.INSERT)
    @NotBlank(message = "制单人不能为空")
    private String user;



}
