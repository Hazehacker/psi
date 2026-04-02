package com.zeroone.star.project.dto.j2.store;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("AttrStockDTO")
public class AttrStockDTO {

    @ApiModelProperty(value = "商品辅助属性ID（内部使用）", example = "1")
    private String attrId;

    @ApiModelProperty(value = "商品属性", example = "8G+128G")
    private String attrName;

    @ApiModelProperty(value = "所属商品ID")
    @JsonIgnore
    private String pid;

    @ApiModelProperty(value = "库存数量", example = "200")
    private BigDecimal totalStock;

    @ApiModelProperty(value = "辅助属性的仓库库存明细")
    private List<WarehouseStockDTO> warehouses;
}
