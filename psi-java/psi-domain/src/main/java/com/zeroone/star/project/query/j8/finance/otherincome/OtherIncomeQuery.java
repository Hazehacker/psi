package com.zeroone.star.project.query.j8.finance.otherincome;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 其他收入单查询数据对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("其他收入单查询参数")
public class OtherIncomeQuery extends PageQuery {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "所属组织")
    private String frame;

    @ApiModelProperty(value = "客户", example = "客户001")
    private String customer;

    @ApiModelProperty(value = "单据编号", example = "QTSRD251022113030")
    private String number;

    @ApiModelProperty(value = "查询起始时间", example = "2025-09-03")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(value = "查询截止时间", example = "2025-10-24")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty(value = "结算/资金账户", example = "ICBC001")
    private String account;

    @ApiModelProperty(value = "关联人员", example = "people002")
    private String people;

    @ApiModelProperty(value = "制单人", example = "制单人002")
    private String user;

    @ApiModelProperty(value = "审核状态", example = "1")
    private Integer examine;

    @ApiModelProperty(value = "核销状态", example = "0")
    private Integer nucleus;

    @ApiModelProperty(value = "备注信息", example = "这是一笔其它收入")
    private String data;
}
