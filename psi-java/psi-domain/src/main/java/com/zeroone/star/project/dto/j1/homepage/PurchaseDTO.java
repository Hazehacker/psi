package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel("采购单统计表")
public class PurchaseDTO {
    @ApiModelProperty(value = "类型",example = "采购单")
    private String type = "采购单";

    private List<DV> list;
}
