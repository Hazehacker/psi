package com.zeroone.star.project.dto.j2.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.dto.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-18 18:13
 * @Description: 批次查询数据对象
 * @Version: 1.0
 */
@Data
@ApiModel("BatchListDTO")
public class BatchListDTO {
    @ApiModelProperty(value = "商品id（内部使用）", example = "1")
    private String id;

    @ApiModelProperty(value = "商品名称", example = "牛奶")
    private String name;

    @ApiModelProperty(value = "库存数量", example = "300")
    private BigDecimal totalStock;

    @ApiModelProperty(value = "保质期（天）", example = "365")
    private Integer protect;

    @ApiModelProperty(value = "预警阈值", example = "10")
    private Integer threshold ;

    @ApiModelProperty(value = "库存阈值")
    @JsonIgnore
    private BigDecimal stock;

    @ApiModelProperty(value = "商品编号", example = "0001")
    private String number;

    @ApiModelProperty(value = "规格型号", example = "500ml")
    private String spec;

    @ApiModelProperty(value = "商品品牌", example = "伊利")
    private String brand;

    @ApiModelProperty(value = "商品单位", example = "盒")
    private String unit;

    @ApiModelProperty(value = "商品条码", example = "111222")
    private String code;

    @ApiModelProperty(value = "商品备注", example = "备注信息")
    private String data;

    @ApiModelProperty(value = "商品分类id（内部使用）")
    private String categoryId;

    @ApiModelProperty(value = "商品分类")
    private String categoryName;

    @ApiModelProperty(value = "是否有属性", example = "true")
    @JsonIgnore
    private Boolean hasAttr;

    @ApiModelProperty(value = "属性批次列表")
    private List<BatchAttrDTO> attrBatches;

    @ApiModelProperty(value = "无属性批次列表")
    private List<BatchNumberDTO> noAttrBatches;

    @ApiModelProperty(value = "商品仓库库存列表")
    private List<WarehouseStockDTO> goodsWarehouses ;

}
