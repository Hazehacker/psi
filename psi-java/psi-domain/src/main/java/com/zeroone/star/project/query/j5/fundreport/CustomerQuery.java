package com.zeroone.star.project.query.j5.fundreport;

import com.zeroone.star.project.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询客户名")
public class CustomerQuery {

    @ApiModelProperty(value = "客户名列表")
    private PageDTO<String> name;
}
