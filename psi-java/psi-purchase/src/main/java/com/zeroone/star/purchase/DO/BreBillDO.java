package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Hazenix 
 * @Data 2025/10/30
 * @Description: 采购退货单核销详情
 */
@Getter
@Setter
@TableName("bre_bill")
public class BreBillDO implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
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
    @TableField(fill = FieldFill.INSERT)
    private String time;

    /**
     * 核销金额
     */
    private BigDecimal money;

}
