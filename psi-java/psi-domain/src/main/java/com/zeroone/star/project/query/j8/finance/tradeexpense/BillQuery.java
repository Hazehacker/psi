package com.zeroone.star.project.query.j8.finance.tradeexpense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("单据费用参数")
public class BillQuery {
    @ApiModelProperty(value = "供应商",example = "中创集团")
    private String supplier;
    @ApiModelProperty(value = "客户",example = "理想")
    private String customer;
    @ApiModelProperty(value = "单据编号",example = "XSD2506131730194\n")
    private String number;
    @ApiModelProperty(value = "支出类别",example = "生活")
    private String iet;
    @ApiModelProperty(value = "起始时间",example = "2024-10-22")
    private LocalDateTime startTime;
    @ApiModelProperty(value = "结束时间",example = "2025-10-22")
    private  LocalDateTime endTime;
    @ApiModelProperty(value = "结算状态",example = "部分结算")
    private String state;
    @ApiModelProperty(value = "单据类型",example = "采购单")
    private String mold;
}
