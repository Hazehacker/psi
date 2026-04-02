package com.zeroone.star.sale.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 销售退货单详情
 * </p>
 *
 * @author author
 * @since 2025-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sre_info")
public class SreInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 所属ID
     */
    private String pid;

    /**
     * 关联详情|SELL
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
    private LocalDate mfd;

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


}
