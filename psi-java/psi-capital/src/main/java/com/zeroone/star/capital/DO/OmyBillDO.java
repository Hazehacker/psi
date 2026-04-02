package com.zeroone.star.capital.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 付款单核销详情数据对象
 * 用于记录付款单的核销明细信息，包括核销类型、关联单据、核销金额等
 * 一张付款单可以对应多条核销记录，用于跟踪付款单与其他单据的关联关系
 * </p>
 *
 * @author Junjie
 * @since 2025-10-25
 */
@TableName("omy_bill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OmyBillDO {
    /**
     * 核销详情唯一标识符
     * 主键，用于唯一标识一条核销记录
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @NotNull(message = "核销详情id不能为空")
    private String id;

    /**
     * 所属付款单编号
     * 外键，关联到omy表的id，标识该核销记录属于哪张付款单
     */
    @NotNull(message = "所属单据不能为空")
    private String pid;

    /**
     * 核销类型
     * 标识核销的业务类型，如采购单核销、入库单核销等
     */
    @NotNull(message = "核销类型不能为空")
    private String type;

    /**
     * 关联单据编号
     * 关联的源单据编号，如采购订单、入库单等单据的id
     */
    @NotNull(message = "关联单据不能为空")
    private String source;

    /**
     * 核销时间
     * 记录该条核销记录的生成时间
     */
    @NotNull(message = "单据时间不能为空")
    private LocalDateTime time;

    /**
     * 核销金额
     * 本次核销的具体金额，精确到小数点后4位
     */
    @NotNull(message = "核销金额不能为空")
    private BigDecimal money;
}
