package com.zeroone.star.capital.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * IceDO 实体类，对应数据库表字段
 */
@Getter
@Setter
@TableName("ice")
public class IceDO {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 所属组织
     */
    private String frame;

    /**
     * 客户
     */
    private String customer;

    /**
     * 单据时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime time;

    /**
     * 单据编号
     */
    private String number;

    /**
     * 单据金额
     */
    private java.math.BigDecimal total;

    /**
     * 实际金额
     */
    private java.math.BigDecimal actual;

    /**
     * 实收金额
     */
    private java.math.BigDecimal money;

    /**
     * 结算账户
     */
    private String account;

    /**
     * 关联人员
     */
    private String people;

    /**
     * 单据附件
     */
    private String file;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 扩展信息
     */
    private String more;

    /**
     * 审核状态 [0:未审核|1:已审核]
     */
    private Integer examine;

    /**
     * 核销状态 [0:未核销|1:部分核销|2:已核销]
     */
    private Integer nucleus;

    /**
     * 制单人
     */
    private String user;
}
