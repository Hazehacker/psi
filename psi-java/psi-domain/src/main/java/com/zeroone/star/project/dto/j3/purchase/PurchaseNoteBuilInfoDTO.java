package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
@ApiModel(description = "采购商品明细（关联buy_info表及关联数据）")
public class PurchaseNoteBuilInfoDTO {
    @ApiModelProperty(value = "明细ID", example = "6")
    private String id;
    @ApiModelProperty(value = "关联采购单主表ID", example = "8")
    private String pid;
    @ApiModelProperty(value = "关联上游单据ID", example = "6")
    private String source;
    @ApiModelProperty(value = "商品ID", example = "3")
    private String goods;
    @ApiModelProperty(value = "辅助属性", example = "")
    private String attr;
    @ApiModelProperty(value = "商品单位", example = "个")
    private String unit;
    @ApiModelProperty(value = "仓库ID", example = "1")
    private String warehouse;
    @ApiModelProperty(value = "批次号", example = "5")
    private String batch;
    @ApiModelProperty(value = "生产日期", example = "2025-10-21")
    private String mfd;
    @ApiModelProperty(value = "商品单价", example = "2")
    private BigDecimal price;
    @ApiModelProperty(value = "采购数量", example = "1")
    private BigDecimal nums;
    @ApiModelProperty(value = "序列号列表（JSON数组字符串）", example = "[]")
    private String serial;
    @ApiModelProperty(value = "折扣率", example = "0")
    private BigDecimal discount;
    @ApiModelProperty(value = "折扣额", example = "0")
    private BigDecimal dsc;
    @ApiModelProperty(value = "明细金额", example = "2")
    private BigDecimal total;
    @ApiModelProperty(value = "税率", example = "0")
    private BigDecimal tax;
    @ApiModelProperty(value = "税额", example = "0")
    private BigDecimal tat;
    @ApiModelProperty(value = "价税合计", example = "2")
    private BigDecimal tpt;
    @ApiModelProperty(value = "商品备注", example = "")
    private String data;
    @ApiModelProperty(value = "退货数量", example = "0")
    private BigDecimal retreat;
    @ApiModelProperty(value = "关联商品基础信息", required = false)
    private GoodsDataDTO goodsData;
    @ApiModelProperty(value = "关联仓库基础信息", required = false)
    private WarehouseDataDTO warehouseData;
}