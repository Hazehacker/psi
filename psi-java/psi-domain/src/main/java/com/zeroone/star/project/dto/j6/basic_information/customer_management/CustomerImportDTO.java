package com.zeroone.star.project.dto.j6.basic_information.customer_management;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CustomerImportDTO extends CustomerImportAddDTO{

    @NotBlank(message = "客户ID不能为空")
    @ExcelProperty("供应商ID")
    @ApiModelProperty(value = "id", required = true)
    private Integer id;
}
