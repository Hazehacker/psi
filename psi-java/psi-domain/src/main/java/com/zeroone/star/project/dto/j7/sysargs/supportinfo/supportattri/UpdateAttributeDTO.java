package com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 辅助属性详情对象
 *
 * @author ailudick
 * @data 2025/10/21
 */

@Data
@ApiModel("辅助属性详情对象")
public class UpdateAttributeDTO  {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty(value = "ID", example = "1",required = true)
    private String id;
    @ApiModelProperty(value = "属性名称", example = "颜色",required = true)
    @NotBlank(message = "属性名不能为空")
    private String name;
    @ApiModelProperty(value = "属性排序", example = "1",required = true)
    @NotNull(message = "属性排序不能为空")
    private Integer sort;
    @ApiModelProperty(value = "备注属性", example = "服饰颜色")
    private String data;
    @ApiModelProperty(value = "属性内容", example = "[\n" +
            "    {\n" +
            "      \"name\": \"经典黑\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"经典白\"\n" +
            "    }\n" +
            "  ]" ,required = true)
    @NotEmpty(message =  "属性内容不能为空")
    List<AttributeInfoDTO> content;
}
