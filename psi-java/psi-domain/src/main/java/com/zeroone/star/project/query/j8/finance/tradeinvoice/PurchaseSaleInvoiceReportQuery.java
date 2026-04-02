package com.zeroone.star.project.query.j8.finance.tradeinvoice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseSaleInvoiceReportQuery{
    // 供应商
    @ApiModelProperty(value = "供应商", example = "客户")
    private String supplier;
    // 客户
    @ApiModelProperty(value = "客户", example = "客户")
    private String customer;
    // 单据编号
    @ApiModelProperty(value = "单据编号", example = "ICE")
    private String number;

    // 单据类型
    @ApiModelProperty(value = "单据类型", example = "其它")
    private String mold;

    // 开始时间
    @ApiModelProperty(value = "开始时间", example = "2020-09-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    // 结束时间
    @ApiModelProperty(value = "结束时间", example = "2027-10-22")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    //前端勾选的要导出的数据id
    @ApiModelProperty(value = "前端勾选的要导出的数据id", example = "0,1,2")
    private List<Integer> ids;

//    发票号码
    @ApiModelProperty(value = "发票号码", example = "FP")
    private String invoiceNumber;

//    发票抬头
    @ApiModelProperty(value = "发票抬头", example = "某")
    private String title;


}




























