package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 采购退货单
 * @TableName bre
 */
@TableName(value ="bre")
@Setter
@Getter
public class BreDO {
    /**
     * 唯一id(主键)
     */
    @TableId("id")
    private String id;

    /**
     * 关联单据|BUY
     */
    @TableField("source")
    private String source;

    /**
     * 所属组织
     */
    @TableField("frame")
    private String frame;

    /**
     * 供应商
     */
    @TableField("supplier")
    private String supplier;

    /**
     * 单据时间
     */
    @TableField("time")
    private Date time;

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
    @TableField("example")
    private Integer examine;

    /**
     * 核销状态[0:未核销|1:部分核销|2:已核销]
     */
    @TableField("nucleus")
    private Integer nucleus;

    /**
     * 费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]
     */
    @TableField("ces")
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