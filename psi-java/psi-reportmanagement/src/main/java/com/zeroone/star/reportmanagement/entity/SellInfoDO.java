package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 销售单详情表
 *
 * @author 乐鱼
 * @date 2025-10-22
 */


@Getter
@Setter
@TableName("sell_info")
public class SellInfoDO {
    /**
     * 唯一ID
     */
    @TableId("id")
    private String id;

    /**
     * 所属ID
     */
    @TableField("pid")
    private String pid;

    /**
     * 关联详情|SOR
     */
    @TableField("source")
    private String source;

    /**
     * 所属商品
     */
    @TableField("goods")
    private String goods;

    /**
     * 辅助属性
     */
    @TableField("attr")
    private String attr;

    /**
     * 单位
     */
    @TableField("unit")
    private String unit;

    /**
     * 仓库
     */
    @TableField("warehouse")
    private String warehouse;

    /**
     * 批次号
     */
    @TableField("batch")
    private String batch;

    /**
     * 生产日期
     */
    @TableField("mfd")
    private LocalDate mfd;

    /**
     * 单价
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 数量
     */
    @TableField("nums")
    private BigDecimal nums;

    /**
     * 序列号
     */
    @TableField("serial")
    private String serial;

    /**
     * 折扣率
     */
    @TableField("discount")
    private BigDecimal discount;

    /**
     * 折扣额
     */
    @TableField("dsc")
    private BigDecimal dsc;

    /**
     * 金额
     */
    @TableField("total")
    private BigDecimal total;

    /**
     * 税率
     */
    @TableField("tax")
    private BigDecimal tax;

    /**
     * 税额
     */
    @TableField("tat")
    private BigDecimal tat;

    /**
     * 价税合计
     */
    @TableField("tpt")
    private BigDecimal tpt;

    /**
     * 备注信息
     */
    @TableField("data")
    private String data;

    /**
     * 退货数量
     */
    @TableField("retreat")
    private BigDecimal retreat;
}
