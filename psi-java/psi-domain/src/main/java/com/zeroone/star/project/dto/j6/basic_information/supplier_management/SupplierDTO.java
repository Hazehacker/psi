package com.zeroone.star.project.dto.j6.basic_information.supplier_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 趣味生煎
 * @version 1.0
 * 描述：供应商
 */
@Data
@ApiModel("供应商")
public class SupplierDTO extends SupplierAddDTO{

    @NotNull(message = "供应商ID不能为空")
    @ApiModelProperty(value = "id", required = true)
    private Integer id;
}
