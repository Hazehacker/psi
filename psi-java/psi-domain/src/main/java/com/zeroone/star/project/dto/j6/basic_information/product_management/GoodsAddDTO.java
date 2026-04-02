package com.zeroone.star.project.dto.j6.basic_information.product_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 描述：新增商品数据传输对象
 *
 * @author 杨潇
 * @version 1.0.0
 */
@Data
@ApiModel("新增商品数据传输对象")
public class GoodsAddDTO {

    @NotBlank(message = "商品名称不能为空")
    @ApiModelProperty(value = "商品名称", example = "苹果手机", required = true)
    private String name;

    @ApiModelProperty(value = "拼音", example = "pgsjpg")
    private String py = "";  // 数据库 NOT NULL，设置默认值

    @NotBlank(message = "商品编号不能为空")
    @ApiModelProperty(value = "商品编号", example = "GD001", required = true)
    private String number;

    @ApiModelProperty(value = "规格", example = "256G")
    private String spec;

    @NotBlank(message = "分类ID不能为空")
    @ApiModelProperty(value = "分类ID", example = "cat001", required = true)
    private String category;

    @ApiModelProperty(value = "品牌", example = "Apple")
    private String brand;

    @NotBlank(message = "单位不能为空")
    @ApiModelProperty(value = "单位", example = "台", required = true)
    private String unit;

    @NotNull(message = "采购价不能为空")
    @ApiModelProperty(value = "采购价", example = "5000", required = true)
    private BigDecimal buy;

    @NotNull(message = "销售价不能为空")
    @ApiModelProperty(value = "销售价", example = "6000", required = true)
    private BigDecimal sell;

    @ApiModelProperty(value = "条码", example = "6901234567890")
    private String code;

    @ApiModelProperty(value = "存放位置", example = "A区01货架")
    private String location;

    @ApiModelProperty(value = "库存", example = "30")
    private BigDecimal stock = BigDecimal.ZERO;  // 数据库 NOT NULL，设置默认值

    @ApiModelProperty(value = "商品类型", example = "1")
    private Integer type = 0;  // 数据库 NOT NULL，设置默认值

    @ApiModelProperty(value = "商品图片（JSON数组字符串）", example = "[\"url1\",\"url2\"]")
    private String imgs = "[]";  // 默认空数组

    @ApiModelProperty(value = "商品详情", example = "商品详细描述")
    private String details;

    @ApiModelProperty(value = "多单位（JSON对象字符串）", example = "{}")
    private String units = "{}";  // 默认空对象

    @ApiModelProperty(value = "策略（JSON对象字符串）", example = "{}")
    private String strategy = "{}";  // 默认空对象

    @ApiModelProperty(value = "是否序列号管理", example = "0")
    private Integer serial;

    @ApiModelProperty(value = "是否批次管理", example = "0")
    private Integer batch;

    @ApiModelProperty(value = "是否有效期管理", example = "0")
    private Integer validity;

    @ApiModelProperty(value = "保质期（天）", example = "365")
    private Integer protect;

    @ApiModelProperty(value = "预警阈值", example = "10")
    private Integer threshold;

    @ApiModelProperty(value = "更多信息（JSON对象字符串）", example = "{}")
    private String more = "{}";  // 数据库 NOT NULL，设置默认值

    @ApiModelProperty(value = "备注信息", example = "")
    private String data;
}
