package com.zeroone.star.capital.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 收款单
 * </p>
 *
 * @author a
 * @since 2025-10-29
 */
@Getter
@Setter
@TableName("oce")
public class OceDO {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 所属组织
     */
    private String frame;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 单据时间
     */
    private LocalDateTime time;

    /**
     * 单据编号
     */
    private String number;

    /**
     * 单据金额
     */
    private BigDecimal total;

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
