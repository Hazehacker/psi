package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 销售订单DO
 * @author clz
 * @date 2025/10/22
 */
@Getter
@Setter
@TableName("sor")
public class SorDO {

    /**
     * 唯一id（主键）
     */
    @TableId(value = "id", type = IdType.NONE)
    private String id;

    /**
     * 所属组织
     */
    private String frame;

    /**
     * 客户
     */
    private String customer;

    /**
     * 单据时间
     */
    private LocalDateTime time;

    /**
     * 单据编号
     */
    private String number;

    /**
     * 单据金额
     */
    private BigDecimal total;

    /**
     * 实际金额
     */
    private BigDecimal actual;

    /**
     * 关联人员
     */
    private String people;

    /**
     * 到货日期
     */
    private LocalDate arrival;

    /**
     * 物流信息
     */
    private String logistics;

    /**
     * 单据附件
     */
    private String file;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 扩展信息
     */
    private String more;

    /**
     * 审核状态
     * 枚举值说明：
     * 0: 未审核
     * 1: 已审核
     */
    private Integer examine;

    /**
     * 出库状态
     * 枚举值说明：
     * 0: 未出库
     * 1: 部分出库
     * 2: 已出库
     * 3: 关闭
     */
    private Integer state;

    /**
     * 制单人
     */
    @TableField(value = "user")
    private String user;
}
