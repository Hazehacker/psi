package com.zeroone.star.storemanagement.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BreDO {

  private String id;
  private String source;
  private String frame;
  private String supplier;
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
  private int examine;
  private int nucleus;
  private int cse;
  private int invoice;
  private int check;
  private String user;



}
