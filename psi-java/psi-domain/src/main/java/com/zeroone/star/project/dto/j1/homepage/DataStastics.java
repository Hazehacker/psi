package com.zeroone.star.project.dto.j1.homepage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataStastics {
    /**
     * 销售笔数
     */
    Integer count;
    /**
     * 销售额
     */
    BigDecimal sales;
    /**
     * 毛利
     */
    BigDecimal gross;
    /**
     * 资金收入
     */
    BigDecimal income;
}
