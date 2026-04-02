package com.zeroone.star.project.query.j8.report;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel("商品查询参数")
public class StockSummaryReportQuery extends PageQuery {
    @ApiModelProperty(value = "商品名称", example = "华为Mate60")
    private String goods;

    @ApiModelProperty(value = "查询的仓库ID列表", example = "[\"W001\",\"W002\"]")
    private List<String> warehouse;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "查询范围时间-早", example = "2020-10-21")
    private LocalDate startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "查询范围时间-晚", example = "2025-10-22")
    private LocalDate endTime;
}
