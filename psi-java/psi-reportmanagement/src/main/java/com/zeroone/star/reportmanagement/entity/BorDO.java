package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购订单
 * @author yu
 * @date 2025/10/23
 */
@Getter
@Setter
@TableName("bor")
public class BorDO {

    /**
     * 主键ID
     */
    @TableId("id")
    private String id;

    /**
     * 关联单据|Sor
     */
    @TableField("source")
    private String source;

    /**
     * 所属组织
     */
    @TableField("frame")
    private String frame;

    /**
     * 供应商
     */
    @TableField("supplier")
    private String supplier;

    /**
     * 单据时间
     */
    @TableField("time")
    private LocalDateTime time;

    /**
     * 单据编号
     */
    @TableField("number")
    private String number;

    /**
     * 单据金额
     */
    @TableField("total")
    private BigDecimal total;

    /**
     * 实际金额
     */
    @TableField("actual")
    private BigDecimal actual;

    /**
     * 关联人员
     */
    @TableField("people")
    private String people;

    /**
     * 到货时间
     */
    @TableField("arrival")
    private LocalDateTime arrival;

    /**
     * 物流信息
     */
    @TableField("logistics")
    private String logistics;

    /**
     * 单据附件
     */
    @TableField("file")
    private String file;

    /**
     * 备注信息
     */
    @TableField("data")
    private String data;

    /**
     * 扩展信息
     */
    @TableField("more")
    private String more;

    /**
     * 审核状态[0:未审核|1:已审核]
     */
    @TableField("examine")
    private Integer examine;

    /**
     * 入库状态[0:未入库|1:部分入库|2:已入库|3:关闭]
     */
    @TableField("state")
    private Integer state;

    /**
     * 制单人
     */
    @TableField("user")
    private String user;
}
