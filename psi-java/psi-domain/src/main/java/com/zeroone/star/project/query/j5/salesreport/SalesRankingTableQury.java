package com.zeroone.star.project.query.j5.salesreport;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("销售排行表")
public class SalesRankingTableQury extends PageQuery {

    @ApiModelProperty(value = "笔记本")
    private String productName;

    @ApiModelProperty(value = "商品编号",example = "%G2025001%")
    private String productNumber;

    @ApiModelProperty(value = "开始时间", example = "2025-10-20")
    private String begintime;
    @ApiModelProperty(value = "结束时间", example = "2025-10-21")
    private String endtime;
}
