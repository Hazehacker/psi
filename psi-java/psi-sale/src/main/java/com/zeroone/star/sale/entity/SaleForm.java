package com.zeroone.star.sale.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class SaleForm {
    private String id;
    private String source;
    private String frame;
    private String customer;
    private LocalDateTime time;
    private String number;
    private BigDecimal total;
    private BigDecimal actual;
    private BigDecimal money;
    private BigDecimal cost;
    private String account;
    private String people;
    private String logistics;
    private String file;
    private String data;
    private String more;
    private Integer examine;
    private Integer nucleus;
    private Integer cse;
    private Integer invoice;
    private Integer check;
    private String user;
}