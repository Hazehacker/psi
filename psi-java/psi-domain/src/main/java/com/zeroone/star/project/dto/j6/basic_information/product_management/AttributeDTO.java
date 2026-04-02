package com.zeroone.star.project.dto.j6.basic_information.product_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 描述：商品属性数据传输对象（包含属性值列表）
 * 
 * @author 杨潇
 * @version 1.0.0
 */
@Data
@ApiModel("商品属性数据传输对象")
public class AttributeDTO {

    @ApiModelProperty(value = "属性ID", example = "attr001")
    private String id;

    @ApiModelProperty(value = "属性名称", example = "颜色")
    private String name;

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;

    @ApiModelProperty(value = "属性值列表")
    private List<AttributeValueDTO> values;

    /**
     * 属性值内部类
     */
    @Data
    @ApiModel("属性值数据传输对象")
    public static class AttributeValueDTO {

        @ApiModelProperty(value = "属性值ID", example = "val001")
        private String id;

        @ApiModelProperty(value = "属性值名称", example = "红色")
        private String name;
    }
}
