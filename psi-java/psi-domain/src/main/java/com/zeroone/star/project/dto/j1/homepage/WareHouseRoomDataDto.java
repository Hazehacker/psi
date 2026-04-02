package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(description = "库存数据的统计信息项")
public class WareHouseRoomDataDto {
    /**
    仓库名
     */
    @ApiModelProperty(value = "仓库名",example = "1号仓")
    String name;
    /**
    仓库所有商品库存数量总和
     */
    @ApiModelProperty(value = "该仓库所有商品库存数量总和",example = "2000")
    Long value;
}
