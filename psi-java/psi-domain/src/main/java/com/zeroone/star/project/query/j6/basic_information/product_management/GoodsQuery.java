package com.zeroone.star.project.query.j6.basic_information.product_management;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 描述：商品查询对象
 * 
 * @author 杨潇
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("商品查询对象")
public class GoodsQuery extends PageQuery {

    @ApiModelProperty(value = "商品名称（模糊查询）", example = "手机")
    private String name;

    @ApiModelProperty(value = "商品编号（模糊查询）", example = "GD")
    private String number;

    @ApiModelProperty(value = "分类ID", example = "cat001")
    private String category;

    @ApiModelProperty(value = "品牌", example = "Apple")
    private String brand;

    @ApiModelProperty(value = "商品类型", example = "1")
    private Integer type;

    @ApiModelProperty(value = "条码", example = "6901234567890")
    private String code;
}
