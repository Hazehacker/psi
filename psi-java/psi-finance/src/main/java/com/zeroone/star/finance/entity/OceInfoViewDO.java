package com.zeroone.star.finance.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OceInfoViewDO {
    private String id;

    private String ietName;

    private BigDecimal money;

    private String data;
}
