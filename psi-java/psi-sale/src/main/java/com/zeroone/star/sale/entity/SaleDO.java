package com.zeroone.star.sale.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sell")
public class SaleDO {
    private String id; // 主键ID
    private String source; // 关联单据 | SOR
    private String frame; // 所属组织
    private String customer; // 客户
    private Date time; // 单据时间
    private String number; // 单据编号
    private BigDecimal total; // 单据金额
    private BigDecimal actual; // 实际金额
    private BigDecimal money; // 实收金额
    private BigDecimal cost; // 单据费用
    private String account; // 结算账户
    private String people; // 关联人员
    private String logistics; // 物流信息
    private String file; // 单据附件
    private String data; // 备注信息
    private String more; // 扩展信息
    @TableField("examine")
    private Boolean examine = false; // 审核状态 [0:未审核|1:已审核]
    @TableField("nucleus")
    private Integer nucleus = 0; // 核销状态 [0:未核销|1:部分核销|2:已核销]
    @TableField("cse")
    private Integer cse = 0; // 费用状态 [0:未结算|1:部分结算|2:已结算|3:无需结算]
    @TableField("invoice")
    private Integer invoice = 0; // 发票状态 [0:未开票|1:部分开票|2:已开票|3:无需开具]
    @TableField("`check`")
    private Boolean check =  false; // 核对状态 [0:未核对|1:已核对]
    private String user; // 制单人
}
