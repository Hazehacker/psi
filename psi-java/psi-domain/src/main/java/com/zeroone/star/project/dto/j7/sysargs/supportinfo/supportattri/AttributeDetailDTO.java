package com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 描述: 辅助属性详情数据对象
 * </p>
 * @author yi
 * @version 1.0.0
 */
@Data
@ApiModel("辅助属性详情数据")
public class AttributeDetailDTO extends AttributeDTO {
    @ApiModelProperty(value = "属性内容", example = "[\n" +
            "    {\n" +
            "      \"name\": \"经典黑\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"经典白\"\n" +
            "    }\n" +
            "  ]")
    List<AttributeInfoDTO> content;
}
