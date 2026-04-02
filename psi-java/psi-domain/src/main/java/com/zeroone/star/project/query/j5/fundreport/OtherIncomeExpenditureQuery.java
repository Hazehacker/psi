package com.zeroone.star.project.query.j5.fundreport;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 其他收支明细查询参数
 */
@ApiModel("其他收支明细查询参数")
@Data
public class OtherIncomeExpenditureQuery extends PageQuery {

    @ApiModelProperty(value = "单据类型", example = "其他收入单")
    private String documentType;

    @ApiModelProperty(value = "单据编号", example = "QTSRD250924171")
    private String documentNumber;

    @ApiModelProperty(value = "开始时间", example = "2025-09-21")
    private String startTime;

    @ApiModelProperty(value = "结束时间", example = "2025-10-21")
    private String endTime;

    @ApiModelProperty(value = "收支类别", example = "销售收入")
    private String category;

    @ApiModelProperty(value = "结算账户", example = "默认用户")
    private String settlementAccount;

    @ApiModelProperty(value = "备注信息", example = "备注信息")
    private String remark;
}
