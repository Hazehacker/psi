package com.zeroone.star.project.dto.j6.basic_information.supplier_management;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("供应商导入DTO")
public class SupplierImportDTO extends SupplierAddDTO{

        @NotNull(message = "供应商ID不能为空")
        @ExcelProperty("供应商ID")
        @ApiModelProperty(value = "id", required = true)
        private Integer id;


}
