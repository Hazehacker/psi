package com.zeroone.star.reportmanagement.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 付款单数据对象
 *
 * @author toexpl
 * @since 2025/10/23
 */

@Getter
@Setter
@TableName("omy")
public class OmyDO {

    /**
     * 唯一id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 所属组织
     */
    @TableField(value = "frame")
    private String frame;

    /**
     * 供应商
     */
    @TableField(value ="supplier")
    private String supplier;

    /**
     * 单据时间
     */
    @TableField(value = "time")
    private LocalDateTime time;

    /**
     * 单据编号
     */
    @TableField(value = "number")
    private String number;

    /**
     * 单据金额
     */
    @TableField(value = "total")
    private BigDecimal total;

    /**
     * 关联人员
     */
    @TableField(value = "people")
    private String people;

    /**
     * 单据附件
     */
    @TableField(value = "file")
    private String file;

    /**
     * 备注信息
     */
    @TableField(value = "data")
    private String data;

    /**
     * 扩展信息
     */
    @TableField(value = "more")
    private String more;

    /**
     * 审核状态【0：未审核】【1:已审核】
     */
    @TableField(value = "examine")
    private boolean examine;

    /**
     * 核销状态【0：未核销】【1：部分核销】【2：已核销】
     */
    @TableField(value = "nucleus")
    private Integer nucleus;

    /**
     * 制单人
     */
    @TableField(value = "user")
    private String user;

}
