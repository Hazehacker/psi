package com.zeroone.star.purchase.DO;



import cn.hutool.core.date.DateTime;
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
 * 销售单核销表
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-25
 */

@TableName(value = "sell_bill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellBillDO {

    /**
     * 销售单核销id
     */
    @TableId(value = "id",type= IdType.ASSIGN_ID)
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
