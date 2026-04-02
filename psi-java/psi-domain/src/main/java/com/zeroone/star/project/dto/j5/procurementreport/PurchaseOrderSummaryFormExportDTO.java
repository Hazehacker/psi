package com.zeroone.star.project.dto.j5.procurementreport;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@ApiModel("采购汇总表excel导出数据对象")
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderSummaryFormExportDTO {
    // ========== 隐藏字段 供应商supplier，用户user，关联人员people ==========
    @ApiModelProperty(value = "供应商", example = "张三")
    @ExcelProperty("供应商")
    private String supplier;

    @ApiModelProperty(name = "用户", example = "张三")
    @ExcelProperty("用户")
    private String user;

    @ApiModelProperty(name = "关联人员", example = "张三")
    @ExcelProperty("关联人员")
    private String people;

    @ApiModelProperty(value = "商品名称", example = "衬衫")
    @ExcelProperty("商品名称")
    private String goods;

    @ApiModelProperty(value = "辅助属性", example = "XL")
    @ExcelProperty("辅助属性")
    private String attr;

    @ApiModelProperty(value = "仓库", example = "1号仓库")
    @ExcelProperty("仓库")
    private String warehouse;

    @ApiModelProperty(value = "单位", example = "件")
    @ExcelProperty("单位")
    private String unit;

    // ========== 采购单 ==========
    @ApiModelProperty("采购单单价")
    @ExcelProperty({"采购单", "单价"})
    private BigDecimal purchaseOrderPrice;

    @ApiModelProperty("采购单数量")
    @ExcelProperty({"采购单", "数量"})
    private BigDecimal purchaseOrderQuantity;

    @ApiModelProperty("采购单金额")
    @ExcelProperty({"采购单", "金额"})
    private BigDecimal purchaseOrderAmount;

    // ========== 采购退货单 ==========
    @ApiModelProperty("采购退货单单价")
    @ExcelProperty({"采购退货单", "单价"})
    private BigDecimal purchaseReturnOrderPrice;

    @ApiModelProperty("采购退货单数量")
    @ExcelProperty({"采购退货单", "数量"})
    private BigDecimal purchaseReturnOrderQuantity;

    @ApiModelProperty("采购退货单金额")
    @ExcelProperty({"采购退货单", "金额"})
    private BigDecimal purchaseReturnOrderAmount;

    // ========== 汇总 ==========
    @ApiModelProperty("汇总数量")
    @ExcelProperty({"汇总", "数量"})
    private BigDecimal summaryQuantity;

    @ApiModelProperty("汇总金额")
    @ExcelProperty({"汇总", "金额"})
    private BigDecimal summaryAmount;
}
