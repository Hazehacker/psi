package com.zeroone.star.project.dto.j6.basic_information.product_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 描述：商品数据传输对象
 * 
 * @author 杨潇
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("商品数据传输对象")
public class GoodsDTO extends GoodsAddDTO {

    @ApiModelProperty(value = "商品ID", example = "1a2b3c4d5e6f", required = true)
    @NotBlank(message = "商品ID不能为空")
    private String id;


    @ApiModelProperty(value = "分类名称", example = "电子产品")
    private String categoryName;
}
