package com.zeroone.star.homepage.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 采购单 (表名: buy)
 * </p>
 *
 * @author zonk
 * @since 2025-11-01
 */
@Getter
@Setter
public class Buy implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     * (对应: id varchar(32))
     */
    private String id;

    /**
     * 关联单据|BOR
     * (对应: source varchar(32))
     */
    private String source;

    /**
     * 所属组织
     * (对应: frame varchar(32))
     */
    private String frame;

    /**
     * 供应商
     * (对应: supplier varchar(32))
     */
    private String supplier;

    /**
     * 单据时间
     * (对应: time datetime)
     */
    private LocalDateTime time;

    /**
     * 单据编号
     * (对应: number varchar(32))
     */
    private String number;

    /**
     * 单据金额
     * (对应: total decimal(16, 4))
     */
    private BigDecimal total;

    /**
     * 实际金额
     * (对应: actual decimal(16, 4))
     */
    private BigDecimal actual;

    /**
     * 实付金额
     * (对应: money decimal(16, 4))
     */
    private BigDecimal money;

    /**
     * 单据费用
     * (对应: cost decimal(16, 4))
     */
    private BigDecimal cost;

    /**
     * 结算账户
     * (对应: account varchar(32))
     */
    private String account;

    /**
     * 关联人员
     * (对应: people varchar(32))
     */
    private String people;

    /**
     * 物流信息
     * (对应: logistics text)
     */
    private String logistics;

    /**
     * 单据附件
     * (对应: file text)
     */
    private String file;

    /**
     * 备注信息
     * (对应: data varchar(256))
     */
    private String data;

    /**
     * 扩展信息
     * (对应: more text)
     */
    private String more;

    /**
     * 审核状态[0:未审核|1:已审核]
     * (对应: examine tinyint(1))
     */
    private Boolean examine;

    /**
     * 核销状态[0:未核销|1:部分核销|2:已核销]
     * (对应: nucleus tinyint(1))
     */
    private Integer nucleus;

    /**
     * 费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]
     * (对应: cse tinyint(1))
     */
    private Integer cse;

    /**
     * 发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]
     * (对应: invoice tinyint(1))
     */
    private Integer invoice;

    /**
     * 核对状态[0:未核对|1:已核对]
     * (对应: `check` tinyint(1))
     * (原字段名为 check，为 Java 关键字，故重命名为 checkStatus)
     */
    private Boolean checkStatus;

    /**
     * 制单人
     * (对应: user varchar(32))
     */
    private String user;

}