package com.zeroone.star.report.entity.do_KazamataNeri;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 采购退货单详情
 * </p>
 *
 * @author KazamataNeri
 * @since 2025-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bre_info")
@ApiModel(value="BreInfo对象", description="采购退货单详情")
public class BreInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "所属ID")
    @TableField("pid")
    private String pid;

    @ApiModelProperty(value = "关联详情|BUY")
    @TableField("source")
    private String source;

    @ApiModelProperty(value = "所属商品")
    @TableField("goods")
    private String goods;

    @ApiModelProperty(value = "辅助属性")
    @TableField("attr")
    private String attr;

    @ApiModelProperty(value = "单位")
    @TableField("unit")
    private String unit;

    @ApiModelProperty(value = "仓库")
    @TableField("warehouse")
    private String warehouse;

    @ApiModelProperty(value = "批次号")
    @TableField("batch")
    private String batch;

    @ApiModelProperty(value = "生产日期")
    @TableField("mfd")
    private LocalDate mfd;

    @ApiModelProperty(value = "单价")
    @TableField("price")
    private BigDecimal price;

    @ApiModelProperty(value = "数量")
    @TableField("nums")
    private BigDecimal nums;

    @ApiModelProperty(value = "序列号")
    @TableField("serial")
    private String serial;

    @ApiModelProperty(value = "折扣率")
    @TableField("discount")
    private BigDecimal discount;

    @ApiModelProperty(value = "折扣额")
    @TableField("dsc")
    private BigDecimal dsc;

    @ApiModelProperty(value = "金额")
    @TableField("total")
    private BigDecimal total;

    @ApiModelProperty(value = "税率")
    @TableField("tax")
    private BigDecimal tax;

    @ApiModelProperty(value = "税额")
    @TableField("tat")
    private BigDecimal tat;

    @ApiModelProperty(value = "价税合计")
    @TableField("tpt")
    private BigDecimal tpt;

    @ApiModelProperty(value = "备注信息")
    @TableField("data")
    private String data;


}
