package com.zeroone.star.sale.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 销售单详情
 * </p>
 *
 * @author renjian
 * @since 2025-10-29
 */
@Getter
@Setter
@TableName("sell_info")
public class SellInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 所属ID
     */
    private String pid;

    /**
     * 关联详情|SOR
     */
    private String source;

    /**
     * 所属商品
     */
    private String goods;

    /**
     * 辅助属性
     */
    private String attr;

    /**
     * 单位
     */
    private String unit;

    /**
     * 仓库
     */
    private String warehouse;

    /**
     * 批次号
     */
    private String batch;

    /**
     * 生产日期
     */
    private Date mfd;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private BigDecimal nums;

    /**
     * 序列号
     */
    private String serial;

    /**
     * 折扣率
     */
    private BigDecimal discount;

    /**
     * 折扣额
     */
    private BigDecimal dsc;

    /**
     * 金额
     */
    private BigDecimal total;

    /**
     * 税率
     */
    private BigDecimal tax;

    /**
     * 税额
     */
    private BigDecimal tat;

    /**
     * 价税合计
     */
    private BigDecimal tpt;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 退货数量
     */
    private BigDecimal retreat;


}
