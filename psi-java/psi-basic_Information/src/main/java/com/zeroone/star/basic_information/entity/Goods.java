package com.zeroone.star.basic_information.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品实体类
 * 
 * @author 杨潇
 * @since 2025-10-20
 */
@Data
@TableName("goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 商品名称
     */
    @TableField("name")
    private String name;

    /**
     * 拼音信息
     */
    @TableField("py")
    private String py;

    /**
     * 商品编号
     */
    @TableField("number")
    private String number;

    /**
     * 规格型号
     */
    @TableField("spec")
    private String spec;

    /**
     * 商品类别
     */
    @TableField("category")
    private String category;

    /**
     * 商品品牌
     */
    @TableField("brand")
    private String brand;

    /**
     * 商品单位[*:常规单位|-1:多单位]
     */
    @TableField("unit")
    private String unit;

    /**
     * 采购价格
     */
    @TableField("buy")
    private BigDecimal buy;

    /**
     * 销售价格
     */
    @TableField("sell")
    private BigDecimal sell;

    /**
     * 商品条码
     */
    @TableField("code")
    private String code;

    /**
     * 商品货位
     */
    @TableField("location")
    private String location;

    /**
     * 库存阈值
     */
    @TableField("stock")
    private BigDecimal stock;

    /**
     * 产品类型[0:常规商品|1:服务商品]
     */
    @TableField("type")
    private Integer type;

    /**
     * 备注信息
     */
    @TableField("data")
    private String data;

    /**
     * 商品图像
     */
    @TableField("imgs")
    private String imgs;

    /**
     * 图文详情
     */
    @TableField("details")
    private String details;

    /**
     * 多单位配置
     */
    @TableField("units")
    private String units;

    /**
     * 折扣策略
     */
    @TableField("strategy")
    private String strategy;

    /**
     * 序列产品[0:关闭|1:启用]
     */
    @TableField("serial")
    private Integer serial;

    /**
     * 批次产品[0:关闭|1:启用]
     */
    @TableField("batch")
    private Integer batch;

    /**
     * 有效期[0:关闭|1:启用]
     */
    @TableField("validity")
    private Integer validity;

    /**
     * 保质期
     */
    @TableField("protect")
    private Integer protect;

    /**
     * 预警阀值
     */
    @TableField("threshold")
    private Integer threshold;

    /**
     * 扩展信息
     */
    @TableField("more")
    private String more;
}
