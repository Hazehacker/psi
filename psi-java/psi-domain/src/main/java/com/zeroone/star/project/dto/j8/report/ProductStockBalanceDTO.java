package com.zeroone.star.project.dto.j8.report;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品库存余额-行数据（查询 & 导出公用）
 * 说明：
 * - 动态仓库列放在 cells 中，导出时自定义表头横向展开。
 * - 右侧“汇总”使用 sumQty / sumAmount / avgCost。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("商品库存余额-行数据")
public class ProductStockBalanceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // ====== 后端关联字段（导出忽略） ======
    @ApiModelProperty(value = "余额表ID")
    @ExcelIgnore
    private String id;

    @ApiModelProperty(value = "商品ID")
    @ExcelIgnore
    private String goodsId;

    // ====== 基础展示列（行首） ======
    @ExcelProperty("商品名称")
    @ApiModelProperty(value = "商品名称", example = "小刀")
    private String productName;

    @ExcelProperty("商品编号")
    @ApiModelProperty(value = "商品编号", example = "01")
    private String productCode;

    @ExcelProperty("规格型号")
    @ApiModelProperty(value = "规格型号", example = "DJ-0001")
    private String specification;

    @ExcelProperty("单位")
    @ApiModelProperty(value = "单位名称", example = "个")
    private String unit;

    // ====== 动态仓库列（每仓：成本/数量/金额），导出时自定义表头展开 ======
    @ApiModelProperty(value = "各仓汇总单元（仓库、成本、数量、金额）")
    @ExcelIgnore
    private List<WarehouseCellDTO> cells;

    // ====== 右侧汇总列 ======
    @ExcelProperty("总数量")
    @ApiModelProperty(value = "总数量", example = "500")
    private BigDecimal sumQty;

    @ExcelProperty("总金额")
    @ApiModelProperty(value = "总金额(成本合计)", example = "7000.00")
    private BigDecimal sumAmount;

    @ExcelProperty("平均成本")
    @ApiModelProperty(value = "加权平均成本(=总金额/总数量)", example = "14.00")
    private BigDecimal avgCost;
}
