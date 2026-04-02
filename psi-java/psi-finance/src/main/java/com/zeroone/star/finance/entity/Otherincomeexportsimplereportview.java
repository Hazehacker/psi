package com.zeroone.star.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author zyj
 * @since 2025-11-02
 */
@Getter
@Setter
public class Otherincomeexportsimplereportview implements Serializable {

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
     * 实际金额
     */
    private BigDecimal actual;

    /**
     * 实收金额
     */
    private BigDecimal money;

    /**
     * 结算账户
     */
    private String account;

    /**
     * 关联人员
     */
    private String people;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 审核状态[0:未审核|1:已审核]
     */
    private Boolean examine;

    /**
     * 核销状态[0:未核销|1:部分核销|2:已核销]
     */
    private Integer nucleus;

    /**
     * 制单人
     */
    private String user;

    /**
     * ice_bill表的id字段
     */
    @TableField("bill_id")
    private String billId;

    /**
     * 核销金额（bill表的money字段）
     */
    @TableField("bill_money")
    private BigDecimal billMoney;


}
