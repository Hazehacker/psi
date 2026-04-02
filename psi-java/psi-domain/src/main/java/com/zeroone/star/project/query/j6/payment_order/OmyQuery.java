package com.zeroone.star.project.query.j6.payment_order;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;


@EqualsAndHashCode(callSuper = false)
@Data
@ApiModel("主表查询条件")
public class OmyQuery extends PageQuery {
    @ApiModelProperty(value = "供应商",example = "中创集团")
    private String supplier;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期",example = "2025-10-22")
    private LocalDate startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束日期",example = "2025-10-22")
    private LocalDate endTime;

    @ApiModelProperty(value = "单据编号",example = "N111")
    private String number;

    @ApiModelProperty(value = "关联人员",example = "jack")
    private String people;

    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]",example = "1")
    private Integer examine;

    @ApiModelProperty(value = "核销状态[0:未核销|1:部分核销|2:已核销]",example = "0")
    private Integer nucleus;

    @ApiModelProperty(value = "制单人",example = "管理员")
    private String user;

    @ApiModelProperty(value = "结算账户",example = "中创资金")
    private String account;

    @ApiModelProperty(value = "备注信息",example = "新增付款单示例")
    private String data1;


}
