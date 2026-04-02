package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("总计汇总信息模型")
public class TotalSummaryDTO {

    @ApiModelProperty(value = "商品总数", example = "78")
    private Integer productCount;

    @ApiModelProperty(value = "客户总数", example = "19")
    private Integer customerCount;

    @ApiModelProperty(value = "供应商总数", example = "7")
    private Integer supplierCount;

    @ApiModelProperty(value = "库存总数", example = "104074.5")
    private Double stockTotal;

    @ApiModelProperty(value = "库存预警", example = "128")
    private Integer stockWarningCount;

    @ApiModelProperty(value = "保质期预警", example = "25")
    private Integer expiryWarningCount;
}