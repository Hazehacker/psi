package com.zeroone.star.project.query.j8.finance.tradeexpense;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel(value = "TradeExpenseQuery", description = "交易支出查询条件")
public class TradeExpenseQuery extends PageQuery {
    @ApiModelProperty(value = "供应商ID", example = "9")
    private String supplierId;

    @ApiModelProperty(value = "客户ID", example = "22")
    private String customerId;

    @ApiModelProperty(value = "单据编号", example = "XSD2506131730194")
    private String orderNumber;

    @ApiModelProperty(value = "支出类别ID", example = "3")
    private List<String> expenseCategoryId;

    @ApiModelProperty(value = "单据开始日期（yyyy-MM-dd）", example = "2025-06-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(value = "单据结束日期（yyyy-MM-dd）", example = "2025-06-30")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty(value = "结算状态（0=未结算,1=部分结算,2=已结算）", example = "1")
    private List<Integer> settleStatus;

    @ApiModelProperty(value = "单据类型（如:sell,buy,bre,sre,entry,extry,swap）", example = "buy")
    private List<String> documentType;
}
