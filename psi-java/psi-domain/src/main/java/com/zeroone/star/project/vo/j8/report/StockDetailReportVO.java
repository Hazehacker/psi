package com.zeroone.star.project.vo.j8.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "收发明细-行")
public class StockDetailReportVO {

    // 基础辨识
    @ApiModelProperty(value = "单据类型编码", example = "buy/sell/sre/entry/extry/swapOut/swapEnter")
    private String type;

    @ApiModelProperty(value = "单据类型名称", example = "采购单/销售单/销售退货/其它入库/其它出库/调拨出/调拨入")
    private String typeName;

    @ApiModelProperty(value = "来源单据头ID（summary.class）")
    private String billId;

    @ApiModelProperty(value = "来源单据明细ID（summary.info）")
    private String billDetailId;

    @ApiModelProperty(value = "单据编号（来源头表.number）", example = "CGD20251001xxxx")
    private String number;

    @ApiModelProperty(value = "单据时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;

    // 商品 / 属性 / 仓库
    @ApiModelProperty("商品ID")
    private String goodsId;

    @ApiModelProperty(value = "商品名称", example = "男士纯棉圆领T恤")
    private String goodsName;

    @ApiModelProperty(value = "辅助属性", example = "纯白色|S")
    private String attr;

    @ApiModelProperty("仓库ID")
    private String warehouseId;

    @ApiModelProperty(value = "仓库名称", example = "1号仓库")
    private String warehouseName;

    @ApiModelProperty(value = "批次号")
    private String batch;

    @ApiModelProperty(value = "生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate mfd;

    @ApiModelProperty(value = "单位", example = "件")
    private String unit;

    // 业务价（来自来源明细.price），与成本分开
    @ApiModelProperty(value = "业务单价（来源明细.price）", example = "998.00")
    private BigDecimal price;

    // 入
    @ApiModelProperty(value = "入库单位成本（summary.uct，direction=1）")
    private BigDecimal inUct;

    @ApiModelProperty(value = "入库数量（direction=1）")
    private BigDecimal inNums;

    @ApiModelProperty(value = "入库成本额（summary.bct，direction=1）")
    private BigDecimal inBct;

    // 出
    @ApiModelProperty(value = "出库单位成本（summary.uct，direction=0）")
    private BigDecimal outUct;

    @ApiModelProperty(value = "出库数量（direction=0）")
    private BigDecimal outNums;

    @ApiModelProperty(value = "出库成本额（summary.bct，direction=0）")
    private BigDecimal outBct;

    // 结存（行后余额，来自 summary.balance）
    @ApiModelProperty(value = "结存单位成本（balance.uct）")
    private BigDecimal balUct;

    @ApiModelProperty(value = "结存数量（balance.uns）")
    private BigDecimal balUns;

    @ApiModelProperty(value = "结存成本额（balance.bct）")
    private BigDecimal balBct;

    // 往来单位（供应商/客户）
    @ApiModelProperty(value = "往来单位ID（supplierId/customerId）")
    private String partnerId;

    @ApiModelProperty(value = "往来单位名称", example = "供应商A/客户A")
    private String partnerName;
}
