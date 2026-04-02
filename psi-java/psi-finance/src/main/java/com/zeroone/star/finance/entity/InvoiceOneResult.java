package com.zeroone.star.finance.entity;




import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 购销发票详情
 * </p>
 *
 * @author 多令
 * @since 2025-10-26
 */
@Getter
@Setter
/**
 * 从下面的表中获得 组织，单位，单据时间，单据的标号 剩下的字段从invoice表
 *bill
 * bor
 * bre
 * buy
 * cost
 * entry
 * extry
 * ice
 * imy
 * oce
 * omy
 * sell
 * sor
 * sre
 * swap
 *
 */
public class InvoiceOneResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String Id;

    /**
     * 单据类型
     */
    private String type;

    /**
     * 所属组织
     */
    private String frame;


    /**
     * 往来的单位
     */
    private String businessUnit;

    /**
     * 单据时间
     */
    private LocalDateTime time;

    /**
     * 单据编号
     */
    private String number;

    /**
     * 单据金额
     */
    private BigDecimal money;

    /**
     * 开票时间
     */
    private LocalDateTime doTime;

    /**
     * 发票的号码
     */
    private String phoneNumber;

    /**
     * 发票抬头
     */
    private String title;

}
