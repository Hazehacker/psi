package com.zeroone.star.project.query.j8.finance.othexpense;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 其他支出单查询数据对象
 */
@Data
@ApiModel("其他支出单查询参数")
public class OtherExpenseQuery extends PageQuery {
    @ApiModelProperty(value = "供应商", example = "F001")
    private String supplier;
    @ApiModelProperty(value = "单据编号", example = "QTZCD2510201810404")
    private String number;
    @ApiModelProperty(value = "查询起始时间", example = "2025-09-03")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @ApiModelProperty(value = "查询截止时间", example = "2025-10-24")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @ApiModelProperty(value = "结算/资金账户", example = "CCB001")
    private String account;
    @ApiModelProperty(value = "关联人员", example = "people001")
    private String people;
    @ApiModelProperty(value = "制单人", example = "U001")
    private String user;
    @ApiModelProperty(value = "审核状态", example = "1")
    private Integer examine;
    @ApiModelProperty(value = "核销状态", example = "0")
    private Integer nucleus;
    @ApiModelProperty(value = "备注信息", example = "test")
    private String data;
}
