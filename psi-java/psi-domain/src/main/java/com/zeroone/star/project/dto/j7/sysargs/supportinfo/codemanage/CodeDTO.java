package com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("条码对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeDTO {


    @NotBlank(message = "条码名称不能为空")
    @ApiModelProperty(value = "条码名称",example = "条码名称",required = true)
    private String name;

    @NotBlank(message = "条码内容不能为空")
    @ApiModelProperty(value = "条码内容",example = "条码内容",required = true)
    private String info;

    @NotNull(message = "条码类型不能为空")
    @ApiModelProperty(value = "条码类型 [0:条形码 | 1:二维码]",example = "1",required = true)
    private Integer type;
    @ApiModelProperty(value = "备注信息",example = "备注")
    private String data;
}
