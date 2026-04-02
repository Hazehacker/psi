package com.zeroone.star.sale.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("销售退货订单详情数据对象")
public class SaleReturnInfo {
    @ApiModelProperty(value = "主键ID", example = "1")
    private String id;

    @ExcelProperty(value = "商品名称", index = 0)
    @ApiModelProperty(value = "商品名称", example = "xx")
    private String goods;

    @ExcelProperty(value = "辅助属性", index = 1)
    @ApiModelProperty(value = "辅助属性", example = "xx")
    private String attr;

    @ExcelProperty(value = "单位", index = 2)
    @ApiModelProperty(value = "单位", example = "个")
    private String unit;

    @ExcelProperty(value = "仓库", index = 3)
    @ApiModelProperty(value = "仓库名称（需转换为仓库ID）", example = "xx")
    private Long warehouse;

    @ExcelProperty(value = "批次号", index = 4)
    @ApiModelProperty(value = "批次号", example = "xxx")
    private String batch;

    @ExcelProperty(value = "生产日期", index = 5)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生产日期", example = "2020-01-01")
    private LocalDate mfd;

    @ExcelProperty(value = "单价", index = 6)
    @ApiModelProperty(value = "单价", example = "1")
    private BigDecimal price;

    @ExcelProperty(value = "数量", index = 7)
    @ApiModelProperty(value = "数量", example = "5")
    private BigDecimal nums;

    @ExcelProperty(value = "序列号", index = 8)
    @ApiModelProperty(value = "序列号", example = "xxx")
    private String serial;

    @ExcelProperty(value = "折扣率(%)", index = 9)
    @ApiModelProperty(value = "折扣率（Excel中是百分比，需除以100）", example = "20.0")
    private BigDecimal discount;

    @ExcelProperty(value = "折扣额", index = 10)
    @ApiModelProperty(value = "折扣额", example = "20.0")
    private BigDecimal dsc;

    @ExcelProperty(value = "金额", index = 11)
    @ApiModelProperty(value = "金额", example = "50.0")
    private BigDecimal total;

    @ExcelProperty(value = "税率(%)", index = 12)
    @ApiModelProperty(value = "税率（Excel中是百分比，需除以100）", example = "0.2")
    private BigDecimal tax;

    @ExcelProperty(value = "税额", index = 13)
    @ApiModelProperty(value = "税额", example = "50.2")
    private BigDecimal tat;

    @ExcelProperty(value = "价税合计", index = 14)
    @ApiModelProperty(value = "价税合计", example = "50.0")
    private BigDecimal tpt;

    @ExcelProperty(value = "备注信息", index = 15)
    @ApiModelProperty(value = "备注信息", example = "xxx")
    private String data;
}