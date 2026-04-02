package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 销售订单详情DO
 * @author clz
 * @date 2025/10/22
 */
@Getter
@Setter
@TableName("sor_info")
public class SorInfoDO {

    /**
     * 唯一id（主键）
     */
    @TableId(value = "id", type = IdType.NONE)
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
     * 出库数量
     */
    private BigDecimal handle;
}