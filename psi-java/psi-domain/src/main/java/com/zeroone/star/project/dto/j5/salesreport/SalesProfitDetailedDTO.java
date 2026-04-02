package com.zeroone.star.project.dto.j5.salesreport;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("销售利润表详细查询参数")
public class SalesProfitDetailedDTO {

    @ExcelProperty("单据类型")
    @ApiModelProperty(value = "单据类型")
    private String type;

    @ExcelProperty("所属组织")
    @ApiModelProperty(value = "所属组织", example = "工厂")
    private String frame;

    @ExcelProperty("客户")
    @ApiModelProperty(value = "客户", example = "李四")
    private String customer;

    @ExcelProperty("单据时间")
    @ApiModelProperty(value = "单据时间", example = "2025-10-01")
    private String time;

    @ExcelProperty("单据编号")
    @ApiModelProperty(value = "单据编号", example = "100001")
    private String number;

    // 商品明细字段
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

    @ExcelProperty("税率")
    @ApiModelProperty(value = "税率")
    private BigDecimal tax;

    @ExcelProperty("价税合计")
    @ApiModelProperty(value = "价税合计")
    private BigDecimal tpt;

    // 单据金额字段
    @ExcelProperty("单据金额")
    @ApiModelProperty(value = "单据金额", example = "0")
    private BigDecimal total;

    @ExcelProperty("优惠金额")
    @ApiModelProperty(value = "优惠金额", example = "0")
    private BigDecimal discount;

    @ExcelProperty("实际金额")
    @ApiModelProperty(value = "实际金额", example = "0")
    private BigDecimal actual;

    // 成本利润字段
    @ExcelProperty("成本")
    @ApiModelProperty(value = "成本", example = "2.0000")
    private BigDecimal buy;

    @ExcelProperty("毛利润")
    @ApiModelProperty(value = "毛利润", example = "0")
    private BigDecimal grossProfit;

    @ExcelProperty("毛利率")
    @ApiModelProperty(value = "毛利率", example = "0")
    private BigDecimal grossProfitMargin;

    @ExcelProperty("单据费用")
    @ApiModelProperty(value = "单据费用", example = "0")
    private BigDecimal cost;

    @ExcelProperty("净利润")
    @ApiModelProperty(value = "净利润", example = "0")
    private BigDecimal netProfit;

    @ExcelProperty("净利率")
    @ApiModelProperty(value = "净利率", example = "0")
    private BigDecimal netProfitMargin;

    // 核销相关字段
    @ExcelProperty("核销金额")
    @ApiModelProperty(value = "核销金额", example = "0")
    private BigDecimal money;

    @ExcelProperty("核销状态")
    @ApiModelProperty(value = "核销状态", example = "1")
    private Integer nucleus;

    // 人员字段
    @ExcelProperty("制单人")
    @ApiModelProperty(value = "制单人", example = "101")
    private String user;

    @ExcelProperty("关联人员")
    @ApiModelProperty(value = "关联人员", example = "101")
    private String people;

    @ExcelProperty("备注信息")
    @ApiModelProperty(value = "备注信息", example = "暂无")
    private String data;

    @ApiModelProperty(value = "单商品数据")
    private List<SalesProductDetail> list;
}