package com.zeroone.star.project.dto.j5.fundreport;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;

/**
 * 往来单位欠款表查询类
 * @author 言语
 * @date 2025/10/20
 */
@Data
@ApiModel("往来单位欠款表查询参数")
public class UnitArrearsReportDTO {

    @ExcelProperty("单位类型")
    @ApiModelProperty(value = "单位类型", example = "工厂")
    private String type;

    @ExcelProperty("单位名称")
    @ApiModelProperty(value = "单位名称", example = "工厂")
    private String name;

    @ExcelProperty("单位编号")
    @ApiModelProperty(value = "单位编号", example = "1000001")
    private String number;

    @ExcelProperty("应收款余额")
    @ApiModelProperty(value = "应收款余额", example = "0")
    private BigDecimal balanceCol;

    @ExcelProperty("应付款余额")
    @ApiModelProperty(value = "应付款余额", example = "0")
    private BigDecimal balancePay;

    @ExcelIgnore
    @ApiModelProperty(value = "总应收款余额", example = "0")
    private BigDecimal balanceColSum;

    @ExcelIgnore
    @ApiModelProperty(value = "总应付款余额", example = "0")
    private BigDecimal balancePaySum;

    @ExcelProperty("备注信息")
    @ApiModelProperty(value = "备注信息", example = "暂无")
    private String data;

}
