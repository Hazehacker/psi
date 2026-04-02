package com.zeroone.star.project.query.j5.fundreport;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 应付账明细表查询类
 */
@Data
@ApiModel("应付账明细表查询参数")
public class DetailedAccountPayableStatementQuery extends PageQuery {

    @ApiModelProperty(value = "供应商", example = "公司")
    private String supplier;

    @ApiModelProperty(value = "供应商类型", example = "全部供应商")
    private String supplierType;

    @ApiModelProperty(value = "开始时间", example = "2025-09-21")
    private String startTime;

    @ApiModelProperty(value = "结束时间", example = "2025-10-21")
    private String endTime;

}
