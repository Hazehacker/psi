package com.zeroone.star.project.query.j8.finance.otherincome;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 其他收入单查询数据对象
 */
@Data
@ApiModel("其他收入单查询参数")
public class OtherInomeQuery extends PageQuery {
    @ApiModelProperty(value = "客户", example = "客户001")
    private String customer;
    @ApiModelProperty(value = "单据编号", example = "QTZCD2510201810404")
    private String number;
    @ApiModelProperty(value = "查询起始时间", example = "2025-09-03")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @ApiModelProperty(value = "查询截止时间", example = "2025-10-24")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @ApiModelProperty(value = "结算/资金账户", example = "CCB001")
    private String account;
    @ApiModelProperty(value = "关联人员", example = "people001")
    private String people;
    @ApiModelProperty(value = "制单人", example = "制单人001")
    private String user;
    @ApiModelProperty(value = "审核状态", example = "1")
    private int examine;
    @ApiModelProperty(value = "核销状态", example = "0")
    private int nucleus;
    @ApiModelProperty(value = "备注信息", example = "备注test")
    private String data;
}
