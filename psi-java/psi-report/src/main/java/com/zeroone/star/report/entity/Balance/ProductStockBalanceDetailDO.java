package com.zeroone.star.report.entity.Balance;

import lombok.Data;
import java.math.BigDecimal;

/** 仓库层（“多”） */
@Data
public class ProductStockBalanceDetailDO {
    private String goodsId;
    private String warehouseId;
    private String warehouseName;
    private BigDecimal quantity;
    private BigDecimal amount;
    private BigDecimal cost;
}