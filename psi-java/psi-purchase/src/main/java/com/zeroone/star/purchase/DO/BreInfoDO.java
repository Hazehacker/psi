package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 采购退货单详情
 * </p>
 *
 * @author xiaoliu
 * @since 2025-10-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("bre_info")
public class BreInfoDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 所属ID
     */
    private String pid;

    /**
     * 关联详情|BUY
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
    private String mfd;

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
    private BigDecimal tpt = BigDecimal.ZERO;

    /**
     * 备注信息
     */
    private String data;

}
