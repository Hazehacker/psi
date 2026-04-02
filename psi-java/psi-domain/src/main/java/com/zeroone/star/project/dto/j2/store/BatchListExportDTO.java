package com.zeroone.star.project.dto.j2.store;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.math.BigDecimal;

@Data
@ApiModel("BatchListExportDTO")
public class BatchListExportDTO {

    @ExcelProperty(value = "商品名称", index = 0)
    @ColumnWidth(20)
    private String name;

    @ExcelProperty(value = "商品编号", index = 1)
    @ColumnWidth(20)
    private String number;

    @ExcelProperty(value = "规格型号", index = 2)
    @ColumnWidth(20)
    private String spec;

    @ExcelProperty(value = "商品品牌", index = 3)
    @ColumnWidth(20)
    private String brand;

    @ExcelProperty(value = "商品单位", index = 4)
    @ColumnWidth(20)
    private String unit;

    @ExcelProperty(value = "商品条码", index = 5)
    @ColumnWidth(20)
    private String code;

    @ExcelProperty(value = "商品分类", index = 6)
    @ColumnWidth(20)
    private String categoryName;

    @ExcelProperty(value = "属性名称", index = 7)
    @ColumnWidth(20)
    private String attrName;

    @ExcelProperty(value = "批次号", index = 8)
    @ColumnWidth(20)
    private String batchNumber;

    @ExcelProperty(value = "生产日期", index = 9)
    @ColumnWidth(20)
    private String productDate;

    @ExcelProperty(value = "过期日期", index = 10)
    @ColumnWidth(20)
    private String expireDate;

    @ExcelProperty(value = "库存数量", index = 11)
    @ColumnWidth(20)
    private BigDecimal stock;

    @ExcelProperty(value = "是否预警", index = 12)
    @ColumnWidth(20)
    private String isWarning;

    @ExcelProperty(value = "保质期(天)", index = 13)
    @ColumnWidth(20)
    private Integer protect;

    @ExcelProperty(value = "预警阈值", index = 14)
    @ColumnWidth(20)
    private BigDecimal threshold;

    @ExcelProperty(value = "备注", index = 15)
    @ColumnWidth(20)
    private String data;
}