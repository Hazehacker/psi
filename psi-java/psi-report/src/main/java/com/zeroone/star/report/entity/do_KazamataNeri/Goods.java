package com.zeroone.star.report.entity.do_KazamataNeri;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods")
@ApiModel(value="Goods对象", description="商品")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "商品名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "拼音信息")
    @TableField("py")
    private String py;

    @ApiModelProperty(value = "商品编号")
    @TableField("number")
    private String number;

    @ApiModelProperty(value = "规格型号")
    @TableField("spec")
    private String spec;

    @ApiModelProperty(value = "商品类别")
    @TableField("category")
    private String category;

    @ApiModelProperty(value = "商品品牌")
    @TableField("brand")
    private String brand;

    @ApiModelProperty(value = "商品单位[*:常规单位|-1:多单位]")
    @TableField("unit")
    private String unit;

    @ApiModelProperty(value = "采购价格")
    @TableField("buy")
    private BigDecimal buy;

    @ApiModelProperty(value = "销售价格")
    @TableField("sell")
    private BigDecimal sell;

    @ApiModelProperty(value = "商品条码")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "商品货位")
    @TableField("location")
    private String location;

    @ApiModelProperty(value = "库存阈值")
    @TableField("stock")
    private BigDecimal stock;

    @ApiModelProperty(value = "产品类型[0:常规商品|1:服务商品]")
    @TableField("type")
    private Boolean type;

    @ApiModelProperty(value = "备注信息")
    @TableField("data")
    private String data;

    @ApiModelProperty(value = "商品图像")
    @TableField("imgs")
    private String imgs;

    @ApiModelProperty(value = "图文详情")
    @TableField("details")
    private String details;

    @ApiModelProperty(value = "多单位配置")
    @TableField("units")
    private String units;

    @ApiModelProperty(value = "折扣策略")
    @TableField("strategy")
    private String strategy;

    @ApiModelProperty(value = "序列产品[0:关闭|1:启用]")
    @TableField("serial")
    private Boolean serial;

    @ApiModelProperty(value = "批次产品[0:关闭|1:启用]")
    @TableField("batch")
    private Boolean batch;

    @ApiModelProperty(value = "有效期[0:关闭|1:启用]")
    @TableField("validity")
    private Boolean validity;

    @ApiModelProperty(value = "保质期")
    @TableField("protect")
    private Integer protect;

    @ApiModelProperty(value = "预警阀值")
    @TableField("threshold")
    private Integer threshold;

    @ApiModelProperty(value = "扩展信息")
    @TableField("more")
    private String more;


}
