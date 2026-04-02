package com.zeroone.star.project.dto.j5.salesreport;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("销售利润表Excel格式DTO")
public class SalesProfitExcelDTO {

    @ExcelProperty("单据类型")
    @ApiModelProperty(value = "单据类型")
    private String type;

    @ExcelProperty("所属组织")
    @ApiModelProperty(value = "所属组织")
    private String frame;

    @ExcelProperty("客户")
    @ApiModelProperty(value = "客户")
    private String customer;

    @ExcelProperty("单据时间")
    @ApiModelProperty(value = "单据时间")
    private String time;

    @ExcelProperty("单据编号")
    @ApiModelProperty(value = "单据编号")
    private String number;

    @ExcelProperty("商品名称")
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ExcelProperty("辅助属性")
    @ApiModelProperty(value = "辅助属性")
    private String attr;

    @ExcelProperty("单位")
    @ApiModelProperty(value = "单位")
    private String unit;

    @ExcelProperty("单价")
    @ApiModelProperty(value = "单价")
    private String price;

    @ExcelProperty("数量")
    @ApiModelProperty(value = "数量")
    private String nums;

    @ExcelProperty("折扣额")
    @ApiModelProperty(value = "折扣额")
    private BigDecimal discountInfo;

    @ExcelProperty("金额")
    @ApiModelProperty(value = "金额")
    private BigDecimal totalInfo;

    @ExcelProperty("税额")
    @ApiModelProperty(value = "税额")
    private BigDecimal tax;

    @ExcelProperty("价税合计")
    @ApiModelProperty(value = "价税合计")
    private BigDecimal tpt;

    @ExcelProperty("单据金额")
    @ApiModelProperty(value = "单据金额")
    private BigDecimal total;

    @ExcelProperty("优惠金额")
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discount;

    @ExcelProperty("实际金额")
    @ApiModelProperty(value = "实际金额")
    private BigDecimal actual;

    @ExcelProperty("成本")
    @ApiModelProperty(value = "成本")
    private BigDecimal buy;

    @ExcelProperty("毛利润")
    @ApiModelProperty(value = "毛利润")
    private BigDecimal grossProfit;

    @ExcelProperty("毛利率")
    @ApiModelProperty(value = "毛利率")
    private BigDecimal grossProfitMargin;

    @ExcelProperty("单据费用")
    @ApiModelProperty(value = "单据费用")
    private BigDecimal cost;

    @ExcelProperty("净利润")
    @ApiModelProperty(value = "净利润")
    private BigDecimal netProfit;

    @ExcelProperty("净利率")
    @ApiModelProperty(value = "净利率")
    private BigDecimal netProfitMargin;

    @ExcelProperty("核销金额")
    @ApiModelProperty(value = "核销金额")
    private BigDecimal money;

    @ExcelProperty("核销状态")
    @ApiModelProperty(value = "核销状态")
    private String nucleus;

    @ExcelProperty("制单人")
    @ApiModelProperty(value = "制单人")
    private String user;

    @ExcelProperty("关联人员")
    @ApiModelProperty(value = "关联人员")
    private String people;

    @ExcelProperty("备注信息")
    @ApiModelProperty(value = "备注信息")
    private String data;
}