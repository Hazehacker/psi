package com.zeroone.star.sale.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 销售订单商品明细
 * 用于封装订单中单个商品的详细信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("销售订单商品明细,销售订单中单个商品的具体信息")
public class SaleOrderExportDetailItem {


    @ApiModelProperty(value = "单据编号", example = "XSDD2510211358447")
    private String orderNo;

    @ApiModelProperty(value = "商品名称", example = "男士纯棉圆领T恤")
    private String productName;

    @ApiModelProperty(value = "规格型号", example = "DJ-0001")
    private String specification;

    @ApiModelProperty(value = "辅助属性（如颜色、尺寸等）", example = "纯白色|S")
    private String auxiliaryAttr;

    @ApiModelProperty(value = "计量单位", example = "个")
    private String unit;

    @ApiModelProperty(value = "所属仓库", example = "2号仓库")
    private String warehouse;

    @ApiModelProperty(value = "商品单价", example = "1688.00")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "订购数量", example = "1")
    private Integer quantity;

    @ApiModelProperty(value = "出库数量", example = "0")
    private Integer deliveredQuantity;

    @ApiModelProperty(value = "折扣率（百分比，如10表示10%）", example = "0")
    private BigDecimal discountRate;

    @ApiModelProperty(value = "折扣额", example = "0.00")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "金额（单价*数量-折扣额）", example = "1688.00")
    private BigDecimal amount;

    @ApiModelProperty(value = "商品备注信息", example = "无")
    private String itemRemarks;
}