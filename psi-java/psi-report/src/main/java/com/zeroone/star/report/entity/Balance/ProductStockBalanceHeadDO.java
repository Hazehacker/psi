package com.zeroone.star.report.entity.Balance;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品层（“一”）
 */
@Data
public class ProductStockBalanceHeadDO {
    private String goodsId;
    private String productName;
    private String productCode;
    private String specification;
    private String unit;

    // 汇总（跨仓库）
    private BigDecimal totalQuantity;   // 总数量
    private BigDecimal totalAmount;     // 总金额(成本合计)
    private BigDecimal avgCost;         // 平均成本 = 总金额 / 总数量
}