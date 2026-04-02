package com.zeroone.star.project.query.j8.finance.tradeinvoice;

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
public class InvoiceReportQuery extends PageQuery {
    @ApiModelProperty(value = "供应商ID", example = "31")
    private String supplier;

    @ApiModelProperty(value = "客户ID", example = "8")
    private String customer;

    @ApiModelProperty(value = "单据编号", example = "123452345")
    private String number;

    @ApiModelProperty(value = "单据类型", example = "bre")
    private List<String> mold;

    @ApiModelProperty(value = "开始时间", example = "2025-09-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束时间", example = "2025-10-22")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty(value = "发票号码", example = "2")
    private String inr;

    @ApiModelProperty(value = "发票抬头", example = "3")
    private String title;
}
