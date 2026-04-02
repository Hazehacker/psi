package com.zeroone.star.project.query.j5.salesreport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ApiModel("销售汇总表查询参数")
public class SalesSummaryQuery extends PageQuery {
    @ApiModelProperty(value = "商品名称",example = "小刀")
    private String productName;

    @ApiModelProperty(value = "仓库id",example = "1")
    private Integer warehouseId;

    @ApiModelProperty(value = "客户id",example = "1")
    private String customerId;

    @ApiModelProperty(value = "用户id",example = "2")
    private String userId;

    @ApiModelProperty(value = "关联人员",example = "3")
    private String peopleId;

    @ApiModelProperty(value = "开始时间",example = "2025-10-21")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束时间",example = "2025-10-25")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty(value = "分组维度：product-商品，customer-客户，user-用户，people-人员",example = "product",required = true)
    private String groupBy;
}
