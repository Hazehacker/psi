package com.zeroone.star.project.dto.j7.sysargs.supportinfo.often;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 菜单列表数据对象
 */
@Data
@ApiModel("菜单列表数据对象")
public class MenuDTO {
    @ApiModelProperty(value = "菜单ID", example = "1")
    @Min(value = 0, message = "菜单ID不能小于0")
    private String id;

    @ApiModelProperty(value = "父级菜单ID", example = "0")
    @NotNull(message = "父级菜单ID不能为空")
    @Min(value = 0, message = "父级菜单ID不能小于0")
    private Integer pid;

    @ApiModelProperty(value = "菜单名称", example = "系统参数")
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    private String name;

    @ApiModelProperty(value = "菜单标识", example = "system")
    @NotBlank(message = "菜单标识不能为空")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "菜单标识只能包含小写字母、数字和下划线，且必须以字母开头")
    private String key;

    @ApiModelProperty(value = "资源路径", example = "/system/menu")
    @Pattern(regexp = "^(/[^\\s]+)?$", message = "资源路径必须以 '/' 开头，且不能包含空格")
    private String resource;

    @ApiModelProperty(value = "图标", example = "el-icon-menu")
    @Pattern(regexp = "^el-icon-[a-zA-Z]+(\\-[a-zA-Z]+)*$", message = "图标格式应为 el-icon-xxx")
    private String ico;

    @ApiModelProperty(value = "所属分组", example = "admin")
    @Pattern(regexp = "^[a-zA-Z]+(\\.[a-zA-Z]+)*$", message = "分组名只能包含字母和点号")
    private String root;

    @ApiModelProperty(value = "额外描述", example = "系统设置组")
    @Size(max = 100, message = "额外描述长度不能超过100个字符")
    private String data;
}