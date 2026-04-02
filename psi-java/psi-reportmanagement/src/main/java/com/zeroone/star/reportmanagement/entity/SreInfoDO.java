package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 销售退货单详情表
 */
@Getter
@Setter
@TableName("sre_info")
public class SreInfoDO {

    /**
     * 唯一ID
     */
    @TableId(value = "id")
    private String id;

    /**
     * 所属ID（关联 sre.id）
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
     * 所属仓库
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