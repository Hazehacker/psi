package com.zeroone.star.project.query.j5.fundreport;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@ApiModel("查询客户对账表参数")
public class CustomerStatementQuery extends PageQuery {

    @ApiModelProperty(value = "客户名",example = "CUST001")
    private String name;

    @ApiModelProperty(value = "隐藏/显示明细",example = "true")
    private boolean hideOrShowDetail;

    @DateTimeFormat(pattern = "yyyy-M-d")
    @ApiModelProperty(value = "开始时间",example = "2023-10-01")
    private LocalDate beginTime;

    @DateTimeFormat(pattern = "yyyy-M-d")
    @ApiModelProperty(value = "结束时间",example = "2025-10-10")
    private LocalDate endTime;

//    @ApiModelProperty(value = "页码",example = "1")
//    private Integer pageIndex;
//
//    @ApiModelProperty(value = "每页条数",example = "10")
//    private Integer pageSize;
}
