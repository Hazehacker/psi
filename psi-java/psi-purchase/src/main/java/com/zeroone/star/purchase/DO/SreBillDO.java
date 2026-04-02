package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author 唐怀瑟
 * @Data 2025/10/24 23:09
 * @PackageName: com.zeroone.star.purchase.DO
 * @CLASSNAME: sreBillDO
 * @Description: 销售退货单核销详情
 * @Version 1.0
 */
@Getter
@Setter
@TableName(value = "sre_bill")
public class SreBillDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
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
    private LocalDateTime time;

    /**
     * 核销金额
     */
    private BigDecimal money;
}
