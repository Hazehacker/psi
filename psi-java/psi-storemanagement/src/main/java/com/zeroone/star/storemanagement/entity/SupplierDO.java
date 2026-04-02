package com.zeroone.star.storemanagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author vigorous
 * @date 2025/10/23
 * @description 供应商
 */
@Data
@TableName(value = "supplier")
public class SupplierDO {

    private String id;

    private String name;

    private String py;

    private String number;

    private String frame;

    private BigDecimal rate;

    private String user;

    private String data;

    private String more;

    private BigDecimal balance;

    private String category;

    private String bank;

    private String tax;

    private String contacts;

    private String account;
}
