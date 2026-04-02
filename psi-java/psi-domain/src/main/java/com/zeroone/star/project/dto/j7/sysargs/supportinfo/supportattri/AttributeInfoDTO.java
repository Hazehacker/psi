package com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 描述：属性内容对象
 * </p>
 *
 * @author yi
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("属性内容")
public class AttributeInfoDTO {
    @ApiModelProperty(value = "属性名称", example = "纯白色")
    private String name;

}
