package com.zeroone.star.project.query.j5.fundreport;

import cn.hutool.core.date.DateTime;
import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 现金银行报表查询类
 */
@Data
@ApiModel("现金银行报表查询参数")
public class CashBankStatementQuery extends PageQuery {

    @ApiModelProperty(value = "客户", example = "李虎")
    private String customer;

    @ApiModelProperty(value = "供应商", example = "工厂")
    private String supplier;

    @ApiModelProperty(value = "开始时间", example = "2025-09-20")
    private String startTime;

    @ApiModelProperty(value = "结束时间", example = "2025-10-20")
    private String endTime;

    @ApiModelProperty(value = "制单人", example = "张三")
    private String user;

    @ApiModelProperty(value = "资金账户", example = "李虎")
    private String account;
}
