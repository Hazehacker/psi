package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel("汇总信息模型")
public class SummaryDTO {
    @ApiModelProperty(value = "类别名称", example = "商品总数")
    private String label;
    @ApiModelProperty(value = "值", example = "248")
    private Integer value;
    @ApiModelProperty(value = "路由", example = "/goods")
    private String to;
}
