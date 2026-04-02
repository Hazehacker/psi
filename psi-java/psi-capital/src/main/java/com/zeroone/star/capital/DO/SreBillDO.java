package com.zeroone.star.capital.DO;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 销售退货单核销详情 */
@Getter
@Setter
@TableName(value = "sre_bill")
public class SreBillDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 主键ID */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /** 所属单据 */
    private String pid;
    /** 核销类型 */
    private String type;
    /** 关联单据 */
    private String source;
    /** 单据时间 */
    private LocalDateTime time;
    /** 核销金额 */
    private BigDecimal money;
}
