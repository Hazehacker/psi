package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 收发统计表
 * @author yu
 * @date 2025/10/23
 */
@Getter
@Setter
@TableName("summary")
public class SummaryDO {

    /**
     * 主键ID
     */
    @TableId("id")
    private String id;

    /**
     * 仓储详情ID
     */
    @TableField("pid")
    private String pid;

    /**
     * 单据类型
     */
    @TableField("type")
    private String type;

    /**
     * 所属单据
     */
    @TableField("class")
    private String clazz;

    /**
     * 所属详情
     */
    @TableField("info")
    private String info;

    /**
     * 单据时间
     */
    @TableField("time")
    private LocalDateTime time;

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
     * 所属仓库
     */
    @TableField("warehouse")
    private String warehouse;

    /**
     * 批次
     */
    @TableField("batch")
    private String batch;

    /**
     * 生产日期
     */
    @TableField("mfd")
    private LocalDateTime mfd;

    /**
     * 序列号
     */
    @TableField("serial")
    private String serial;

    /**
     * 方向[0:出|1:入]
     */
    @TableField("direction")
    private Integer direction;

    /**
     * 基础单价
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 基础数量
     */
    @TableField("nums")
    private BigDecimal nums;

    /**
     * 单位成本
     */
    @TableField("uct")
    private BigDecimal uct;

    /**
     * 基础成本
     */
    @TableField("bct")
    private BigDecimal bct;

    /**
     * 结存组
     */
    @TableField("exist")
    private String exist;

    /**
     * 结余组
     */
    @TableField("balance")
    private String balance;

    /**
     * 先进先出数量
     */
    @TableField("handle")
    private BigDecimal handle;
}
