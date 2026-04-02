package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hpsf.Decimal;

import java.time.LocalDateTime;

/**
 * 其他出库单详情数据对象
 *
 * @author 言语
 * @since 2025/10/23
 */

@Getter
@Setter
@TableName("extry_info")
public class ExtryInfoDO {

    /**
     * 主键ID
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
     * 批次号
     */
    @TableField("batch")
    private String batch;

    /**
     * 生产日期
     */
    @TableField("mfd")
    private LocalDateTime mfd;

    /**
     * 成本
     */
    @TableField("price")
    private Decimal price;

    /**
     * 数量
     */
    @TableField("nums")
    private Decimal nums;

    /**
     * 序列号
     */
    @TableField("serial")
    private String serial;

    /**
     * 总成本
     */
    @TableField("total")
    private String total;

    /**
     * 备注信息
     */
    @TableField("data")
    private String data;
}