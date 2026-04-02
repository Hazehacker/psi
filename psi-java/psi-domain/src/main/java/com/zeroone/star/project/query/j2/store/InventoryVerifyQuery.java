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
 * @CreateTime: 2025-10-18 18:08
 * @Description: 库存盘点参数对象
 * @Version: 1.0
 */
@Data
@ApiModel("InventoryVerifyQuery")
public class InventoryVerifyQuery extends PageQuery {
    @ApiModelProperty(value = "商品名称", example = "小米")
    private String name;
    @ApiModelProperty(value = "商品编号", example = "002")
    private String number;
    @ApiModelProperty(value = "商品型号", example = "DJ-232")
    private String spec;
    @ApiModelProperty(value = "商品类别", example = "电子产品")
    private String category;
    @ApiModelProperty(value = "商品品牌", example = "小米")
    private String brand;
    @ApiModelProperty(value = "仓库信息", example = "1号仓库【2号仓库】")
    private List<Integer> warehouseId;
    @ApiModelProperty(value = "商品备注", example = "随便")
    private String data;
}
