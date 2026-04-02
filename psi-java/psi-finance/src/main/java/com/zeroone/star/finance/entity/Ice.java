package com.zeroone.star.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 其它收入单
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
@Getter
@Setter
public class Ice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
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
     * 实收金额
     */
    private BigDecimal money;

    /**
     * 核销金额
     */
    private BigDecimal writeOffAmount;

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

    /**
     * 核销单列表 (一对多)
     */
    private List<IceBill> iceBills;

    /**
     * 明细信息列表 (一对多)
     */
    private List<IceInfo> iceInfos;

}
