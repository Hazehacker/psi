package com.zeroone.star.project.dto.j4.sale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("销售订单商品资料明细，对应CSV中单行商品数据")
public class SaleOrderItemImportDTO {

    @ApiModelProperty(value = "商品名称（商品资料）", required = true, example = "笔记本电脑")
    @NotBlank(message = "商品资料-商品名称不能为空")
    private String productName;

    @ApiModelProperty(value = "辅助属性（商品资料，如规格/颜色）", example = "Pro16/16G/512G")
    private String auxiliaryAttribute;

    @ApiModelProperty(value = "单位（商品资料，如个/台）", required = true, example = "台")
    @NotBlank(message = "商品资料-单位不能为空")
    private String unit;

    @ApiModelProperty(value = "仓库（商品资料，发货仓库）", required = true, example = "北京主仓")
    @NotBlank(message = "商品资料-仓库不能为空")
    private String warehouse;

    @ApiModelProperty(value = "单价（商品资料，不含税）", required = true, example = "6400.00")
    @NotNull(message = "商品资料-单价不能为空")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "数量（商品资料）", required = true, example = "4")
    @NotNull(message = "商品资料-数量不能为空")
    private BigDecimal quantity;

    @ApiModelProperty(value = "折扣率(%)（商品资料）", example = "5.00")
    private BigDecimal discountRate = BigDecimal.ZERO;

    @ApiModelProperty(value = "折扣额（商品资料）", example = "1280.00")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "金额（商品资料，不含税=单价*数量-折扣额）", required = true, example = "24320.00")
    @NotNull(message = "商品资料-金额不能为空")
    private BigDecimal amount;

    @ApiModelProperty(value = "税率(%)（商品资料）", required = true, example = "13.00")
    @NotNull(message = "商品资料-税率不能为空")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "税额（商品资料，=金额*税率）", required = true, example = "3161.60")
    @NotNull(message = "商品资料-税额不能为空")
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "价税合计（商品资料，=金额+税额）", required = true, example = "27481.60")
    @NotNull(message = "商品资料-价税合计不能为空")
    private BigDecimal totalWithTax;

    @ApiModelProperty(value = "备注信息（商品资料，商品级备注）", example = "预装Windows 11专业版")
    private String productRemark;
}