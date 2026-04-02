package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;


/**
 * 采购订单详情表
 * @author yu
 * @date 2025/10/22
 */
@Getter
@Setter
@TableName("bor_info")
public class BorInfoDO {

    /**
     * 唯一id
     */
    @TableId("id")
    private String id;

    /**
     * 所属ID
     */
    @TableField("pid")
    private String pid;

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
     * 入库数量
     */
    @TableField("handle")
    private BigDecimal handle;
}
