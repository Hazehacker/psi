package com.zeroone.star.project.query.j1.homepage;

import java.math.BigDecimal;
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
 * @author Duuuuuu
 * @since 2025-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("is_goods")
@ApiModel(value="IsGoods对象", description="商品")
public class IsGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "拼音信息")
    private String py;

    @ApiModelProperty(value = "商品编号")
    private String number;

    @ApiModelProperty(value = "规格型号")
    private String spec;

    @ApiModelProperty(value = "商品类别")
    private Integer category;

    @ApiModelProperty(value = "商品品牌")
    private String brand;

    @ApiModelProperty(value = "商品单位[*:常规单位|-1:多单位]")
    private String unit;

    @ApiModelProperty(value = "采购价格")
    private BigDecimal buy;

    @ApiModelProperty(value = "销售价格")
    private BigDecimal sell;

    @ApiModelProperty(value = "商品条码")
    private String code;

    @ApiModelProperty(value = "商品货位")
    private String location;

    @ApiModelProperty(value = "库存阈值")
    private BigDecimal stock;

    @ApiModelProperty(value = "产品类型[0:常规商品|1:服务商品]")
    private Boolean type;

    @ApiModelProperty(value = "备注信息")
    private String data;

    @ApiModelProperty(value = "商品图像")
    private String imgs;

    @ApiModelProperty(value = "图文详情")
    private String details;

    @ApiModelProperty(value = "多单位配置")
    private String units;

    @ApiModelProperty(value = "折扣策略")
    private String strategy;

    @ApiModelProperty(value = "序列产品[0:关闭|1:启用]")
    private Boolean serial;

    @ApiModelProperty(value = "批次产品[0:关闭|1:启用]")
    private Boolean batch;

    @ApiModelProperty(value = "有效期[0:关闭|1:启用]")
    private Boolean validity;

    @ApiModelProperty(value = "保质期")
    private Integer protect;

    @ApiModelProperty(value = "预警阀值")
    private Integer threshold;

    @ApiModelProperty(value = "扩展信息")
    private String more;


}
