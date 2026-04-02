package com.zeroone.star.project.dto.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * <p>
 * 描述：常用功能
 * </p>
 *
 * @author heavydrink
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("常用功能")
public class CommonMenuDTO {
    @ApiModelProperty(value = "功能名称", example = "首页")
    String label;
    @ApiModelProperty(value = "菜单地址", example = "/system/customer")
    String to;
}
