package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "首页-数据概览-销售单统计响应模型")
public class SalesDailyTotalResponseDTO {
    
    @ApiModelProperty(value = "数据类型",example = "销售单")
    private String type = "销售单";

    @ApiModelProperty(value = "销售单单统计列表")
    private List<SalesDailyTotalDTO> list;

    public SalesDailyTotalResponseDTO() {
        this.type = "销售单";
    }
    
    public SalesDailyTotalResponseDTO(List<SalesDailyTotalDTO> list) {
        this.type = "销售单";
        this.list = list;
    }
}