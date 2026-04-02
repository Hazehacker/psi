package com.zeroone.star.project.query.j8.finance.tradeinvoice;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * 发票(条件+分页)查询数据对象
 */
@Data
@ApiModel("其他支出单查询参数")
public class TradeInvoiceQuery extends PageQuery {
    @ApiModelProperty(value = "供应商ID", example = "S001")
    private String supplier;
    @ApiModelProperty(value = "客户id", example = "C001")
    private String customer;
    @ApiModelProperty(value = "单据编号", example = "XSD2509251111576")
    private String number;
    @ApiModelProperty(value = "发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]", example = "1")
    private List<Integer> status;
    @ApiModelProperty(value = "查询起始时间", example = "2025-09-03")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @ApiModelProperty(value = "查询截止时间", example = "2025-10-24")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @ApiModelProperty(value = "单据类型", example = "buy,sell,sre,bre")
    private List<String> type;
}
