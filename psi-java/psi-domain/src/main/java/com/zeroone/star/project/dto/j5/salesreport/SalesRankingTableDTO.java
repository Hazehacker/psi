package com.zeroone.star.project.dto.j5.salesreport;


import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@ApiModel("销售排行报表数据对象")
@Accessors(chain = true)
public class SalesRankingTableDTO {

    @ExcelProperty("商品名称")
    @ApiModelProperty(value = "商品名称", example = "电脑")
    private String productName;

    @ExcelProperty("辅助属性")
    @ApiModelProperty(value = "辅助属性", example = "辅助属性")
    private String auxiliaryAttribute;

    @ExcelProperty("商品编号")
    @ApiModelProperty(value = "商品编号", example = "123313")
    private String productNumber;

    @ExcelProperty("规格型号")
    @ApiModelProperty(value = "规格型号", example = "超大杯")
    private String specificationModel;

    @ExcelProperty("单位")
    @ApiModelProperty(value = "单位", example = "个")
    private String unit;

    @ExcelProperty("数量")
    @ApiModelProperty(value = "数量", example = "12")
    private Integer quantity;

    @ExcelProperty("折扣额")
    @ApiModelProperty(value = "折扣额", example = "0.2")
    private BigDecimal discountAmount;

    @ExcelProperty("税额")
    @ApiModelProperty(value = "税额", example = "0.2")
    private BigDecimal taxAmount;

    @ExcelProperty("价税合计")
    @ApiModelProperty(value = "价税合计", example = "0.2")
    private BigDecimal totalAmountWithTax;

    @ExcelProperty("成本")
    @ApiModelProperty(value = "成本", example = "2.5")
    private BigDecimal cost;

    @ExcelProperty("总成本")
    @ApiModelProperty(value = "总成本", example = "2.5")
    private BigDecimal totalCost;

    @ExcelProperty("毛利润")
    @ApiModelProperty(value = "毛利润", example = "50")
    private BigDecimal grossProfit;

    @ExcelProperty("毛利率")
    @ApiModelProperty(value = "毛利率", example = "0.24")
    private BigDecimal grossProfitMargin;
}
