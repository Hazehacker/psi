package com.zeroone.star.storemanagement.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.storemanagement.entity
 * @Author: nixiangtaiduole!
 * @CreateTime: 2025-10-23  16:52
 * @Description: 采购退货单详情
 * @Version: 1.0
 */
@Data
public class BreInfoDO {
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

}
