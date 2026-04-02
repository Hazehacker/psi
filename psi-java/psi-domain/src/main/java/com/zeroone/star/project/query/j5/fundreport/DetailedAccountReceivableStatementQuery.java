package com.zeroone.star.project.query.j5.fundreport;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 应收款明细表查询类
 */
@Data
@ApiModel("应收款明细表查询参数")
public class DetailedAccountReceivableStatementQuery extends PageQuery {

    @ApiModelProperty(value = "客户", example = "李虎")
    private String customer;

    @ApiModelProperty(value = "客户类型", example = "全部用户")
    private String category;

    @ApiModelProperty(value = "开始时间", example = "2025-09-20")
    private String startTime;

    @ApiModelProperty(value = "结束时间", example = "2025-10-20")
    private String endTime;
}
