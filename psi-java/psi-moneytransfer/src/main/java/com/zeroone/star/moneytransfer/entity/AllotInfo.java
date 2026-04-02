package com.zeroone.star.moneytransfer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 转账单详情
 * </p>
 *
 * @author hh
 * @since 2025-10-23
 */
@Getter
@Setter
@TableName("allot_info")
public class AllotInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属ID
     */
    @TableField("pid")
    private String pid;

    /**
     * 转出账户
     */
    @TableField("account")
    private String account;

    /**
     * 转入账户
     */
    @TableField("tat")
    private String tat;

    /**
     * 结算金额
     */
    @TableField("money")
    private BigDecimal money;

    /**
     * 结算号
     */
    @TableField("settle")
    private String settle;

    /**
     * 备注信息
     */
    @TableField("data")
    private String data;


}
