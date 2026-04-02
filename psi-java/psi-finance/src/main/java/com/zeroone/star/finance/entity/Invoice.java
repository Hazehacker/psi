package com.zeroone.star.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 发票详情
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
@Getter
@Setter
@Data
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 单据类型
     */
    private String type;

    /**
     * 所属单据
     */
    @TableField("class")
    private String clazz;

    /**
     * 开票时间
     */
    private LocalDateTime time;

    /**
     * 发票号码
     */
    private String number;

    /**
     * 发票抬头
     */
    private String title;

    /**
     * 开票金额
     */
    private BigDecimal money;

    /**
     * 发票附件
     */
    private String file;

    /**
     * 备注信息
     */
    private String data;


}
