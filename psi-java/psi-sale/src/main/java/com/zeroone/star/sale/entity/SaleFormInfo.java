package com.zeroone.star.sale.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
public class SaleFormInfo {
    private String id;
    private String pid;
    private String source;
    private String goods;
    private String attr;
    private String unit;
    private String warehouse;
    private String batch;
    private LocalDate mfd;
    private BigDecimal price;
    private BigDecimal nums;
    private String serial;
    private BigDecimal discount;
    private BigDecimal dsc;
    private BigDecimal total;
    private BigDecimal tax;
    private BigDecimal tat;
    private BigDecimal tpt;
    private String data;
    private BigDecimal retreat;
}