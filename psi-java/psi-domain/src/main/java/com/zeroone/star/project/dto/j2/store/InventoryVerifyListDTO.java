package com.zeroone.star.project.dto.j2.store;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.dto.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-18 18:15
 * @Description: 库存盘点数据对象
 * @Version: 1.0
 */
@Data
@ApiModel("InventoryVerifyListDTO")
public class InventoryVerifyListDTO implements Serializable {

    //    @ApiModelProperty(value = "库存盘点id", example = "1")
//    private Integer id;
    @ExcelProperty(value = "商品id", index = 0)
    @ApiModelProperty(value = "商品id", example = "g001")
    private String id;
    @ExcelProperty(value = "商品名称", index = 1)
    @ApiModelProperty(value = "商品名称", example = "刀具")
    private String name;
    @ExcelProperty(value = "商品属性名称", index = 2)
    @ApiModelProperty(value = "商品属性对象列表", example = "[name=’红色‘,name=’蓝色‘,name='白色',name='XL',name='M',name='L'}...]")
    private String attrName;
    @ExcelProperty(value = "库存数量", index = 3)
    @ApiModelProperty(value = "库存数量", example = "122")
    private String totalStock;
    @ApiModelProperty(value = "该属性的仓库库存明细",example = "1号仓库：111,2号仓库：10...")
    private List<WarehouseStockDTO> warehouses;
    @ExcelProperty(value = "盘盈盘亏", index = 4)
    @ApiModelProperty(value = "盘盈盘亏", example = "120")
    private BigDecimal InventoryDifference;
    @ExcelProperty(value = "商品编号", index = 5)
    @ApiModelProperty(value="商品编号",example = "0003")
    private String number;
    @ExcelProperty(value = "规格型号", index = 6)
    @ApiModelProperty(value = "规格型号", example = "DJ-111")
    private String spec;
    @ExcelProperty(value = "商品分类id", index = 7)
    @ApiModelProperty(value = "商品分类id", example = "7")
    private Integer categoryId;
    @ExcelProperty(value = "商品分类名称", index = 8)
    @ApiModelProperty(value = "商品类型对象",example = "name=’五金工具‘")
    private String categoryName;
    @ExcelProperty(value = "商品品牌", index = 9)
    @ApiModelProperty(value = "商品品牌", example = "飞虎")
    private String brand;
    @ExcelProperty(value = "商品单位", index = 10)
    @ApiModelProperty(value = "商品单位", example = "个")
    private String unit;
    @ExcelProperty(value = "商品条码", index = 11)
    @ApiModelProperty(value = "商品条码", example = "1")
    private String code;
    @ExcelProperty(value = "商品备注", index = 12)
    @ApiModelProperty(value = "商品备注", example = "随便")
    private String data;
}
