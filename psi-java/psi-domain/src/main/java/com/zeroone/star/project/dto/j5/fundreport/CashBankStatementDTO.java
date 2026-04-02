package com.zeroone.star.project.dto.j5.fundreport;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 现金银行报表对象
 */
@Data
@ApiModel("现金银行报表数据对象")
public class CashBankStatementDTO {

    @ExcelProperty(value = "账户名称", index = 0)
    @ApiModelProperty(value = "账户名称", example = "资金测试")
    private String name;

    @ExcelProperty(value = "单据类型", index = 1)
    @ApiModelProperty(value = "单据类型", example = "销售单")
    private String type;

    @ExcelProperty(value = "所属组织", index = 2)
    @ApiModelProperty(value = "所属组织", example = "默认组织")
    private String frame;

    @ExcelProperty(value = "往来单位", index = 3)
    @ApiModelProperty(value = "往来单位", example = "李虎")
    private String customer;

    @ExcelProperty(value = "单据时间", index = 4)
    @ApiModelProperty(value = "单据时间", example = "2025-10-10")
    private String time;

    @ExcelProperty(value = "单据编号", index = 5)
    @ApiModelProperty(value = "单据编号", example = "QTSRD250924171")
    private String number;

    @ExcelProperty(value = "收入", index = 6)
    @ApiModelProperty(value = "收入", example = "1000.0")
    private BigDecimal income;

    @ExcelProperty(value = "支出", index = 7)
    @ApiModelProperty(value = "支出", example = "1000.0")
    private BigDecimal expend;

    @ExcelProperty(value = "账户余额", index = 8)
    @ApiModelProperty(value = "账户余额", example = "1000.0")
    private BigDecimal balance;

    @ExcelProperty(value = "制单人", index = 9)
    @ApiModelProperty(value = "制单人", example = "张三")
    private String user;

    @ExcelProperty(value = "备注", index = 10)
    @ApiModelProperty(value = "备注", example = "备注信息")
    private String data;

}
