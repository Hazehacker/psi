package com.zeroone.star.payment_order.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 付款单DO（与数据库omy表一一对应）
 */
@Data
@TableName("omy")
public class OmyDO {
    private String id;
    private String frame; // 所属组织
    private String supplier; // 供应商
    private LocalDateTime time; // 单据时间
    private String number; // 单据编号
    private BigDecimal total; // 单据金额
    private String people; // 关联人员
    private String file; // 单据附件
    private String data; // 备注信息
    private String more; // 扩展信息
    private Integer examine; // 审核状态[0:未审核|1:已审核]
    private Integer nucleus; // 核销状态[0:未核销|1:部分核销|2:已核销]
    private String user; // 制单人
}
