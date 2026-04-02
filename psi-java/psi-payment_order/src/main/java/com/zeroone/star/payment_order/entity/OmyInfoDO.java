package com.zeroone.star.payment_order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 付款单详情DO（与数据库omy_info表一一对应）
 */
@Data
@TableName("omy_info")
public class OmyInfoDO {
    private String id;
    private String pid; // 所属ID（关联omy表的id）
    private String account; // 结算账户
    private BigDecimal money; // 结算金额
    private String settle; // 结算号
    private String data; // 备注信息
}
