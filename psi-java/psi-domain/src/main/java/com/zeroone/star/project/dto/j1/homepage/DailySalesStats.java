package com.zeroone.star.project.dto.j1.homepage;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DailySalesStats {
    private Date salesDate;          // 销售日期
    private Integer dailySalesQuantity; // 日销售量
    private BigDecimal dailySalesAmount; // 日销售额
}