package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 描述：封装数据统计返回值
 * </p>
 *
 * @author heavydrink
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("数据统计")
public class DataStatisticsDTO {
    DailySales dailySales;
    DailyGross dailyGross;
    DailyCount dailyCount;
    DailyIncome dailyIncome;
}
