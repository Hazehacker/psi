package com.zeroone.star.finance.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 其它支出单核销详情
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
@Getter
@Setter
@TableName("oce_bill")
public class OceBill implements Serializable {

    private static final long serialVersionUID = 1L;


    @ExcelProperty(value = "核销ID", index = 0)
    private String id;

    @ExcelProperty(value = "所属单据ID", index = 1)
    private String pid;

    @ExcelProperty(value = "核销类型", index = 2)
    private String type;

    @ExcelProperty(value = "关联单据", index = 3)
    private String source;

    @ExcelProperty(value = "单据时间", index = 4, format = "yyyy-MM-dd HH:mm:ss")
    private String time;

    @ExcelProperty(value = "核销金额", index = 5)
    private BigDecimal money;

}
