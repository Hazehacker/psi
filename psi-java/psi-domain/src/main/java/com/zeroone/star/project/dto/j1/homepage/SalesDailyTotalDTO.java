package com.zeroone.star.project.dto.j1.homepage;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 描述：封装数据概览 销售单 返回值
 * </p>
 *
 * @author Altolia
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@ApiModel(description = "首页-数据概览-销售单统计模型")
public class SalesDailyTotalDTO {
    /**
     * 统计的日期
     */
    @ApiModelProperty(value = "日期",example = "2025-09-24")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    /**
    当日所有销售单单的总金额
     */
    @ApiModelProperty(value = "当日所有销售单总金额", example = "1000.00")
    private BigDecimal value;
}