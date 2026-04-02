package com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 描述：辅助属性列表数据对象
 * </p>
 *
 * @author yi
 * @version 1.0.0
 */
@Data
@ApiModel("辅助属性列表数据")
public class AttributeDTO {
    @ApiModelProperty(value = "ID", example = "1")
    private String id;
    @ApiModelProperty(value = "属性名称", example = "颜色")
    private String name;
    @ApiModelProperty(value = "属性排序", example = "1")
    private Integer sort;
    @ApiModelProperty(value = "备注属性", example = "服饰颜色")
    private String data;
}
