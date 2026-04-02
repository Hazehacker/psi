package com.zeroone.star.purchase.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 采购退货单导出简单报表Excel类
 *
 * @author Hazenix 
 * @since 2025-11-13
 */
@Data
public class PurchaseReturnExportExcel {

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "所属组织"}, index = 0)
    private String frame;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "供应商"}, index = 1)
    private String supplier;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "单据时间"}, index = 2)
    private String time;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "单据编号"}, index = 3)
    private String number;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "单据金额"}, index = 4)
    private String total;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "实际金额"}, index = 5)
    private String actual;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "单据收款"}, index = 6)
    private String money;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "核销金额"}, index = 7)
    private String cancel;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "单据费用"}, index = 8)
    private String cost;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "关联人员"}, index = 9)
    private String people;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "审核状态"}, index = 10)
    private String examine;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "核销状态"}, index = 11)
    private String nucleus;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "费用状态"}, index = 12)
    private String cse;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "发票状态"}, index = 13)
    private String invoice;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "核对状态"}, index = 14)
    private String check;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "制单人"}, index = 15)
    private String user;

    @ColumnWidth(15)
    @ExcelProperty(value = {"采购退货单列表", "备注信息"}, index = 16)
    private String data;

}
