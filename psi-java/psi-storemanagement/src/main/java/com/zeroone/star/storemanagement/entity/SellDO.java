package com.zeroone.star.storemanagement.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 功能：
 * <p>
 * ——————————————————————————————
 * version   变更日期    修改人
 * ------------------------------
 * v1.0.0    2025/10/24    shark
 * ——————————————————————————————
 * 修改说明：
 *
 * @author: shark
 */
@Data
public class SellDO {
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

    private Integer invoke;

    private Integer check;

    private String user;
}
