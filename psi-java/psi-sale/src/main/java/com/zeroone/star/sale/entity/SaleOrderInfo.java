package com.zeroone.star.sale.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("销售订单详情数据对象")
public class SaleOrderInfo {
    @ApiModelProperty(value = "销售订单详情id",example = "1")
    private String id;
    @ApiModelProperty(value = "销售订单id",example = "3")
    private String pid;
    @ApiModelProperty(value = "所属商品",example = "衣服")
    private String goods;
    @ApiModelProperty(value = "辅助单位",example = "cm")
    private String attr;
    @ApiModelProperty(value = "单位",example = "人民币")
    private String unit;
    @ApiModelProperty(value = "仓库",example = "北京XXXX仓库")
    private String warehouse;
    @ApiModelProperty(value = "单价",example = "30.0")
    private BigDecimal price;
    @ApiModelProperty(value = "数量",example = "10.0")
    private BigDecimal nums;
    @ApiModelProperty(value = "折扣",example = "0.9")
    private BigDecimal discount;
    @ApiModelProperty(value = "折扣额",example = "300.0")
    private BigDecimal dsc;
    @ApiModelProperty(value = "金额",example = "300.0")
    private BigDecimal total;
    @ApiModelProperty(value = "税率",example = "30.0")
    private BigDecimal tax;
    @ApiModelProperty(value = "税额",example = "90.0")
    private BigDecimal tat;
    @ApiModelProperty(value = "价税合计",example = "390.0")
    private BigDecimal tpt;
    @ApiModelProperty(value = "备注信息",example = "备注信息")
    private String data;
    @ApiModelProperty(value = "出库数量",example = "100。0")
    private BigDecimal handle;
}

