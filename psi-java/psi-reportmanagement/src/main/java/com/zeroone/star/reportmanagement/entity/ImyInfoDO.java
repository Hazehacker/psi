package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * 收款单详情
 * @TableName imy_info
 */
@TableName(value ="imy_info")
@Getter
@Setter
public class ImyInfoDO {
    /**
     * 唯一id（主键）
     */
    @TableId("id")
    private String id;

    /**
     * 所属ID
     */
    @TableField("pid")
    private String pid;

    /**
     * 结算账户
     */
    @TableField("pid")
    private String account;

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