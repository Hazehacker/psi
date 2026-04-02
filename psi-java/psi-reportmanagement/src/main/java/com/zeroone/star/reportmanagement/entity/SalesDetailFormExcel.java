package com.zeroone.star.reportmanagement.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * excel导出时数据传输对象
 * @author rainsilent
 * @date 2025/10/27
 */
@Data
@ColumnWidth(20)
public class SalesDetailFormExcel {

    @ExcelProperty("所属组织")
    @ColumnWidth(15)
    private String frame;

    @ExcelProperty("客户")
    private String customer;

    @ExcelProperty("单据时间")
    @ColumnWidth(25)
    private String time;

    @ExcelProperty("单据编号")
    private String number;

    @ExcelProperty(value = "商品名称")
    private String name;

    @ExcelProperty(value = "辅助属性")
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
