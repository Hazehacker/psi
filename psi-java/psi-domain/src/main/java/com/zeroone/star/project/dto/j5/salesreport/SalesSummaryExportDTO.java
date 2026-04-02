package com.zeroone.star.project.dto.j5.salesreport;


import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 描述：仅包含Excel需要的扁平化字段
 */
@Data
public class SalesSummaryExportDTO {

    // 分组依据
    private String groupField;

    @ExcelProperty("商品名称")
    private String productName;

    @ExcelProperty("属性")
    private String attribute;

    @ExcelProperty("仓库")
    private String warehouse;

    @ExcelProperty("单位")
    private String unit;

    @ExcelProperty("销售单价")
    private BigDecimal SalesPrice;

    @ExcelProperty("销售数量")
    private Integer salesQuantity;

    @ExcelProperty("销售金额")
    private BigDecimal salesAmount;

    @ExcelProperty("购退单价")
    private BigDecimal returnPrice;

    @ExcelProperty("购退数量")
    private Integer returnQuantity;

    @ExcelProperty("购退金额")
    private BigDecimal returnAmount;

    @ExcelProperty("汇总数量")
    private Integer totalQuantity;

    @ExcelProperty("汇总金额")
    private BigDecimal totalAmount;
}
