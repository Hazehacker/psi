package com.zeroone.star.project.dto.j1.sysconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 描述：字典传输数据对象
 * </p>
 * @author fish
 * @version 1.0.0
 */
@Data
@ApiModel("字典传输数据对象")
public class DictDTO {

    @ApiModelProperty(value = "字典类型名称", example = "文件存储方式", required = true)
    @NotBlank(message = "字典类型名称不能为空")
    String type_name;

    @ApiModelProperty(value = "字典类型唯一标识", example = "f42afd7d846248528fe72d62c01e9d1e", required = true)
    @NotBlank(message = "字典类型唯一标识不能为空")
    String tid;

    @ApiModelProperty(value = "字典名称", example = "fastdfs", required = true)
    @NotBlank(message = "字典名称不能为空")
    String name;

    @ApiModelProperty(value = "字典值", example = "10001", required = true)
    @NotBlank(message = "值不能为空")
    String value;

    @ApiModelProperty(value = "描述", example = " ")
    String remark;

    @ApiModelProperty(value = "唯一标识", example = "07cd9e35e50746c4817523c8d4cc046a", required = true)
    @NotBlank(message = "编号不能为空")
    String id;

}
