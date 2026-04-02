package com.zeroone.star.sale.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 资金详情
 * </p>
 *
 * @author author
 * @since 2025-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("account_info")
public class AccountInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属账户
     */
    private String pid;

    /**
     * 单据类型
     */
    private String type;

    /**
     * 所属类
     */
    @TableField("`class`")  // 使用反引号转义
    private String className;

    /**
     * 单据时间
     */
    private LocalDateTime time;

    /**
     * 方向[0:出|1:入]
     */
    private Integer direction;

    /**
     * 金额
     */
    private BigDecimal money;


}
