package com.zeroone.star.payment_order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 付款单核销详情DO（与数据库omy_bill表一一对应）
 */
@Data
@TableName("omy_bill")
public class OmyBillDO {
    private String id;
    private String pid; // 关联付款单主表id
    private String type; // 核销类型
    private String source; // 关联单据
    private LocalDateTime time; // 单据时间
    private BigDecimal money; // 核销金额
}