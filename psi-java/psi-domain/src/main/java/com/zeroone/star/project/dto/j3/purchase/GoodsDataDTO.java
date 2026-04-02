package com.zeroone.star.project.dto.j3.purchase;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
@ApiModel(description = "商品基础信息（数据库表goods字段）")
@TableName("`goods`")
public class GoodsDataDTO {
    @ApiModelProperty(value = "商品ID", example = "3")
    private String id;
    @ApiModelProperty(value = "商品名称", example = "刀具")
    private String name;
    @ApiModelProperty(value = "商品拼音缩写", example = "dj")
    private String py;
    @ApiModelProperty(value = "商品编号", example = "0003")
    private String number;
    @ApiModelProperty(value = "商品规格", example = "DJ-0001")
    private String spec;
    @ApiModelProperty(value = "商品分类ID", example = "7")
    private String category;
    @ApiModelProperty(value = "商品品牌", example = "飞虎")
    private String brand;
    @ApiModelProperty(value = "商品单位", example = "个")
    private String unit;
    @ApiModelProperty(value = "标准采购价", example = "2")
    private BigDecimal buy;
    @ApiModelProperty(value = "标准销售价", example = "5")
    private BigDecimal sell;
    @ApiModelProperty(value = "商品编码", example = "")
    private String code;
    @ApiModelProperty(value = "商品位置", example = "")
    private String location;
    @ApiModelProperty(value = "当前库存数量", example = "30")
    private BigDecimal stock;
    @ApiModelProperty(value = "商品类型", example = "0")
    private Integer type;
    @ApiModelProperty(value = "商品备注", example = "")
    private String data;
    @ApiModelProperty(value = "商品图片（JSON数组字符串）", example = "[]")
    private String imgs;
    @ApiModelProperty(value = "商品详情", example = "")
    private String details;
    @ApiModelProperty(value = "商品单位列表（JSON数组字符串）", example = "[]")
    private String units;
    @ApiModelProperty(value = "商品策略（JSON数组字符串）", example = "[]")
    private String strategy;
    @ApiModelProperty(value = "是否需要序列号", example = "false")
    private Boolean serial;
    @ApiModelProperty(value = "是否需要批次管理", example = "true")
    private Boolean batch;
    @ApiModelProperty(value = "商品有效性", example = "true")
    private Boolean validity;
    @ApiModelProperty(value = "商品保质期（天）", example = "100")
    private Integer protect;
    @ApiModelProperty(value = "库存预警阈值", example = "60")
    private Integer threshold;
    @ApiModelProperty(value = "扩展信息（JSON字符串）", example = "{}")
    private String more;
}