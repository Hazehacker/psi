package com.zeroone.star.project.dto.j6.basic_information.customer_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 描述：组织数据传输对象DTO
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@Data
@ApiModel("获取组织树")
public class queryOrganizationTreeDto {
    @ApiModelProperty(value = "组织编号",example = "0", required= true)
    private int id;
    @ApiModelProperty(value = "组织名称",example = "默认组织",required = true)
    private String name;
}
