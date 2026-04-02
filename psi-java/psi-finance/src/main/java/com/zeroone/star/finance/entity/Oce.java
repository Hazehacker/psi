package com.zeroone.star.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 其它支出单
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
@Getter
@Setter
public class Oce implements Serializable {

    private static final long serialVersionUID = 1L;


    @ExcelProperty(value = "单据ID", index = 0)
    private String id;

    @ExcelProperty(value = "所属组织", index = 1)
    private String frame;

    @ExcelProperty(value = "供应商", index = 2)
    private String supplier;

    @ExcelProperty(value = "单据时间", index = 3, format = "yyyy-MM-dd HH:mm:ss")
    private String time;

    @ExcelProperty(value = "单据编号", index = 4)
    private String number;

    @ExcelProperty(value = "单据金额", index = 5)
    private BigDecimal total;

    @ExcelProperty(value = "实际金额", index = 6)
    private BigDecimal actual;

    @ExcelProperty(value = "实付金额", index = 7)
    private BigDecimal money;

    @ExcelProperty(value = "结算账户", index = 8)
    private String account;

    @ExcelProperty(value = "关联人员", index = 9)
    private String people;

    @ExcelProperty(value = "单据附件", index = 10)
    private String file;

    @ExcelProperty(value = "备注信息", index = 11)
    private String data;

    @ExcelProperty(value = "扩展信息", index = 12)
    private String more;

    @ExcelProperty(value = "审核状态", index = 13)
    private Integer examine;

    @ExcelProperty(value = "核销状态", index = 14)
    private Integer nucleus;


    @ExcelProperty(value = "制单人", index = 15)
    private String user;

}
