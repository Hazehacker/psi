package com.zeroone.star.project.dto.j1.sysconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("字典类型基础数据传输对象")
public class DictTypeDTO {
    /**
     * 字典类型mingc
     */
    @NotBlank(message = "字典类型名称不能为空")
    @ApiModelProperty(value = "字典类型名称",required = true, example = "文件类型")
    private String name;
    /**
     * 字典类型编码
     */
    @NotBlank(message = "字典类型名称不能为空")
    @ApiModelProperty(value = "字典类型编码", required = true, example = "file_type")
    private String code;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注信息", example = "用于管理系统中的文件类型字典")
    private String remark;
    @ApiModelProperty(value = "主键ID", example = "61f3bbc90b904b51890448968afc106f")
    private String id;
}
