package com.zeroone.star.project.dto.j5.fundreport;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 应收款明细表对象
 */
@Data
@ApiModel("应收款明细表数据对象")
public class DetailedAccountReceivableStatementDTO {

    @ExcelProperty(value = "客户", index = 0)
    @ApiModelProperty(value = "客户", example = "李虎")
    private String customer;

    @ExcelProperty(value = "单据类型", index = 1)
    @ApiModelProperty(value = "单据类型", example = "销售单")
    private String type;

    @ExcelProperty(value = "所属组织", index = 2)
    @ApiModelProperty(value = "所属组织", example = "默认组织")
    private String frame;

    @ExcelProperty(value = "单据时间", index = 3)
    @ApiModelProperty(value = "单据时间", example = "2025-9-21")
    private String time;

    @ExcelProperty(value = "单据编号", index = 4)
    @ApiModelProperty(value = "单据编号", example = "QTSRD250924171")
    private String number;

    @ExcelProperty(value = "增加应收款", index = 5)
    @ApiModelProperty(value = "增加应收款", example = "1000.0")
    private BigDecimal increaseAccountsReceivable;

    @ExcelProperty(value = "增加预收款", index = 6)
    @ApiModelProperty(value = "增加预收款", example = "1000.0")
    private BigDecimal increaseAdvanceReceipts;

    @ExcelProperty(value = "应收款余额", index = 7)
    @ApiModelProperty(value = "应收款余额", example = "1000,0")
    private BigDecimal balance;

    @ExcelProperty(value = "备注", index = 8)
    @ApiModelProperty(value = "备注", example = "备注信息")
    private String data;
}
