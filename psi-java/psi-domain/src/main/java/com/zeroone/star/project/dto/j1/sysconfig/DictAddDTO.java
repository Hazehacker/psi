package com.zeroone.star.project.dto.j1.sysconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * <p>
 * 描述：字典增加数据对象
 * </p>
 * @author zhao0527
 * @version 1.0.0
 */
@Data
@ApiModel("字典增加数据对象")
public class DictAddDTO {
    @ApiModelProperty(value = "所属字典类型", example = "f42afd7d846248528fe72d62c01e9d1e", required = true)
    @NotBlank(message = "所属字典类型不能为空")
    String tid;

    @ApiModelProperty(value = "字典名称", example = "fastdfs", required = true)
    @NotBlank(message = "字典名称不能为空")
    String name;

    @ApiModelProperty(value = "字典值", example = "10001", required = true)
    @NotBlank(message = "值不能为空")
    String value;

    @ApiModelProperty(value = "描述", example = " ")
    String remark;



}
