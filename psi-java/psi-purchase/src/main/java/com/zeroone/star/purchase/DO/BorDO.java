package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * <p>
 * 采购订单
 * </p>
 *
 * @author 小可
 * @since 2025-10-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("bor")
public class BorDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 关联单据|Sor
     */
    private String source;

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
     * 到货时间
     */
    private LocalDateTime arrival;

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
     * 审核状态[0:未审核|1:已审核]
     */
    private Integer examine;

    /**
     * 入库状态[0:未入库|1:部分入库|2:已入库|3:关闭]
     */
    private Integer state;

    /**
     * 制单人
     */
    private String user;

}
