package com.zeroone.star.capital.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: a
 * @description: 采购核销单实体
 * @create: 2025-10-29
 * @Version: 1.0
 **/

@Data
@TableName("buy_bill")
public class BuyBillDO {

    /**
     * id
     */
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
