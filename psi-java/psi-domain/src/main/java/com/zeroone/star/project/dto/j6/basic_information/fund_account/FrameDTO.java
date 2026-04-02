package com.zeroone.star.project.dto.j6.basic_information.fund_account;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 描述: 组织结构树
 * </p>
 *
 * @author goldenHour
 * @version 1.0.0
 */

@Data
@ApiModel(description = "组织结构树")
public class FrameDTO {

    @NotEmpty(message = "ID不能为空")
    @ApiModelProperty(value = "组织ID",required = true)
    private String id;

    @ApiModelProperty("所属ID")
    private String pid;

    @ApiModelProperty("组织名称")
    private String name;

    @ApiModelProperty("组织排序")
    private Integer sort;

    @ApiModelProperty("备注信息")
    private String data;
}
