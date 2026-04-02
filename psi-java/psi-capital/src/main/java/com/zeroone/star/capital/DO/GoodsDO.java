package com.zeroone.star.capital.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 商品DO
 */
@Data
@Accessors(chain = true)
@TableName("goods")
@ApiModel(value = "GoodsDO对象", description = "商品信息")
public class GoodsDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", required = true)
    @NotBlank(message = "主键ID不能为空")
    @Size(max = 32, message = "主键ID长度不能超过32个字符")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "商品名称", required = true)
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 32, message = "商品名称长度不能超过32个字符")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "拼音信息", required = true)
    @NotBlank(message = "拼音信息不能为空")
    @Size(max = 32, message = "拼音信息长度不能超过32个字符")
    @TableField("py")
    private String py;

    @ApiModelProperty(value = "商品编号", required = true)
    @NotBlank(message = "商品编号不能为空")
    @Size(max = 32, message = "商品编号长度不能超过32个字符")
    @TableField("number")
    private String number;

    @ApiModelProperty(value = "规格型号")
    @Size(max = 32, message = "规格型号长度不能超过32个字符")
    @TableField("spec")
    private String spec;

    @ApiModelProperty(value = "商品类别", required = true)
    @NotBlank(message = "商品类别不能为空")
    @Size(max = 32, message = "商品类别长度不能超过32个字符")
    @TableField("category")
    private String category;

    @ApiModelProperty(value = "商品品牌")
    @Size(max = 32, message = "商品品牌长度不能超过32个字符")
    @TableField("brand")
    private String brand;

    @ApiModelProperty(value = "商品单位[*:常规单位|-1:多单位]", required = true)
    @NotBlank(message = "商品单位不能为空")
    @Size(max = 32, message = "商品单位长度不能超过32个字符")
    @TableField("unit")
    private String unit;

    @ApiModelProperty(value = "采购价格", required = true)
    @NotNull(message = "采购价格不能为空")
    @DecimalMin(value = "0.0000", message = "采购价格不能小于0")
    @Digits(integer = 8, fraction = 4, message = "采购价格整数位不能超过8位，小数位不能超过4位")
    @TableField("buy")
    private BigDecimal buy;

    @ApiModelProperty(value = "销售价格", required = true)
    @NotNull(message = "销售价格不能为空")
    @DecimalMin(value = "0.0000", message = "销售价格不能小于0")
    @Digits(integer = 8, fraction = 4, message = "销售价格整数位不能超过8位，小数位不能超过4位")
    @TableField("sell")
    private BigDecimal sell;

    @ApiModelProperty(value = "商品条码")
    @Size(max = 64, message = "商品条码长度不能超过64个字符")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "商品货位")
    @Size(max = 64, message = "商品货位长度不能超过64个字符")
    @TableField("location")
    private String location;

    @ApiModelProperty(value = "库存阈值", required = true)
    @NotNull(message = "库存阈值不能为空")
    @DecimalMin(value = "0.0000", message = "库存阈值不能小于0")
    @Digits(integer = 8, fraction = 4, message = "库存阈值整数位不能超过8位，小数位不能超过4位")
    @TableField("stock")
    private BigDecimal stock;

    @ApiModelProperty(value = "产品类型[0:常规商品|1:服务商品]", required = true)
    @NotNull(message = "产品类型不能为空")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "备注信息")
    @Size(max = 256, message = "备注信息长度不能超过256个字符")
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
    private Integer serial = 0;

    @ApiModelProperty(value = "批次产品[0:关闭|1:启用]")
    @TableField("batch")
    private Integer batch = 0;

    @ApiModelProperty(value = "有效期[0:关闭|1:启用]")
    @TableField("validity")
    private Integer validity = 0;

    @ApiModelProperty(value = "保质期")
    @TableField("protect")
    private Integer protect = 0;

    @ApiModelProperty(value = "预警阀值")
    @TableField("threshold")
    private Integer threshold = 0;

    @ApiModelProperty(value = "扩展信息", required = true)
    @NotBlank(message = "扩展信息不能为空")
    @TableField("more")
    private String more;
}