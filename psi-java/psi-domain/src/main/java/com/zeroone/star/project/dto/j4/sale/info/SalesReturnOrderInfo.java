package com.zeroone.star.project.dto.j4.sale.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("销售退货单详情数据对象")
public class SalesReturnOrderInfo {

    @ApiModelProperty(value = "销售退货单详情id",example = "1")
    private String id;
    @ApiModelProperty(value = "销售单详情id",example = "1")
    private String pid;
    @ApiModelProperty(value = "退货商品id",example = "3")
    private String goods;

    @ApiModelProperty(value = "辅助属性",example = "")
    private String attr;

    @ApiModelProperty(value = "单位",example = "个")
    private String unit;

    @ApiModelProperty(value = "仓库id",example = "1")
    private String warehouse;

    @ApiModelProperty(value = "批次号",example = "333")
    private String batch;

    @ApiModelProperty(value = "生产日期",example = "2023-09-01")
    private LocalDate mfd;

    @ApiModelProperty(value = "单价",example = "5.0000")
    private BigDecimal price;

    @ApiModelProperty(value = "数量",example = "1.0000")
    private BigDecimal nums;

    @ApiModelProperty(value = "金额",example = "5.0000")
    private BigDecimal total;

    @ApiModelProperty(value = "备注信息",example = "这是退货单备注")
    private String data;

    @ApiModelProperty(value = "关联销售单详情ID（来源详情，对应is_sell_info表）", example = "0")
    private Integer source;

    @ApiModelProperty(value = "序列号（多个用逗号分隔）", example = "SN123456,SN123457")
    private String serial;

    @ApiModelProperty(value = "折扣率（百分比）", example = "0.00")
    private BigDecimal discount;

    @ApiModelProperty(value = "折扣额", example = "0.00")
    private BigDecimal dsc;

    @ApiModelProperty(value = "税率（百分比）", example = "13.00")
    private BigDecimal tax;

    @ApiModelProperty(value = "税额", example = "39.00")
    private BigDecimal tat;

    @ApiModelProperty(value = "价税合计", example = "339.00")
    private BigDecimal tpt;

}