package com.zeroone.star.project.dto.j8.finance.tradeinvoice;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("购销发票报表")
public class TradeInvoiceReportDTO {
    @ExcelProperty(value = "购销发票id")
    @ApiModelProperty(value = "购销发票id", example="##")
    private String Id;
    // 单据类型
    @ExcelProperty(value = "单据类型")
    @ApiModelProperty(value = "单据类型", example="##")
    private String type;
    // 所属组织
    @ExcelProperty(value = "所属组织")
    @ApiModelProperty(value = "所属组织", example="##")
    private String frame;
    // 往来单位
    @ExcelProperty(value = "往来单位")
    @ApiModelProperty(value = "往来单位", example="##")                    //自定义
    private String businessUnit;
    // 单据时间
    @ExcelProperty(value = "单据时间")
    @ApiModelProperty(value = "单据时间", example="2025-10-22 ")
    private String time;
    // 单据编号
    @ExcelProperty(value = "单据编号")
    @ApiModelProperty(value = "单据编号", example="##")
    private String number;
    // 单据金额
    @ExcelProperty(value = "单据金额")
    @ApiModelProperty(value = "单据金额", example="8000")
    private Double total;

    //开票时间
    @ExcelProperty(value = "开票时间")
    @ApiModelProperty(value = "开票时间")
    private String invoiceDate;

    //发票号码
    @ExcelProperty(value = "发票号码")
    @ApiModelProperty(value = "发票号码")
    private String invoiceNumber;

    //发票抬头
    @ExcelProperty(value = "发票抬头")
    @ApiModelProperty(value = "发票抬头")
    private String title;

    //发票金额
    @ExcelProperty(value = "发票金额")
    @ApiModelProperty(value = "发票金额")
    private BigDecimal money;

    //发票附件
    @ExcelProperty(value = "发票附件")
    @ApiModelProperty(value = "发票附件")
    private String file;

    //备注信息
    @ExcelProperty(value = "备注信息")
    @ApiModelProperty(value = "备注信息")
    private String data;
}





























