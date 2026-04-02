package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *采购单详情表
 * @author chuming_7
 * @date 2025-10-23
 */
@Getter
@Setter
@TableName("buy_info")
public class BuyInfoDO {
    /**
     * 唯一id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 所属ID
     */
    @TableField(value = "pid")
    private String pid;

    /**
     * 关联详情
     */
    @TableField(value = "source")
    private String source;

    /**
     * 所属商品
     */
    @TableField(value = "goods")
    private String goods;

    /**
     * 辅助属性
     */
    @TableField(value = "attr")
    private String attr;

    /**
     * 单位
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 仓库
     */
    @TableField(value = "warehouse")
    private String warehouse;

    /**
     * 批次号
     */
    @TableField(value = "batch")
    private String batch;

    /**
     * 生产日期
     */
    @TableField(value = "mfd")
    private LocalDateTime mfd;

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
