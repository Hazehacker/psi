package com.zeroone.star.project.dto.j6.basic_information.product_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 描述：商品选择列表数据传输对象（简化版，用于下拉选择）
 * 
 * @author 杨潇
 * @version 1.0.0
 */
@Data
@ApiModel("商品选择列表数据传输对象")
public class GoodsSelectDTO {

    @ApiModelProperty(value = "商品ID", example = "1a2b3c4d5e6f")
    private String id;

    @ApiModelProperty(value = "商品名称", example = "苹果手机")
    private String name;

    @ApiModelProperty(value = "商品编号", example = "GD001")
    private String number;

    @ApiModelProperty(value = "规格", example = "256G")
    private String spec;

    @ApiModelProperty(value = "单位", example = "台")
    private String unit;

    @ApiModelProperty(value = "销售价", example = "6000.00")
    private BigDecimal sell;

    @ApiModelProperty(value = "库存数量", example = "100.00")
    private BigDecimal stock;

    @ApiModelProperty(value = "分类名称", example = "电子产品")
    private String categoryName;
}
