package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 单据费用表
 */
@Getter
@Setter
@TableName("cost")
public class CostDO {

    /**
     * 唯一ID
     */
    @TableId(value = "id")
    private String id;

    /**
     * 单据类型（如 sell, sre 等）
     */
    private String type;

    /**
     * 所属类（关联单据ID，如 sell.id 或 sre.id）
     */
    @TableField(value = "class")
    private String clazz;

    /**
     * 单据时间
     */
    private LocalDateTime time;

    /**
     * 所属收支项目（如快递费、包装费）
     */
    private String iet;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 备注
     */
    private String data;

    /**
     * 结算金额
     */
    private BigDecimal settle;

    /**
     * 结算状态 [0:未结算|1:部分结算|2:已结算]
     */
    private Integer state;
}