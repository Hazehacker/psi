package com.zeroone.star.reportmanagement.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import lombok.Data;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 应付账款明细表Excel导出实体
 * @author 天天困
 * @date 2025/10/28
 */
@Data
@ColumnWidth(20)
public class DetailedAccountPayableStatementExcel {

    @ExcelProperty("供应商")
    @ColumnWidth(25)
    private String supplier;

    @ExcelProperty("单据类型")
    @ColumnWidth(15)
    private String documentType;

    @ExcelProperty("所属组织")
    @ColumnWidth(20)
    private String frame;

    @ExcelProperty(value = "单据时间", converter = PurchaseOrderTrackingFormExcelLocalDateStringConverter.class)
    @ColumnWidth(20)
    private LocalDate dateTime;

    @ExcelProperty("单据编号")
    @ColumnWidth(25)
    private String documentNumber;

    @ExcelProperty("增加应付款")
    @ColumnWidth(15)
    private BigDecimal accountsPayableIncrease;

    @ExcelProperty("增加预付款")
    @ColumnWidth(15)
    private BigDecimal advancePaymentIncrease;

    @ExcelProperty("应付款余额")
    @ColumnWidth(15)
    private BigDecimal accountsPayableBalance;

    @ExcelProperty("备注")
    @ColumnWidth(30)
    private String remark;
}
