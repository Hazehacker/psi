package com.zeroone.star.project.dto.j1.sysconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 描述：删除目录数据对象
 * </p>
 * @author hui_cheng12
 * @version 1.0.0
 */
@Data
@ApiModel("目录类型删除id")
public class MenuDeleteDTO {
    /**
     * 目录id
     */
    @ApiModelProperty(value = "目录id",example = "70", required = true)
    @NotBlank(message = "未识别到当前目录id")
    private Integer id;
}
