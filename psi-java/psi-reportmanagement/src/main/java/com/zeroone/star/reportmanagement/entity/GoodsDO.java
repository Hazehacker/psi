package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 商品DO
 * @author clz
 * @date 2025/10/22
 */
@Getter
@Setter
@TableName("goods")
public class GoodsDO {

    /**
     * 唯一id（主键）
     */
    @TableId(value = "id", type = IdType.NONE)
    private String id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 拼音信息
     */
    private String py;

    /**
     * 商品编号
     */
    private String number;

    /**
     * 规格型号
     */
    private String spec;

    /**
     * 商品类别
     */
    private String category;

    /**
     * 商品品牌
     */
    private String brand;

    /**
     * 商品单位
     * 取值说明：
     *  *: 常规单位
     * -1: 多单位
     */
    private String unit;

    /**
     * 采购价格
     */
    private BigDecimal buy;

    /**
     * 销售价格
     */
    private BigDecimal sell;

    /**
     * 商品条码
     */
    private String code;

    /**
     * 商品货位
     */
    private String location;

    /**
     * 库存阈值
     */
    private BigDecimal stock;

    /**
     * 产品类型
     * 枚举值说明：
     * 0: 常规商品
     * 1: 服务商品
     */
    private Integer type;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 商品图像
     */
    private String imgs;

    /**
     * 图文详情
     */
    private String details;

    /**
     * 多单位配置
     */
    private String units;

    /**
     * 折扣策略
     */
    private String strategy;

    /**
     * 序列产品启用状态
     * 枚举值说明：
     * 0: 关闭
     * 1: 启用
     */
    private Integer serial;

    /**
     * 批次产品启用状态
     * 枚举值说明：
     * 0: 关闭
     * 1: 启用
     */
    private Integer batch;

    /**
     * 有效期管理状态
     * 枚举值说明：
     * 0: 关闭
     * 1: 启用
     */
    private Integer validity;

    /**
     * 保质期
     */
    private Integer protect;

    /**
     * 预警阀值
     */
    private Integer threshold;

    /**
     * 扩展信息
     */
    private String more;
}
