package com.zeroone.star.project.vo.j6.basic_information.supplier_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("导入失败详情")
public class ImportFailureVO {

    @ApiModelProperty("行号")
    private Integer rowNumber;

    @ApiModelProperty("失败数据")
    private String data;

    @ApiModelProperty("失败原因")
    private String reason;
}
