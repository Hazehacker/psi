package com.zeroone.star.fund.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 收款单详情
 * </p>
 *
 * @author icefast
 * @since 2025-10-24
 */
@Getter
@Setter
@TableName("imy_info")
public class ImyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 所属ID
     */
    private String pid;

    /**
     * 结算账户
     */
    private String account;

    /**
     * 结算金额
     */
    private BigDecimal money;

    /**
     * 结算号
     */
    private String settle;

    /**
     * 备注信息
     */
    private String data;


}
