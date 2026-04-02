package com.zeroone.star.project.dto.j1.sysconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DictUpdateDTO extends DictAddDTO{

    @ApiModelProperty(value = "唯一标识", example = "07cd9e35e50746c4817523c8d4cc046a", required = true)
    @NotBlank(message = "编号不能为空")
    String id;
}
