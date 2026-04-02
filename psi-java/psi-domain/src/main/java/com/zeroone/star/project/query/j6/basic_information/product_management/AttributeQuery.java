package com.zeroone.star.project.query.j6.basic_information.product_management;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 描述：商品属性查询对象
 * 
 * @author 杨潇
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("商品属性查询对象")
public class AttributeQuery extends PageQuery {

    @ApiModelProperty(value = "属性名称（模糊查询）", example = "颜色")
    private String name;
}
