package com.zeroone.star.project.query.j5.salesreport;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 销售收款表
 * @author yu, leyu(接手)
 * @date 2025/10/21
 */
@Data
@ApiModel("销售收款表查询参数")
public class SalesReceiptQuery extends PageQuery {

    @ApiModelProperty(value = "客户", example = "A公司")
    private String customer;
    @ApiModelProperty(value = "单据编号", example = "100001")
    private String number;
    @ApiModelProperty(value = "单据开始时间", example = "2025-10-01")
    private String begintime;
    @ApiModelProperty(value = "单据结束时间", example = "2025-10-31")
    private String endtime;
    @ApiModelProperty(value = "核销状态",example = "1",allowableValues = "0,1,2")
    private Integer nucleus;
    @ApiModelProperty(value = "单据类型",example = "销售单")
    private String type;
}
