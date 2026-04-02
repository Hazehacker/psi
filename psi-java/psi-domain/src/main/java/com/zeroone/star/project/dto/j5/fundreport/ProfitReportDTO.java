package com.zeroone.star.project.dto.j5.fundreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 利润表信息的每行数据
 * 固定的一页数据无需分页查询参数
 */
@Data
@ApiModel("利润表信息的每行数据")
@AllArgsConstructor
public class ProfitReportDTO {
    @ApiModelProperty(value = "项目名称", example = "主营业务")
    private String projectName;
    @ApiModelProperty(value = "金额（字符串格式，可能为空）", example = "762909122.23")
    private String amount;
}
