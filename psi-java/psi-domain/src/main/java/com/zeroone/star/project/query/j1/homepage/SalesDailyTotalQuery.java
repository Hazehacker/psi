package com.zeroone.star.project.query.j1.homepage;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@ApiModel(description = "首页-数据概览-销售单查询query")
public class SalesDailyTotalQuery {

    @ApiModelProperty(required = true,value = "具体类型",example = "销售单")
    @NotNull(message = "类型不能为空")
    private String type;

    @ApiModelProperty(value = "从哪一天开始统计 传入string就行", example = "2025-10-08")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(value = "统计到哪一天 传入string就行", example = "2025-10-13")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
