package com.zeroone.star.project.query.j5.salesreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("销售排行表查询参数（分页必填）")
public class NoRequiredSalesRankingTableQuery extends SalesRankingTableQury {

    @Override
    @ApiModelProperty(value = "查询页码", example = "1", required = false)
    public long getPageIndex() {
        return super.getPageIndex();
    }

    @Override
    @ApiModelProperty(value = "查询条数", example = "10", required = false)
    public long getPageSize() {
        return super.getPageSize();
    }
}
