package com.zeroone.star.project.vo.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 描述：登录显示数据对象
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@ApiModel("登录显示对象")
@Data
public class LoginVO {
    @ApiModelProperty(value = "用户唯一编号", example = "1")
    private String id;

    @ApiModelProperty(value = "用户名", example = "admin")
    private String username;

    @ApiModelProperty(value = "用户角色列表", example = "['ADMIN','MANAGER']")
    private List<String> roles;

    @ApiModelProperty(value = "用户头像",example = "https://img95.699pic.com/photo/60112/3125.jpg_wh860.jpg")
    private String avatar;
}
