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
public class Otherincomeexportdetailreportview implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ice表的id字段
     */
    @TableField("ice_id")
    private String iceId;

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
     * 备注信息
     */
    private String data;

    /**
     * 单据金额
     */
    private BigDecimal total;

    /**
     * 结算账户
     */
    private String account;

    /**
     * 关联人员
     */
    private String people;

    /**
     * ice_info表的id字段
     */
    @TableField("ice_info_id")
    private String iceInfoId;

    /**
     * 结算金额
     */
    private BigDecimal money;

    /**
     * ice_bill表的id字段
     */
    @TableField("ice_bill_id")
    private String iceBillId;

    /**
     * 核销金额
     */
    @TableField("bill_money")
    private BigDecimal billMoney;

    private String iet;
}
