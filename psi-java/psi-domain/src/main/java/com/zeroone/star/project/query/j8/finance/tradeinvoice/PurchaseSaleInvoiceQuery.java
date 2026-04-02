package com.zeroone.star.project.query.j8.finance.tradeinvoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zeroone.star.project.query.PageQuery;
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
@Builder
public class PurchaseSaleInvoiceQuery extends PageQuery {
    // 供应商
    @ApiModelProperty(value = "供应商", example = "31")
    private String supplier;
    // 客户
    @ApiModelProperty(value = "客户", example = "8")
    private String customer;
    // 单据编号
    @ApiModelProperty(value = "单据编号", example = "123452345")
    private String number;
    // 单据类型

    @ApiModelProperty(value = "单据类型", example = "bre")
    private List<String> mold;

    // 开始时间
    @ApiModelProperty(value = "开始时间", example = "2025-09-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    // 结束时间
    @ApiModelProperty(value = "结束时间", example = "2025-10-22")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    /*// 发票号码
    @ApiModelProperty(value = "发票号码", example = "2")
    private String inr;*/
    // 发票状态
    @ApiModelProperty(value = "发票状态", example="已开票")
    private String invoice;
    // 发票抬头
   /* @ApiModelProperty(value = "发票抬头", example = "3")
    private String title;*/
}
