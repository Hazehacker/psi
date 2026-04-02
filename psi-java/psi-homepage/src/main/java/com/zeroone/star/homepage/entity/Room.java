package com.zeroone.star.homepage.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 仓储信息
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Getter
@Setter
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 所属仓库
     */
    private String warehouse;

    /**
     * 所属商品
     */
    private String goods;

    /**
     * 辅助属性
     */
    private String attr;

    /**
     * 库存数量
     */
    private BigDecimal nums;


}
