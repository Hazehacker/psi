package com.zeroone.star.reportmanagement.entity;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 采购明细表excel导出时数据传输对象
 *
 * @author chuming_7
 * @since 2025-10-28
 */
@Data
@ColumnWidth(20)
public class ProcurementDetailFormExcel {
    @ExcelProperty("所属组织ID")
    @ColumnWidth(15)
    private String frame;
    @ExcelProperty("供应商")
    private String supplier;
    @ExcelProperty("单据时间")
    @ColumnWidth(25)
    private String time;
    @ExcelProperty("单据编号")
    private String number;
    @ExcelProperty("商品名称")
    private String name;
    @ExcelProperty("辅助属性")
    private String attr;
    @ExcelProperty("仓库")
    private String warehouse;
    @ExcelProperty("单位")
    @ColumnWidth(15)
    private String unit;
    @ExcelProperty("数量")
    @ColumnWidth(15)
    private Integer nums;
    @ExcelProperty("折扣额")
    @ColumnWidth(15)
    private Double dsc;
    @ExcelProperty("金额")
    @ColumnWidth(15)
    private Double total;
    @ExcelProperty("税额")
    @ColumnWidth(15)
    private Double tat;
    @ExcelProperty("价税合计")
    @ColumnWidth(15)
    private Double tpt;
    @ExcelProperty("备注信息")
    @ColumnWidth(30)
    private String data;
}
