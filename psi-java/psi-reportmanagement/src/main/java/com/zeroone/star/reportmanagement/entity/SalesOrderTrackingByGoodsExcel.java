package com.zeroone.star.reportmanagement.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 销售订单跟踪表（以商品聚合）
 *
 * @author leyu
 * @date 2025-11-14
 */
@Data
@ColumnWidth(20)
public class SalesOrderTrackingByGoodsExcel {
    // Type=1 父级 (商品)
    @ExcelProperty("商品名称")
    private String goodsName;

    @ExcelProperty("辅助属性")
    private String attr;

    @ExcelProperty("仓库名称")
    private String warehouseName;

    // Type=1 子级 (单据行)
    @ExcelProperty("所属组织")
    @ColumnWidth(15)
    private String frameName;

    @ExcelProperty("客户")
    private String customerName;

    @ExcelProperty("单据时间")
    @ColumnWidth(25)
    private String time;

    @ExcelProperty("单据编号")
    private String number;

    @ExcelProperty(value = "到货日期", converter = SalesOrderTrackingFormExcelLocalDateStringConverter.class)
    @ColumnWidth(25)
    private LocalDate arrival;

    @ExcelProperty("单位")
    @ColumnWidth(10)
    private String unit;

    @ExcelProperty("单价")
    private String price;

    @ExcelProperty("数量")
    private Integer nums;

    @ExcelProperty("金额")
    private BigDecimal total;

    @ExcelProperty("备注信息")
    @ColumnWidth(30)
    private String itemRemark;

    @ExcelProperty("未入库数量")
    private Integer unstockedQuantity;

    @ExcelProperty("未入库金额")
    private BigDecimal unstockedAmount;

    @ExcelProperty(value = "入库状态", converter = SalesOrderStatusConverter.class) // (这是订单的整体入库状态)
    @ColumnWidth(15)
    private Integer state;
}
