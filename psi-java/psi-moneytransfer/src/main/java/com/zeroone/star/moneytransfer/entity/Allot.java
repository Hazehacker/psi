package com.zeroone.star.moneytransfer.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 转账单
 * </p>
 *
 * @author hh
 * @since 2025-10-23
 */
@Getter
@Setter
@TableName("allot")
public class Allot implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属组织
     */
    @TableField("frame")
    private String frame;

    /**
     * 单据时间
     */
    @TableField(value = "time", fill = FieldFill.INSERT)
    private LocalDate time;

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
     * 关联人员
     */
    @TableField("people")
    private String people;

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
     * 制单人
     */
    @TableField("user")
    private String user;


}
