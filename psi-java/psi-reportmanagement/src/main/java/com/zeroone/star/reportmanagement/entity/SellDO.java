package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售单表
 *
 * @author 乐鱼
 * @date 2025-10-22
 */

@Getter
@Setter
@TableName("sell")
public class SellDO {

    /**
     * 唯一id
     */
    @TableId("id")
    private String id;

    /**
     * 关联单据|SOR
     */
    @TableField("source")
    private String source;

    /**
     * 所属组织
     */
    @TableField("frame")
    private String frame;

    /**
     * 客户
     */
    @TableField("customer")
    private String customer;

    /**
     * 单据时间
     */
    @TableField("time")
    private LocalDateTime time;

    /**
     * 单据编号
     */
    @TableField("number")
    private String number;

    /**
     * 单据金额
     */
    @TableField("total")
    private BigDecimal total;

    /**
     * 实际金额
     */
    @TableField("actual")
    private BigDecimal actual;

    /**
     * 实收金额
     */
    @TableField("money")
    private BigDecimal money;

    /**
     * 单据费用
     */
    @TableField("cost")
    private BigDecimal cost;

    /**
     * 结算账户
     */
    @TableField("account")
    private String account;

    /**
     * 关联人员
     */
    @TableField("people")
    private String people;

    /**
     * 物流信息
     */
    @TableField("logistics")
    private String logistics;

    /**
     * 单据附件
     */
    @TableField("file")
    private String file;

    /**
     * 备注信息
     */
    @TableField("data")
    private String data;

    /**
     * 扩展信息
     */
    @TableField("more")
    private String more;

    /**
     * 审核状态[0:未审核|1:已审核]
     */
    @TableField("examine")
    private Integer examine;

    /**
     * 核销状态[0:未核销|1:部分核销|2:已核销]
     */
    @TableField("nucleus")
    private Integer nucleus;

    /**
     * 费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]
     */
    @TableField("cse")
    private Integer cse;

    /**
     * 发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]
     */
    @TableField("invoice")
    private Integer invoice;

    /**
     * 核对状态[0:未核对|1:已核对]
     */
    @TableField("check")
    private Integer check;

    /**
     * 制单人
     */
    @TableField("user")
    private String user;


}
