package com.zeroone.star.finance.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OceDetailViewDO {

    private String id;

    private String frame;

    private String supplier;

    private LocalDateTime time;

    private String number;

    private BigDecimal total;

    private BigDecimal actual;

    private BigDecimal money;

    //oce_bill报销金额之和
    private BigDecimal writeOffAmount;

    private String account;

    private String people;

    private String file;

    private String data;

    private int examine;

}
