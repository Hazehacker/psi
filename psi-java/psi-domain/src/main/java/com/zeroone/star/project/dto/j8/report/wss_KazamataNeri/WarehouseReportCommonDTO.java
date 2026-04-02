package com.zeroone.star.project.dto.j8.report.wss_KazamataNeri;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
@ApiModel("商品收发汇总表通用封装数据")
public class WarehouseReportCommonDTO {

    @ApiModelProperty(value = "成本", example = "1.0000")
    private BigDecimal price;

    @ApiModelProperty(value = "数量", example = "12.0000")
    private BigDecimal nums;

    @ApiModelProperty(value = "总成本", example = "13.0000")
    private BigDecimal total;
}
