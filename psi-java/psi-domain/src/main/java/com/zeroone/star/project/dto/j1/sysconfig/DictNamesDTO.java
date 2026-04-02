package com.zeroone.star.project.dto.j1.sysconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 描述：字典名称数据对象
 * </p>
 * @author fish
 * @version 1.0.0
 */
@Data
@ApiModel("字典名称数据对象")
public class DictNamesDTO {

    @ApiModelProperty(value = "唯一标识", example = "07cd9e35e50746c4817523c8d4cc046a", required = true)
    @NotBlank(message = "编号不能为空")
    String id;

    @ApiModelProperty(value = "字典值", example = "10001", required = true)
    @NotBlank(message = "值不能为空")
    String value;

    @ApiModelProperty(value = "字典名称", example = "fastdfs", required = true)
    @NotBlank(message = "字典名称不能为空")
    String name;

}
