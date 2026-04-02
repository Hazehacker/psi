package com.zeroone.star.reportmanagement.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 辅助属性[商品]
 * </p>
 *
 * @author imp
 * @since 2025-10-23
 */
@Getter
@Setter
@TableName("attr")
public class Attr implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id")
    private String id;

    /**
     * 所属商品
     */
    private String pid;

    /**
     * 属性名称
     */
    private String name;

    /**
     * 采购价格
     */
    private BigDecimal buy;

    /**
     * 销售价格
     */
    private BigDecimal sell;

    /**
     * 条形码
     */
    private String code;


}
