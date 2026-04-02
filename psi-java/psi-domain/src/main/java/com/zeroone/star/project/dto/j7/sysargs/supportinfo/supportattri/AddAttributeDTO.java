package com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 添加辅助属性数据对象
 *
 * @author ailudick
 * @data 2025/10/21
 */
@Data
@ApiModel("添加辅助属性参数")
public class AddAttributeDTO {

    @ApiModelProperty(value="属性",example="颜色")
    @NotBlank(message = "属性名不能为空")
    private  String name;
    @ApiModelProperty(value="属性排序",example="1")
    @NotNull(message = "属性排序不能为空")
    private  Integer sort;
    @ApiModelProperty(value="备注属性",example="服饰颜色")
    private  String data;
    @ApiModelProperty(value = "属性内容", required = true, example = "[\n" +
            "    {\n" +
            "      \"name\": \"经典黑\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"经典白\"\n" +
            "    }\n" +
            "  ]")
    @NotEmpty(message =  "属性内容不能为空")
    private List<AttributeInfoDTO> content;
}
