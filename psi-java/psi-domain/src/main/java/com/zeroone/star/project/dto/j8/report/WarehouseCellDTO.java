package com.zeroone.star.project.dto.j8.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("库存余额-仓库单元")
public class WarehouseCellDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "成本(单价)", example = "12.34")
    private BigDecimal cost;      // 单位成本（可为加权实时成本）

    @ApiModelProperty(value = "数量", example = "100")
    private BigDecimal qty;

    @ApiModelProperty(value = "金额(=成本*数量)", example = "1234.00")
    private BigDecimal amount;
}
