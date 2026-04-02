package com.zeroone.star.project.dto.j5.salesreport;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("销售利润表详细查询参数")
public class SalesProductDetail {

    @ExcelProperty("商品名称")
    @ApiModelProperty(value = "商品名称", example = "刀具")
    private String goodsName;

    @ExcelProperty("辅助属性")
    @ApiModelProperty(value = "辅助属性", example = "")
    private String attr;

    @ExcelProperty("单位")
    @ApiModelProperty(value = "单位", example = "个")
    private String unit;

    @ExcelProperty("单价")
    @ApiModelProperty(value = "单价", example = "1")
    private String price;

    @ExcelProperty("数量")
    @ApiModelProperty(value = "数量", example = "")
    private String nums;

    @ExcelProperty("折扣额")
    @ApiModelProperty(value = "折扣额", example = "")
    private BigDecimal discountInfo;

    @ExcelProperty("金额")
    @ApiModelProperty(value = "金额", example = "")
    private BigDecimal totalInfo;

    @ExcelProperty("税率")
    @ApiModelProperty(value = "税率", example = "")
    private BigDecimal tax;

    @ExcelProperty("价税合计")
    @ApiModelProperty(value = "价税合计", example = "")
    private BigDecimal tpt;

    @ExcelProperty("成本")
    @ApiModelProperty(value = "成本", example = "2.0000")
    private BigDecimal buy;

    @ExcelProperty("毛利润")
    @ApiModelProperty(value = "毛利润", example = "0")
    private BigDecimal grossProfit;

    @ExcelProperty("毛利率")
    @ApiModelProperty(value = "毛利率", example = "0")
    private BigDecimal grossProfitMargin;

}
