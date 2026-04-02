package com.zeroone.star.project.dto.j6.basic_information.frame;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 温昊璇
 * @version 1.0
 * 描述：组织结构树
 */
@Data
@ApiModel("组织结构树")
public class FrameDTO {
    @ApiModelProperty(value = "组织结构树ID", required = true)
    @NotBlank(message = "组织结构树ID不能为空")
    private String id;

    @ApiModelProperty(value = "组织结构树父级ID", required = false)
    private String pid;

    @ApiModelProperty(value = "组织结构树名称", required = true)
    @NotBlank(message = "组织结构树名称不能为空")
    private String name;

    @ApiModelProperty(value = "组织结构树排序", required = true)
    @NotBlank(message = "组织结构树排序不能为空")
    private Integer sort;

    @ApiModelProperty(value = "组织结构树备注", required = true)
    private String data;

    private List<FrameDTO> children = new ArrayList<>();
    private Boolean hasChildren;

}
