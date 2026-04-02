package com.zeroone.star.project.vo.j6.basic_information.product_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 描述：商品分类树形结构视图对象
 * 
 * @author 杨潇
 * @version 1.0.0
 */
@Data
@ApiModel("商品分类树形结构视图对象")
public class CategoryTreeVO {

    @ApiModelProperty(value = "分类ID", example = "cat001")
    private String id;

    @ApiModelProperty(value = "分类名称", example = "电子产品")
    private String name;

    @ApiModelProperty(value = "父分类ID", example = "0")
    private String pid;

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;

    @ApiModelProperty(value = "子分类列表")
    private List<CategoryTreeVO> children;
}
