package com.zeroone.star.project.dto.j5.salesreport;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 销售利润表查询类
 * @author 言语
 * @date 2025/10/20
 */
@Data
@ApiModel("销售利润表查询参数")
public class SalesProfitDTO {

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

    @ExcelProperty("单据金额")
    @ApiModelProperty(value = "单据金额", example = "0")
    private BigDecimal total;

    @ExcelProperty("优惠金额")
    @ApiModelProperty(value = "优惠金额", example = "0")
    private BigDecimal discount;

    @ExcelProperty("实际金额")
    @ApiModelProperty(value = "实际金额", example = "0")
    private BigDecimal actual;

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

    @ExcelProperty("核销金额")
    @ApiModelProperty(value = "核销金额", example = "0")
    private BigDecimal money;

    @ExcelProperty("核销状态")
    @ApiModelProperty(value = "核销状态", example = "1")
    private Integer nucleus;

    @ExcelProperty("制单人")
    @ApiModelProperty(value = "制单人", example = "101")
    private String user;

    @ExcelProperty("关联人员")
    @ApiModelProperty(value = "关联人员", example = "101")
    private String people;

    @ExcelProperty("备注信息")
    @ApiModelProperty(value = "备注信息", example = "暂无")
    private String data;
}