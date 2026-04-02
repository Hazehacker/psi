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
public class SellInfoDO {
    private String id;

    private String pid;

    private String source;

    private String goods;

    private String attr;

    private String unit;

    private String warehouse;

    private String batch;

    private LocalDateTime mfd;

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
