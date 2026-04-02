package com.zeroone.star.reportmanagement.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@TableName("imy")
public class ImyDO {
    /**
     *唯一id(主键)
     */
    @TableId("id")
    private String id;

    /**
     * 所属组织
     */
    @TableField("frame")
    private String frame;

    /**
     * 客户
     */
    @TableField("customer")
    private String customer;

    /**
     * 单据时间
     */
    @TableField("customer")
    private Date time;

    /**
     * 单据编号
     */
    @TableField("time")
    private String number;

    /**
     * 单据金额
     */
    @TableField("total")
    private BigDecimal total;

    /**
     * 关联人员
     */
    @TableField("people")
    private String people;

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
     * 核销状态[0:未核销|1:部分核销|2:已核销]
     */
    @TableField("nucleus")
    private Integer nucleus;

    /**
     * 制单人
     */
    @TableField("user")
    private String user;
}
