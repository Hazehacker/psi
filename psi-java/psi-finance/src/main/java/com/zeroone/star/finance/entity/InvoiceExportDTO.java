package com.zeroone.star.finance.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 购销发票导出DTO
 * 涉及数据库表：invoice（发票表）、buy（采购表）、supplier（供应商表）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Api("购销发票导出对象")
public class InvoiceExportDTO {

    @ApiModelProperty(value = "单据类型（如：采购单、销售退货单）", example = "采购单")
    @ExcelProperty("单据类型") // Excel表头名称
    private String billType;

    @ApiModelProperty(value = "所属组织", example = "默认组织")
    @ExcelProperty("所属组织")
    private String org;

    @ApiModelProperty(value = "往来单位", example = "郑州东方之花医药股份公司")
    @ExcelProperty("往来单位")
    private String contactUnit;

    @ApiModelProperty(value = "单据时间", example = "2025-10-27 00:00:00")
    @ExcelProperty("单据时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime billTime;

    @ApiModelProperty(value = "单据编号", example = "ZCGD2510271738512")
    @ExcelProperty("单据编号")
    private String billNumber;

    @ApiModelProperty(value = "发票状态", example = "未开具")
    @ExcelProperty("发票状态")
    private String invoiceStatus;

    @ApiModelProperty(value = "单据金额（元）", example = "1.00")
    @ExcelProperty("单据金额（元）")
    private String billAmount;

    @ApiModelProperty(value = "已开票金额（元）", example = "0.00")
    @ExcelProperty("已开票金额（元）")
    private String invoicedAmount;

    @ApiModelProperty(value = "未开票金额（元）", example = "1.00")
    @ExcelProperty("未开票金额（元）")
    private String uninvoicedAmount;
}