package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@ApiModel("首页-数据概览-销售退货单统计模型")
public class SreCountsDTO {
    /**
     * 统计展示的日期
     */
    @ApiModelProperty(value = "日期",example = "2025-10-18")
    LocalDate date;
    /**
     每日所有销售退货的总金额
     */
    @ApiModelProperty(value = "每日所有销售退货的总金额",example = "1919810.00000")
    BigDecimal value;
}