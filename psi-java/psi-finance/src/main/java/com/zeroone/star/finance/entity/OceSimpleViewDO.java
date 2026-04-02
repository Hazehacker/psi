package com.zeroone.star.finance.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OceSimpleViewDO {
    private String id;

    private String frame;

    private String supplier;

    private LocalDateTime time;

    private String number;

    private BigDecimal total;

    private BigDecimal actual;

    private BigDecimal money;

    //oce_bill
    private BigDecimal writeOffAmount;

    private String account;

    private String people;

    private Integer examine;

    private Integer nucleus;

    private String user;

    private String data;
}
