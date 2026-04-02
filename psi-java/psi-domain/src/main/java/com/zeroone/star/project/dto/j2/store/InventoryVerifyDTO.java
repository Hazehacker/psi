package com.zeroone.star.project.dto.j2.store;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@ApiModel("InventoryVerifyDTO")
public class InventoryVerifyDTO  implements Serializable {


    @ApiModelProperty(value = "商品id", example = "g001")
    private String goodId;

    @ApiModelProperty(value = "商品属性Id", example = "1")
    private String attrId;

    @ApiModelProperty(value = "仓库Id", example = "1号仓库")
    private String warehouseId;

    @ExcelProperty(value = "盘盈盘亏", index = 4)
    @ApiModelProperty(value = "盘盈盘亏", example = "120")
    private BigDecimal InventoryDifference;
}
