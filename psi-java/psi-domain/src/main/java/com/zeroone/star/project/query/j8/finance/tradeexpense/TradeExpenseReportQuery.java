package com.zeroone.star.project.query.j8.finance.tradeexpense;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel("购销费用报表查询参数")
public class TradeExpenseReportQuery extends PageQuery {
    @ApiModelProperty(value = "供应商ID", example = "S001")
    private String supplierId;

    @ApiModelProperty(value = "客户ID", example = "C001")
    private String customerId;

    @ApiModelProperty(value = "单据编号", example = "XSD2506131730194")
    private String documentNumber;

    @ApiModelProperty(value = "支出类别ID", example = "I001")
    private List<String> expenseTypeId;

    @ApiModelProperty(value = "单据开始日期（yyyy-MM-dd）", example = "2025-06-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(value = "单据结束日期（yyyy-MM-dd）", example = "2025-06-30")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty(value = "结算状态（1=部分结算，2=已结算）", example = "1")
    private List<Integer> settlementStatus;

    @ApiModelProperty(value = "单据类型（如：销售单、采购单、支出单）", example = "sell")
    private List<String> documentType;
}
