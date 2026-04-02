package com.zeroone.star.reportmanagement.entity;

import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 其他支出单
 * @author 天天困
 * @date 2025/10/23
 */
@Getter
@Setter
@TableName("oce")
public class OceDO {

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
    * 供应商
    */
    private String supplier;

    /**
    * 单据时间
    */
    private Date time;

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
    * 实付金额
    */
    private BigDecimal money;

    /**
    * 结算账户
    */
    private String account;

    /**
    * 关联人员
    */
    private String people;

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
    * 审核状态[0:未审核|1:已审核]
    */
    private Integer examine;

    /**
    * 核销状态[0:未核销|1:部分核销|2:已核销]
    */
    private Integer nucleus;

    /**
    * 制单人
    */
    private String user;

}
