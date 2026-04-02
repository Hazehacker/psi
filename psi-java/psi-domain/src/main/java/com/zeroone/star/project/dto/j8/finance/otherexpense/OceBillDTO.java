package com.zeroone.star.project.dto.j8.finance.otherexpense;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OceBillDTO implements Serializable {
    @ExcelProperty(value = "编号", index = 0)
    @ApiModelProperty(value = "编号")
    private String id;
    @ExcelProperty(value = "组织名称", index = 1)
    @ApiModelProperty(value = "组织名称")
    private String frame;
    @ExcelProperty(value = "供应商名称", index = 2)
    @ApiModelProperty(value = "供应商名称")
    private String supplier;
    @ExcelProperty(value = "开票日期", index = 3)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开票日期")
    private String time;
    @ExcelProperty(value = "发票号码", index = 4)
    @ApiModelProperty(value = "发票号码")
    private String number;
    @ExcelProperty(value = "单据金额", index = 5)
    @ApiModelProperty(value = "单据金额")
    private String total;
    @ExcelProperty(value = "实收金额", index = 6)
    @ApiModelProperty(value = "实收金额")
    private String actual;
    @ExcelProperty(value = "单据付款", index = 7)
    @ApiModelProperty(value = "单据付款")
    private String money;
    @ExcelProperty(value = "核销金额", index = 8)
    @ApiModelProperty(value = "核销金额")
    private String amount;
    @ExcelProperty(value = "结算账户", index = 9)
    @ApiModelProperty(value = "结算账户")
    private String account;
    @ExcelProperty(value = "关联人员", index = 10)
    @ApiModelProperty(value = "关联人员")
    private String people;
    @ExcelProperty(value = "审核状态", index = 11)
    @ApiModelProperty(value = "审核状态")
    private String examine;
    @ExcelProperty(value = "核销状态", index = 12)
    @ApiModelProperty(value = "核销状态")
    private String nucleus;
    @ExcelProperty(value = "制单人", index = 13)
    @ApiModelProperty(value = "制单人")
    private String user;
    @ExcelProperty(value = "备注", index = 14)
    @ApiModelProperty(value = "备注")
    private String data;
}

