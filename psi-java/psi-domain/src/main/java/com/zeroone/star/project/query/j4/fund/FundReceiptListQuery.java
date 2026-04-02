package com.zeroone.star.project.query.j4.fund;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("收款单列表查询参数")
public class FundReceiptListQuery extends PageQuery {
    @ApiModelProperty(value = "客户", example = "阿伟")
    private String customer;
    @ApiModelProperty(value = "单据时间(开始)",example = "2020-01-01")
    private LocalDate beginTime;
    @ApiModelProperty(value = "单据时间(结束)",example = "2020-01-01")
    private LocalDate endTime;
    @ApiModelProperty(value = "单据编号",example = "123456789")
    private String number;
    @ApiModelProperty(value = "关联人员",example = "李四")
    private String people;
    @ApiModelProperty(value = "制单人",example = "王五")
    private String user;
    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]",example = "1")
    private Integer examine;
    @ApiModelProperty(value = "核销状态", example = "0")
    private Integer nucleus;
    @ApiModelProperty(value = "结算账户", example = "10086")
    private String account;
    @ApiModelProperty(value = "备注信息", example = "无")
    private String data;
}
