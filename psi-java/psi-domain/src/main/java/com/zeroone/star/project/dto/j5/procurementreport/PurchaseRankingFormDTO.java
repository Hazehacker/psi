package com.zeroone.star.project.dto.j5.procurementreport;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 采购排行表对象
 * @author impovo123
 * @date 2025/10/20
 */
@Data
@ApiModel("采购排行表数据对象")
public class PurchaseRankingFormDTO {
    @ApiModelProperty(value = "商品名称", example = "联想笔记本电脑")
    @ExcelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty(value = "辅助属性", example = "SN序列号")
    @ExcelProperty("辅助属性")
    private String attr;

    @ApiModelProperty(value = "商品编号", example = "lenovo123")
    @ExcelProperty("商品编号")
    private String goodsNumber;

    @ApiModelProperty(value = "规格型号", example = "lenovo123")
    @ExcelProperty("规格型号")
    private String spec;

    @ApiModelProperty(value = "单位", example = "件")
    @ExcelProperty("单位")
    private String unit;

    @ApiModelProperty(value = "数量", example = "171935")
    @ExcelProperty("数量")
    private BigDecimal nums;

    @ApiModelProperty(value = "折扣额", example = "0.00")
    @ExcelProperty("折扣额")
    private BigDecimal dsc;

    @ApiModelProperty(value = "税额", example = "9594.00")
    @ExcelProperty("税额")
    private BigDecimal tat;

    @ApiModelProperty(value = "价税合计", example = "84501.00")
    @ExcelProperty("价税合计")
    private BigDecimal tpt;

    @ApiModelProperty(value = "成本", example = "59.22")
    @ExcelProperty("成本")
    private BigDecimal cost;

    @ApiModelProperty(value = "总成本", example = "10182550.00")
    @ExcelProperty("总成本")
    private BigDecimal totalCost;
}
