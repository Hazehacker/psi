package com.zeroone.star.reportmanagement.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 付款单详细信息数据对象
 *
 * @author toexpl
 * @since 2025/10/23
 */

@Getter
@Setter
@TableName("omy_info")
public class OmyInfoDO {

    /**
     * 唯一id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 所属id
     */
    @TableField(value = "pid")
    private String pid;

    /**
     * 结算账户
     */
    @TableField(value = "account")
    private String account;

    /**
     * 结算金额
     */
    @TableField(value = "money")
    private BigDecimal money;

    /**
     * 结算号
     */
    @TableField(value = "settle")
    private String settle;

    /**
     * 备注信息
     */
    @TableField(value = "data")
    private String data;
}
