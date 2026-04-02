package com.zeroone.star.project.query.j8.report;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel(description = "收发明细报表查询参数")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDetailReportQuery extends PageQuery {
    @ApiModelProperty(value = "单据类型", example = "sell,buy", required = false)
    private List<String> mold;          // 单据类型

    @ApiModelProperty(value = "开始时间", example = "2023-01-01", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime; // 开始时间

    @ApiModelProperty(value = "结束时间", example = "2023-12-31", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;   // 结束时间

    @ApiModelProperty(value = "商品", example = "iPhone 15", required = false)
    private String goods;         // 商品

    @ApiModelProperty(value = "仓库ID", example = "北京仓", required = false)
    private List<String> warehouse;     // 仓库
}
