package com.zeroone.star.project.vo.j3.capital;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 扩展信息
 * </p>
 *
 * @author a
 * @since 2025-10-29
 */
@Data
public class Extension {
    /**
     * 总核销金额
     */
    private BigDecimal amount;
    /**
     * 未核销金额
     */
    private BigDecimal anwo;
    /**
     * 核对状态[0:未核对|1:已核对]
     */
    private String check;

    /**
     * 费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]
     */
    private String cse;

    /**
     * 审核状态[0:未审核|1:已审核]
     */
    private String examine;

    /**
     * 发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]
     */
    private String invoice;

    /**
     * 是否核销[0:未核销|1:已核销]
     */
    private String nucleus;
}
