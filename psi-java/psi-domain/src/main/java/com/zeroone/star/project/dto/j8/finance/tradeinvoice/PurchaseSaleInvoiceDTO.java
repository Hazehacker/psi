package com.zeroone.star.project.dto.j8.finance.tradeinvoice;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("购销发票")
public class PurchaseSaleInvoiceDTO {
    @ExcelProperty(value = "购销发票id")
    @ApiModelProperty(value = "购销发票id", example="##")
    private Integer Id;
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
    // 发票状态
    @ExcelProperty(value = "发票状态")
    @ApiModelProperty(value = "发票状态", example="已开票")
    private String invoice;
    // 单据金额
    @ExcelProperty(value = "单据金额")
    @ApiModelProperty(value = "单据金额", example="8000")
    private Double total;
    // 已开票金额
    @ExcelProperty(value = "已开票金额")
    @ApiModelProperty(value = "已开票金额", example="30000")             //自定义
    private Double invoicedAmount;
    // 未开票金额
    @ExcelProperty(value = "未开票金额")
    @ApiModelProperty(value = "未开票金额", example="50000")                 //自定义
    private Double uninvoicedAmount;
    //开票金额
    @ExcelProperty(value = "开票金额")
    @ApiModelProperty(value = "开票金额", example="100000")
    private BigDecimal money;




}
