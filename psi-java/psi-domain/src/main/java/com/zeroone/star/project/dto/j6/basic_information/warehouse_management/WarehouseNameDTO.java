package com.zeroone.star.project.dto.j6.basic_information.warehouse_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 温昊璇
 * @version 1.0
 * 描述：仓库
 */
@Data
@ApiModel("仓库")
public class WarehouseNameDTO {
    @ApiModelProperty(value = "仓库ID", required = true)
    @NotBlank(message = "仓库ID不能为空")
    private String id;
    @ApiModelProperty(value = "仓库名称", required = true)
    @NotBlank(message = "仓库名称不能为空")
    private String name;
}
