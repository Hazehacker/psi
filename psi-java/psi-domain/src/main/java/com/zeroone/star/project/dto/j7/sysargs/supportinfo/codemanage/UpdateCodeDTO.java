package com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 类名：UpdateCodeDTO
 * 包名：com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/31
 * 版本号：V1.0
 */
@Data
@ApiModel("更新条码数据对象")
public class UpdateCodeDTO extends CodeDTO{

    @NotBlank(message = "id不能为空")
    @ApiModelProperty(value = "条码id",example = "1",required = true)
    String id;
}
