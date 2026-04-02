package com.zeroone.star.project.query.j5.fundreport;


import com.zeroone.star.project.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询供应商名字")
public class SupplierQuery {

    @ApiModelProperty(value = "供应商名列表")
    private PageDTO<String> supplier;
}
