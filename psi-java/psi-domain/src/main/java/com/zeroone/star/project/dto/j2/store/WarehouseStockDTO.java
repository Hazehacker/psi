package com.zeroone.star.project.dto.j2.store;


import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("WarehouseStockDTO")
public class WarehouseStockDTO {

    @ApiModelProperty(value = "所属存储ID", example = "1")
    @JsonIgnore
    private String storedRecordId;

    @ApiModelProperty(value = "所属仓库ID（内部使用）", example = "1")
    private String warehouseId;

    @ExcelProperty(value = "仓库名称",index = 2)
    @ApiModelProperty(value = "所属仓库名称", example = "上海仓库")
    private String warehouseName;

    @ApiModelProperty(value = "所属商品ID（内部使用）", example = "1")
    @JsonIgnore
    private String goodsId;

    @ApiModelProperty(value = "商品辅助属性ID（内部使用）", example = "1")
    @JsonIgnore
    private String attrId;

    @ApiModelProperty(value = "库存数量", example = "100")
    private BigDecimal stockNum;

}
