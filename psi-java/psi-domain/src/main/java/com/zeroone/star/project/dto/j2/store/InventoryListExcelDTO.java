package com.zeroone.star.project.dto.j2.store;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.dto.j2.store
 * @Author: 高
 * @CreateTime: 2025-11-3 10:05
 * @Description: 库存列表导出excel数据对象
 * @Version: 1.0
 */
@Data
@ApiModel("InventoryListExcelDTO")
public class InventoryListExcelDTO {
    // ================= 商品基础信息 =================
    @ExcelProperty(value = "商品名称", index = 0)
    @ApiModelProperty(value = "商品名称")
    private String name;

    @ExcelProperty(value = "库存数量", index = 1)
    @ApiModelProperty(value = "商品总库存数量")
    private BigDecimal totalStock;

    @ExcelProperty(value = { "仓库名称", "仓储数量" }, index = 2)
    @ApiModelProperty(value = "所属仓库名称")
    private String warehouseName;

    @ExcelProperty(value = { "仓库名称", "仓储数量" }, index = 3)
    @ApiModelProperty(value = "仓储数量", example = "100")
    private BigDecimal stockNum;

    @ExcelProperty(value = "预警阈值", index = 4)
    @ApiModelProperty(value = "预警阈值")
    private Integer threshold;

    @ExcelProperty(value = "商品编号", index = 5)
    @ApiModelProperty(value = "商品编号")
    private String number;

    @ExcelProperty(value = "规格型号", index = 6)
    @ApiModelProperty(value = "规格型号")
    private String spec;

    @ExcelProperty(value = "商品分类", index = 7)
    @ApiModelProperty(value = "类别名称")
    private String categoryName;

    @ExcelProperty(value = "商品品牌", index = 8)
    @ApiModelProperty(value = "商品品牌")
    private String brand;

    @ExcelProperty(value = "商品单位", index = 9)
    @ApiModelProperty(value = "商品单位")
    private String unit;

    @ExcelProperty(value = "商品条码", index = 10)
    @ApiModelProperty(value = "商品条码")
    private String code;

    @ExcelProperty(value = "商品备注", index = 11)
    @ApiModelProperty(value = "商品备注")
    private String remark;


}