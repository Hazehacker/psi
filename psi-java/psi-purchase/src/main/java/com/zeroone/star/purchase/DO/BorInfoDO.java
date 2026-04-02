package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.*;

/**
 * <p>
 * 采购订单详情
 * </p>
 *
 * @author 小可
 * @since 2025-10-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("bor_info")
public class BorInfoDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 所属ID
     */
    private String pid;

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
     * 单价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private BigDecimal nums;

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
     * 入库数量
     */
    private BigDecimal handle;

}
