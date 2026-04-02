package com.zeroone.star.sale.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sell_info")
public class SaleInfoDO {
    private String id; // 主键ID
    private String pid; // 所属销售单ID（外键）
    private String source; // 关联详情 | SOR
    private String goods; // 所属商品
    private String attr; // 辅助属性
    private String unit; // 单位
    private String warehouse; // 仓库
    private String batch; // 批次号
    private Date mfd; // 生产日期
    private BigDecimal price; // 单价
    private BigDecimal nums; // 数量
    private String serial; // 序列号
    private BigDecimal discount; // 折扣率
    private BigDecimal dsc; // 折扣额
    private BigDecimal total; // 金额
    private BigDecimal tax; // 税率
    private BigDecimal tat; // 税额
    private BigDecimal tpt; // 价税合计
    private String data; // 备注信息
    private BigDecimal retreat; // 退货数量
}
