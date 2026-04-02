package com.zeroone.star.project.dto.j6.basic_information.product_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 杨潇, nick8311370879
 * @version 1.0
 * @date:
 */
@Data
@ApiModel(description = "导出商品请求参数")
public class GoodExportDTO {
    @ApiModelProperty(value = "商品ID列表", required = true)
    private List<String> ids;
}
