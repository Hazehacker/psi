package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "首页-数据概览-采购退货单统计响应模型")
public class PurchaseReturnDailyTotalResponseDTO {
    
    @ApiModelProperty(value = "数据类型",example = "采购退货单")
    private String type = "采购退货单";

    @ApiModelProperty(value = "采购退货单统计列表")
    private List<PurchaseReturnDailyTotalDTO> list;

    public PurchaseReturnDailyTotalResponseDTO() {
        this.type = "采购退货单";
    }
    
    public PurchaseReturnDailyTotalResponseDTO(List<PurchaseReturnDailyTotalDTO> list) {
        this.type = "采购退货单";
        this.list = list;
    }
}