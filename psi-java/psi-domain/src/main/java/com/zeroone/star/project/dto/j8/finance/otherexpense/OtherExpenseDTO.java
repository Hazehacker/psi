package com.zeroone.star.project.dto.j8.finance.otherexpense;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 其他支出单数据对象
 * <p>
 * 涉及数据库表：<b>oce,oce_bill</b>
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("其他支出单数据对象/简易报表内容数据对象")
public class OtherExpenseDTO {

    @ExcelProperty(value = "支出单ID", index = 0)
    @ApiModelProperty(value = "支出单ID", example = "OCE001")
    private String id;

    @ExcelProperty(value = "所属组织", index = 1)
    @ApiModelProperty(value = "所属组织", example = "F001")
    private String frame;

    @ExcelProperty(value = "供应商", index = 2)
    @ApiModelProperty(value = "供应商", example = "S001")
    private String supplier;

    @ExcelProperty(value = "单据时间", index = 3)
    @ApiModelProperty(value = "单据时间", example = "2025-10-20 15:23:00")
    private LocalDateTime time;

    @ExcelProperty(value = "单据编号", index = 4)
    @ApiModelProperty(value = "单据编号", example = "QTZCD2510201810404")
    private String number;

    @ExcelProperty(value = "单据金额", index = 5)
    @ApiModelProperty(value = "单据金额", example = "12345.67")
    private BigDecimal total;

    @ExcelProperty(value = "实际金额", index = 6)
    @ApiModelProperty(value = "实际金额", example = "12345.67")
    private BigDecimal actual;

    @ExcelProperty(value = "实付金额/单据收款", index = 7)
    @ApiModelProperty(value = "实付金额/单据收款", example = "12345.67")
    private BigDecimal money;

    //oce_bill
    @ExcelProperty(value = "核销金额", index = 8)
    @ApiModelProperty(value = "核销金额", example = "123.33")
    private BigDecimal writeOffAmount;

    @ExcelProperty(value = "结算账户", index = 9)
    @ApiModelProperty(value = "结算账户", example = "A001")
    private String account;

    @ExcelProperty(value = "关联人员", index = 10)
    @ApiModelProperty(value = "关联人员", example = "P001")
    private String people;

    @ExcelProperty(value = "审核状态", index = 11)
    @ApiModelProperty(value = "审核状态", example = "1")
    private int examine;

    @ExcelProperty(value = "核销状态", index = 12)
    @ApiModelProperty(value = "核销状态", example = "0")
    private int nucleus;

    @ExcelProperty(value = "制单人", index = 13)
    @ApiModelProperty(value = "制单人", example = "U001")
    private String user;

    @ExcelProperty(value = "备注信息", index = 14)
    @ApiModelProperty(value = "备注信息", example = "备注test")
    private String data;
}

