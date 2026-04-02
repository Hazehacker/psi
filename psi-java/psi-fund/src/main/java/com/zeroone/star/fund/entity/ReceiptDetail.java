package com.zeroone.star.fund.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 收款单详情实体类
 * </p>
 *
 * @author ikun
 * @since 2025-10-24
 */
@Data
@TableName("imy_info")
public class ReceiptDetail {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 所属ID
     */
    private String pid;

    /**
     * 结算账户
     */
    private String account;

    /**
     * 结算金额
     */
    private BigDecimal money;

    /**
     * 结算号
     */
    private String settle;

    /**
     * 备注信息
     */
    private String data;
}
