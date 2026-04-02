package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@ApiModel(description = "付款单统计模型")
public class OmyDailyTotalDto {
    /**
     * 统计的日期
     */
    @ApiModelProperty(value = "日期",example = "2025-10-10")
    LocalDate date;
    /**
    当日所有付款单的总金额
     */
    @ApiModelProperty(value = "当日所有付款单总金额", example = "103.2")
    BigDecimal value;
}
