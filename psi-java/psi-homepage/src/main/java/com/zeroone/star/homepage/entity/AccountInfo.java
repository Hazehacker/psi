package com.zeroone.star.homepage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 资金详情
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Getter
@Setter
@TableName("account_info")
public class AccountInfo implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private LocalDateTime time;

    /**
     * 方向[0:出|1:入]
     */
    private Boolean direction;

    /**
     * 金额
     */
    private BigDecimal money;


}
