package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: a
 * @CreateTime: 2025-10-25
 * @Description: 账户详细信息实体类
 * @Version: 1.0
 */
@Getter
@Setter
@TableName("account_info")
public class AccountInfoDO {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
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
    private String clazz;

    /**
     * 单据时间
     */
    private Date time;

    /**
     * 方向[0:出|1:入]
     */
    private Integer direction;

    /**
     * 金额
     */
    private BigDecimal money;
}

