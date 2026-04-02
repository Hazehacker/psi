package com.zeroone.star.finance.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillSnapshot {
    private String type;           // buy/bre/sell/sre
    private String id;             // 单据ID
    private BigDecimal actual;     // 单据金额(用于上限判断)
    private Integer invStatus;     // 主表发票状态 0/1/2/3
    private BigDecimal openedAmount; // 已开金额(来自 invoice 汇总)
}