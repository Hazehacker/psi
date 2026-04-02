package com.zeroone.star.project.dto.j8.report.wss_KazamataNeri;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询过程传递参数用")
public class IntermediateParameterDTO {
    @ApiModelProperty(value = "仓库参数id")
    private String warehouseNumber;

    @ApiModelProperty(value = "商品编号(商品对应各种表的中间参数)")
    private String goodsNumber;
}
