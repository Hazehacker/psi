package com.zeroone.star.reportmanagement.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 其他收支明细表Excel导出实体
 * @author 天天困
 * @date 2025/10/28
 */
@Data
@ColumnWidth(20)
public class OtherIncomeExpenditureDetailExcel {

    @ExcelProperty("单据类型")
    @ColumnWidth(15)
    private String documentType;

    @ExcelProperty("所属组织")
    @ColumnWidth(20)
    private String frame;

    @ExcelProperty("往来单位")
    @ColumnWidth(25)
    private String counterparty;

    @ExcelProperty(value = "单据时间")
    @ColumnWidth(20)
    private String dateTime;

    @ExcelProperty("单据编号")
    @ColumnWidth(25)
    private String documentNumber;

    @ExcelProperty("收支类别")
    @ColumnWidth(20)
    private String category;

    @ExcelProperty("收入")
    @ColumnWidth(15)
    private BigDecimal income;

    @ExcelProperty("支出")
    @ColumnWidth(15)
    private BigDecimal expenditure;

    @ExcelProperty("结算账户")
    @ColumnWidth(20)
    private String settlementAccount;

    @ExcelProperty("备注信息")
    @ColumnWidth(30)
    private String remark;
}
