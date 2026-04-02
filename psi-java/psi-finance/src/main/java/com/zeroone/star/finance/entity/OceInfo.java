package com.zeroone.star.finance.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 其它支出单详情
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
@Getter
@Setter
@TableName("oce_info")
public class OceInfo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ExcelProperty(value = "详情ID", index = 0)
    private String id;

    @ExcelProperty(value = "所属单据ID", index = 1)
    private String pid;

    @ExcelProperty(value = "所属费用", index = 2)
    private String source;

    @ExcelProperty(value = "收支类型", index = 3)
    private String iet;

    @ExcelProperty(value = "结算金额", index = 4)
    private BigDecimal money;

    @ExcelProperty(value = "备注信息", index = 5)
    private String data;


}
