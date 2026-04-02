package com.zeroone.star.project.query.j2.store;


import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.query.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-18 10:29
 * @Description: 商品查询参数对象
 * @Version: 1.0
 */
@Data
@ApiModel("InventoryQuery")
public class InventoryQuery extends PageQuery {

    @ApiModelProperty(value = "商品名称",example = "iPhone")
    private String goodsName;

    @ApiModelProperty(value = "商品编号",example = "123456")
    private String goodsNumber;

    @ApiModelProperty(value = "商品型号",example = "17pro")
    private String goodsSpec;

    @ApiModelProperty(hidden = true)
    private List<String> goodsCategoryIds;//类别树ID列表(内部处理字段)

    @ApiModelProperty(value = "商品类别",example = "0")
    private String goodsCategoryId;

    @ApiModelProperty(value = "商品品牌",example = "苹果")
    private String goodsBrand;

    @ApiModelProperty(value = "商品条码",example = "iPhoneCode")
    private String goodsCode;

    @ApiModelProperty(value = "商品备注",example = "备注信息")
    private String goodsRemark;

    @ApiModelProperty(value = "仓库信息",example = "1,2")
    private List<String> warehouseId;

    @ApiModelProperty(
            value = "库存类型：0-常规库存，1-非零库存，2-预警库存",
            example = "1")
    private Integer stockState;
}
