package com.zeroone.star.project.dto.j7.sysargs.supportinfo.often;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 保存用户常用功能的请求
 */
@Data
@ApiModel("保存用户常用功能的请求数据对象")
public class SaveOftenReqDTO {
    @NotBlank(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", example = "1001", required = true)
    private String userId;


    @ApiModelProperty(value = "选中的菜单 key 列表", example = "[\"home\",\"base\"]", required = true)
    private List<String> menuKeys;
}