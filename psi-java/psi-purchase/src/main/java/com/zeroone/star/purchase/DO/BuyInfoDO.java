package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.NoArgsConstructor;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 小阳
 * @date 2025/10/24 16:02
 * @description 采购单详情实体类，对应数据库buy_info表
 * Version: 1.0
 */

@Getter
@Setter
@TableName("buy_info")

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyInfoDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", required = true)
    @NotBlank(message = "主键ID不能为空")
    @Size(max = 32, message = "主键ID长度不能超过32个字符")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "所属ID", required = true)
    @NotBlank(message = "所属ID不能为空")
    @Size(max = 32, message = "所属ID长度不能超过32个字符")
    @TableField("pid")
    private String pid;

    @ApiModelProperty(value = "关联详情|BOR")
    @Size(max = 32, message = "关联详情长度不能超过32个字符")
    @TableField("source")
    private String source;

    @ApiModelProperty(value = "所属商品", required = true)
    @NotBlank(message = "所属商品不能为空")
    @Size(max = 32, message = "所属商品长度不能超过32个字符")
    @TableField("goods")
    private String goods;

    @ApiModelProperty(value = "辅助属性")
    @Size(max = 64, message = "辅助属性长度不能超过64个字符")
    @TableField("attr")
    private String attr;

    @ApiModelProperty(value = "单位", required = true)
    @NotBlank(message = "单位不能为空")
    @Size(max = 32, message = "单位长度不能超过32个字符")
    @TableField("unit")
    private String unit;

    @ApiModelProperty(value = "仓库")
    @Size(max = 32, message = "仓库长度不能超过32个字符")
    @TableField("warehouse")
    private String warehouse;

    @ApiModelProperty(value = "批次号")
    @Size(max = 32, message = "批次号长度不能超过32个字符")
    @TableField("batch")
    private String batch;

    @ApiModelProperty(value = "生产日期")
    @TableField("mfd")
    private LocalDate mfd;

    @ApiModelProperty(value = "单价", required = true)
    @NotNull(message = "单价不能为空")
    @DecimalMin(value = "0.0000", message = "单价不能小于0")
    @Digits(integer = 8, fraction = 4, message = "单价整数位不能超过8位，小数位不能超过4位")
    @TableField("price")
    private BigDecimal price;

    @ApiModelProperty(value = "数量", required = true)
    @NotNull(message = "数量不能为空")
    @DecimalMin(value = "0.0000", message = "数量不能小于0")
    @Digits(integer = 8, fraction = 4, message = "数量整数位不能超过8位，小数位不能超过4位")
    @TableField("nums")
    private BigDecimal nums;

    @ApiModelProperty(value = "序列号")
    @TableField("serial")
    private String serial;

    @ApiModelProperty(value = "折扣率", required = true)
    @NotNull(message = "折扣率不能为空")
    @DecimalMin(value = "0.00", message = "折扣率不能小于0")
    @Digits(integer = 3, fraction = 2, message = "折扣率整数位不能超过3位，小数位不能超过2位")
    @TableField("discount")
    private BigDecimal discount = BigDecimal.ZERO;

    @ApiModelProperty(value = "折扣额", required = true)
    @NotNull(message = "折扣额不能为空")
    @DecimalMin(value = "0.0000", message = "折扣额不能小于0")
    @Digits(integer = 8, fraction = 4, message = "折扣额整数位不能超过8位，小数位不能超过4位")
    @TableField("dsc")
    private BigDecimal dsc = BigDecimal.ZERO;

    @ApiModelProperty(value = "金额", required = true)
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.0000", message = "金额不能小于0")
    @Digits(integer = 8, fraction = 4, message = "金额整数位不能超过8位，小数位不能超过4位")
    @TableField("total")
    private BigDecimal total;

    @ApiModelProperty(value = "税率", required = true)
    @NotNull(message = "税率不能为空")
    @DecimalMin(value = "0.00", message = "税率不能小于0")
    @Digits(integer = 3, fraction = 2, message = "税率整数位不能超过3位，小数位不能超过2位")
    @TableField("tax")
    private BigDecimal tax = BigDecimal.ZERO;

    @ApiModelProperty(value = "税额", required = true)
    @NotNull(message = "税额不能为空")
    @DecimalMin(value = "0.0000", message = "税额不能小于0")
    @Digits(integer = 8, fraction = 4, message = "税额整数位不能超过8位，小数位不能超过4位")
    @TableField("tat")
    private BigDecimal tat = BigDecimal.ZERO;

    @ApiModelProperty(value = "价税合计", required = true)
    @NotNull(message = "价税合计不能为空")
    @DecimalMin(value = "0.0000", message = "价税合计不能小于0")
    @Digits(integer = 8, fraction = 4, message = "价税合计整数位不能超过8位，小数位不能超过4位")
    @TableField("tpt")
    private BigDecimal tpt;

    @ApiModelProperty(value = "备注信息")
    @Size(max = 256, message = "备注信息长度不能超过256个字符")
    @TableField("data")
    private String data;

    @ApiModelProperty(value = "退货数量")
    @DecimalMin(value = "0.0000", message = "退货数量不能小于0")
    @Digits(integer = 8, fraction = 4, message = "退货数量整数位不能超过8位，小数位不能超过4位")
    @TableField("retreat")
    private BigDecimal retreat = BigDecimal.ZERO;
}
