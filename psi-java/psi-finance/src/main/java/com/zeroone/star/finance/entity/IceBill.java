package com.zeroone.star.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 其它收入单核销详情
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
@Getter
@Setter
@TableName("ice_bill")
public class IceBill implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 所属单据
     */
    private String pid;

    /**
     * 核销类型
     */
    private String type;

    /**
     * 关联单据
     */
    private String source;

    /**
     * 单据时间
     */
    private LocalDateTime time;

    /**
     * 核销金额
     */
    private BigDecimal money;


}
