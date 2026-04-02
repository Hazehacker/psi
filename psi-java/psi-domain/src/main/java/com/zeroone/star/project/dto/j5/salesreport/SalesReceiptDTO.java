package com.zeroone.star.project.dto.j5.salesreport;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;
import org.apache.poi.ss.formula.functions.Rate;

import java.math.BigDecimal;

/**
 * 销售收款表数据对象
 * @author yu, leyu(接手)
 * @date 2025/10/21
 */
@Data
@ApiModel("销售收款表数据对象")
public class SalesReceiptDTO {

    @ApiModelProperty(value = "单据类型",example = "销售单")
    @ExcelProperty("单据类型")
    private String type;

    @ApiModelProperty(value = "所属组织", example = "A公司")
    @ExcelProperty("所属组织")
    private String frame;

    @ApiModelProperty(value = "客户", example = "B公司")
    @ExcelProperty("客户")
    private String customer;

    @ApiModelProperty(value = "单据时间", example = "2025-10-19")
    @ExcelProperty("单据时间")
    private String time;

    @ApiModelProperty(value = "单据编号", example = "100001")
    @ExcelProperty("单据编号")
    private String number;

    @ApiModelProperty(value = "单据金额",example = "0")
    @ExcelProperty("单据金额")
    private BigDecimal total;

    @ApiModelProperty(value = "实际金额",example = "0")
    @ExcelProperty("实际金额")
    private BigDecimal actual;

    @ApiModelProperty(value = "单据收款",example = "0")
    @ExcelProperty("单据收款")
    private BigDecimal invoiceCollection;

    @ApiModelProperty(value = "应收款余额",example = "0")
    @ExcelProperty("应收款余额")
    private BigDecimal accountsReceivableBalance ;

    @ApiModelProperty(value = "收款率",example = "0")
    @ExcelProperty("收款率")
    private BigDecimal collectionRate;

    @ApiModelProperty(value = "核销状态", example = "0", allowableValues = "0,1,2")
    @ExcelProperty("核销状态")
    private int nucleus;

    @ApiModelProperty(value = "备注信息",example = "这是一个备注信息!")
    @ExcelProperty("备注信息")
    private String data;

}
