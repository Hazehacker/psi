package com.zeroone.star.project.query.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@ApiModel(description = "首页-数据概览-采购单查询query")
public class PurchaseDailyTotalQuery {
    //TODO: 是不是要加type

    @ApiModelProperty(required = true, value = "从哪一天开始统计", example = "2025-10-08")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @ApiModelProperty(required = true, value = "统计到哪一天", example = "2025-10-15")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;
}