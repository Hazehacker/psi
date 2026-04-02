package com.zeroone.star.homepage.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 收款单
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Getter
@Setter
public class Imy implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 所属组织
     */
    private String frame;

    /**
     * 客户
     */
    private String customer;

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
    private BigDecimal total;

    /**
     * 关联人员
     */
    private String people;

    /**
     * 单据附件
     */
    private String file;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 扩展信息
     */
    private String more;

    /**
     * 审核状态[0:未审核|1:已审核]
     */
    private Boolean examine;

    /**
     * 核销状态[0:未核销|1:部分核销|2:已核销]
     */
    private Boolean nucleus;

    /**
     * 制单人
     */
    private String user;


}
