package com.zeroone.star.moneytransfer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 资金详情
 * </p>
 *
 * @author hh
 * @since 2025-10-29
 */
@Getter
@Setter
@TableName("account_info")
public class AccountInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属账户
     */
    @TableField("pid")
    private String pid;

    /**
     * 单据类型
     */
    @TableField("type")
    private String type;

    /**
     * 所属类
     */
    //为了避免和Java关键字冲突，重命名一下
    @TableField("`class`")
    private String className;

    /**
     * 单据时间
     */
    @TableField("time")
    private LocalDate time;

    /**
     * 方向[0:出|1:入]
     */
    @TableField("direction")
    private Integer direction;

    /**
     * 金额
     */
    @TableField("money")
    private BigDecimal money;


}
