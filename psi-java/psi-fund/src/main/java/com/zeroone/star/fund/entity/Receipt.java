package com.zeroone.star.fund.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.zeroone.star.project.dto.j4.fund.ReceiptSettlementDetailDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 收款单实体类
 * </p>
 *
 * @author ikun
 * @since 2025-10-24
 */
@Data
@TableName(value = "imy", autoResultMap = true)
public class Receipt {
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
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> file;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 扩展信息（数据库字段，text类型）
     */
    private String more;

    /**
     * 扩展信息（业务逻辑字段，非数据库字段）
     */
    @TableField(exist = false)
    private List<ReceiptSettlementDetailDTO> info;

    /**
     * 审审核状态[0:未审核|1:已审核]
     */
    private Integer examine;

    /**
     * 核销状态[0:未核销|1:部分核销|2:已核销]
     */
    private Integer nucleus;

    /**
     * 制单人
     */
    private String user;
}