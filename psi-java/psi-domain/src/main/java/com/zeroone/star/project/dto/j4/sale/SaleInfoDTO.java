package com.zeroone.star.project.dto.j4.sale;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("销售单详情数据对象")
public class SaleInfoDTO {
    @ApiModelProperty(value = "主键ID", example = "1")
    private String id;

    @ApiModelProperty(value = "所属销售单ID（外键）", example = "1001", required = true)
    private String pid;

    @ApiModelProperty(value = "关联详情|SOR", example = "2001")
    private String source;

    @ApiModelProperty(value = "所属商品", example = "3001")
    @ExcelProperty(value = "商品名称",index = 1)
    private String goods;

    @ApiModelProperty(value = "辅助属性", example = "红色|XL", allowableValues = "length <= 64")
    @ExcelProperty(value = "辅助属性",index = 2)
    private String attr;

    @ApiModelProperty(value = "单位", example = "件", allowableValues = "length <= 32")
    @ExcelProperty(value = "单位",index = 3)
    private String unit;

    @ApiModelProperty(value = "仓库", example = "4001")
    @ExcelProperty(value = "仓库",index = 4)
    private String warehouse;

    @ApiModelProperty(value = "批次号", example = "BATCH20251018", allowableValues = "length <= 32")
    @ExcelProperty(value = "批次号",index = 5)
    private String batch;

    @ApiModelProperty(value = "生产日期", example = "2025-10-18")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ExcelProperty(value = "生产日期",index = 6)
    private Date mfd;

    @ApiModelProperty(value = "单价", example = "99.9999")
    @ExcelProperty(value = "单价",index = 7)
    private BigDecimal price;

    @ApiModelProperty(value = "数量", example = "10.0000")
    @ExcelProperty(value = "数量",index = 8)
    private BigDecimal nums;

    @ApiModelProperty(value = "序列号（多个用逗号分隔）", example = "SN123456")
    @ExcelProperty(value = "序列号",index = 9)
    private String serial;

    @ApiModelProperty(value = "折扣率", example = "0.95", allowableValues = "范围: 0.00 ~ 1.00")
    @ExcelProperty(value = "折扣率",index = 10)
    private BigDecimal discount;

    @ApiModelProperty(value = "折扣额", example = "5.0000")
    @ExcelProperty(value = "折扣额",index = 11)
    private BigDecimal dsc;

    @ApiModelProperty(value = "金额", example = "95.0000")
    @ExcelProperty(value = "金额",index = 12)
    private BigDecimal total;

    @ApiModelProperty(value = "税率", example = "0.13")
    @ExcelProperty(value = "税率",index = 13)
    private BigDecimal tax;

    @ApiModelProperty(value = "税额", example = "12.3500")
    @ExcelProperty(value = "税额",index = 14)
    private BigDecimal tat;

    @ApiModelProperty(value = "价税合计", example = "107.3500")
    @ExcelProperty(value = "价税合计",index = 15)
    private BigDecimal tpt;

    @ApiModelProperty(value = "备注信息", example = "备注信息", allowableValues = "length <= 256")
    @ExcelProperty(value = "备注信息",index = 16)
    private String data;

    @ApiModelProperty(value = "退货数量", example = "2.0000")
    private BigDecimal retreat;
}
