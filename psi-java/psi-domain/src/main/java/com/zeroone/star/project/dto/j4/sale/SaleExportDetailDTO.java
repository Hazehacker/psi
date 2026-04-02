// psi-domain/src/main/java/com/zeroone/star/project/dto/j4/sale/SaleDetailDTO.java
package com.zeroone.star.project.dto.j4.sale;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javafx.util.converter.LocalDateStringConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("销售单详情数据对象")
public class SaleExportDetailDTO {

//    @ApiModelProperty(value = "详情ID", example = "sell_info_123456")
//    private String id;
//
//    @ApiModelProperty(value = "所属销售单ID", example = "sell_123456")
//    private String pid;
//
//    @ApiModelProperty(value = "关联详情", example = "sor_info_123456")
//    private String source;

    @ApiModelProperty(value = "商品id", example = "goods_123456")
    @ExcelProperty(value = "商品id", index = 0)
    private String goods;

    @ApiModelProperty(value = "辅助属性", example = "红色,128G")
    @ExcelProperty(value = "辅助属性", index = 1)
    private String attr;

    @ApiModelProperty(value = "单位", example = "台")
@ExcelProperty(value = "单位", index = 2)
    private String unit;

    @ApiModelProperty(value = "仓库", example = "warehouse_123456")
    @ExcelProperty(value = "仓库", index = 3)
    private String warehouse;

    @ApiModelProperty(value = "批次号", example = "BATCH20230101")
    @ExcelProperty(value = "批次号", index = 4)
    private String batch;

    @ApiModelProperty(value = "生产日期", example = "2023-01-01")
    @ExcelProperty(value = "生产日期", index = 5)
    private Date mfd;

    @ApiModelProperty(value = "单价", example = "5000.00")
    @ExcelProperty(value = "单价", index = 6)
    private BigDecimal price;

    @ApiModelProperty(value = "数量", example = "2.0000")
    @ExcelProperty(value = "数量", index = 7)
    private BigDecimal nums;

//    @ApiModelProperty(value = "序列号", example = "SN123456789")
//    private String serial;

    @ApiModelProperty(value = "折扣率", example = "0.00")
    @ExcelProperty(value = "折扣率", index = 8)
    private BigDecimal discount;

    @ApiModelProperty(value = "折扣额", example = "0.0000")
    @ExcelProperty(value = "折扣额", index = 9)
    private BigDecimal dsc;

    @ApiModelProperty(value = "金额", example = "10000.00")
    @ExcelProperty(value = "金额", index = 10)
    private BigDecimal total;

//    @ApiModelProperty(value = "税率", example = "13.00")
//    private BigDecimal tax;
//
//    @ApiModelProperty(value = "税额", example = "1300.00")
//    private BigDecimal tat;

//    @ApiModelProperty(value = "价税合计", example = "11300.00")
//    private BigDecimal tpt;

    @ApiModelProperty(value = "备注信息", example = "商品详情备注信息")
    private String data;

//    @ApiModelProperty(value = "退货数量", example = "0.0000")
//    private BigDecimal retreat;
}