package com.zeroone.star.project.dto.j2.store;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("CategoryDataDTO")
public class CategoryDataDTO {

    @ExcelProperty(value = "商品类别id", index = 0)
    @ApiModelProperty(value = "商品类别id", example = "1")
    private String id;

    @ExcelProperty(value = "类别名称", index = 1)
    @ApiModelProperty(value = "类别名称", example = "默认类别")
    private String name;

}
