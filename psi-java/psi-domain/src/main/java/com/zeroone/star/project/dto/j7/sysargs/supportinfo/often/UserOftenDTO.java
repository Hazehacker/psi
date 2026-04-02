package com.zeroone.star.project.dto.j7.sysargs.supportinfo.often;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 用户已设置的常用功能
 */
@Data
@ApiModel("用户已设置的常用功能数据对象")
public class UserOftenDTO {
    @NotBlank(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", example = "1001")
    private String userId;

    @Valid
    @ApiModelProperty(value = "用户标记的常用功能菜单列表", example = "[{\"id\":1,\"name\":\"系统参数\"}]")
    private List<MenuDTO> menus;
}