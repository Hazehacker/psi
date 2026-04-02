package com.zeroone.star.storemanagement.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SreInfoDO {
    private String Id;

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
}
