package com.zeroone.star.project.query.j1.sysconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 描述：字典类型名称查询数据对象
 * </p>
 */
@Data
@ApiModel("字典名称查询对象")
public class DictNameQuery {
    @NotBlank
    @ApiParam(value = "字典类型编码", required = true, example = "save_way")
    String typeCode;
}
