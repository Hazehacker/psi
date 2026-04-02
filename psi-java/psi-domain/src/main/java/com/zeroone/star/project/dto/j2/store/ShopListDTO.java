package com.zeroone.star.project.dto.j2.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/*此dto的所有字段都可以在goods表中找到*/
@Data
@ApiModel("ShopListDTO")
public class ShopListDTO {
    @ApiModelProperty(value = "隐藏属性唯一id",required = true , example = "1")
    private String id;
    @ApiModelProperty(value = "商品名称",required = true, example = "刀具")
    private String name;
    @ApiModelProperty(value = "商品编号",required = true, example = "0003")
    private String number;
    @ApiModelProperty(value = "规格标号", example = "DJ-0001")
    private String spec;
    @ApiModelProperty(value = "商品分类",required = true, example = "五金工具")
    private String category;
    @ApiModelProperty(value = "商品品牌", example = "飞虎")
    private String brand;
    @ApiModelProperty(value = "商品单位",required = true, example = "个")
    private String unit;
    @ApiModelProperty(value = "采购价格",required = true, example = "2")
    private BigDecimal buy;
    @ApiModelProperty(value = "销售价格",required = true, example = "5")
    private BigDecimal sell;
    @ApiModelProperty(value = "商品条码", example = "")
    private String code;
    @ApiModelProperty(value = "备注信息", example = "null")
    private String data;
}
