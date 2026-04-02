package com.zeroone.star.project.dto.j3.purchase;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

/**
 * <p>
 * 采购退货单DTO
 * </p>
 *
 * @author 简单点
 * @since 2025-10-19
 */
@Data
@ApiModel("采购退货单报表DTO")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseReturnDetailReportDTO {
    //bre + goods + bre_info + bre_bill + invoice

    //bre
    @ExcelProperty("供应商")
    @ApiModelProperty(value = "供应商",example = "李四")
    private String supplier;

    //bre
    @ExcelProperty("单据日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "单据日期",example = "2025-10-19")
    private String time;

    //bre
    @ExcelProperty("单据编号")
    @ApiModelProperty(value = "单据编号",example = "SKD2509231616598")
    private String number;

    //goods
    @ExcelProperty("商品名称")
    @ApiModelProperty(value = "商品名称",example = "A4纸123")
    private String name;

    //goods
    @ExcelProperty("规格型号")
    @ApiModelProperty(value = "规格型号",example = "210*297")
    private String spec;

    //bre_info
    @ExcelProperty("辅助属性")
    @ApiModelProperty(value = "辅助属性",example = " ")
    private String attr;

    //bre_info
    @ExcelProperty("单位")
    @ApiModelProperty(value = "单位",example = "包")
    private String unit;

    //bre_info
    @ExcelProperty("仓库")
    @ApiModelProperty(value = "仓库",example = "二仓")
    private String warehouse;

    //bre_info
    @ExcelProperty("单价")
    @ApiModelProperty(value = "单价",example = "20")
    private BigDecimal price;

    //bre_info
    @ExcelProperty("数量")
    @ApiModelProperty(value = "数量",example = "1")
    private BigDecimal nums;

    //bre_info
    @ExcelProperty("折扣率")
    @ApiModelProperty(value = "折扣率",example = "0")
    private BigDecimal discount;

    //bre_info
    @ExcelProperty("折扣额")
    @ApiModelProperty(value = "折扣额",example = "0")
    private BigDecimal dsc;

    //bre_info
    @ExcelProperty("金额")
    @ApiModelProperty(value = "金额",example = "20")
    private BigDecimal total;

    //bre_info
    @ExcelProperty("税率")
    @ApiModelProperty(value = "税率",example = "0")
    private BigDecimal tax;

    //bre_info
    @ExcelProperty("税额")
    @ApiModelProperty(value = "税额",example = "0")
    private BigDecimal tat;

    //bre_info
    @ExcelProperty("价税合计")
    @ApiModelProperty(value = "价税合计",example = "20")
    private BigDecimal tpt;

    //bre_info
    @ExcelProperty("备注信息")
    @ApiModelProperty(value = "备注信息 [bre_info]",example = " ")
    private String data;

    //bre
    @ExcelProperty("单据金额")
    @ApiModelProperty(value = "单据金额",example = "20")
    private BigDecimal bTotal;

    //bre
    @ExcelProperty("单据费")
    @ApiModelProperty(value = "单据费用",example = "0")
    private BigDecimal cost;

    //bre
    @ExcelProperty("实际金额")
    @ApiModelProperty(value = "实际金额",example = "0")
    private BigDecimal actual;

    //bre_bill
    @ExcelProperty("核销金额")
    @ApiModelProperty(value = "核销金额",example = "0")
    private BigDecimal money;

    //bre
    @ExcelProperty("结算账户")
    @ApiModelProperty(value = "结算账户",example = "中创资金")
    private String account;

    //invoice
    @ExcelProperty("发票信息")
    @ApiModelProperty(value = "发票信息",example = "3")
    private Integer nums2;

    //bre
    @ExcelProperty("关联人员")
    @ApiModelProperty(value = "关联人员",example = "NTJK_test_DD001")
    private String people;

    //bre
    @ExcelProperty("物流信息")
    @ApiModelProperty(value = "物流信息",example = " ")
    private String logistics;

    //bre
    @ExcelProperty("退货单备注信息")
    @ApiModelProperty(value = "备注信息 [bre表]",example = "")
    private String data2;

}
