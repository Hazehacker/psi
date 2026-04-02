package com.zeroone.star.project.dto.j8.finance.otherincome;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *涉及三个表
 * 主要包含了一个zip文件，这个文件是包含两个内容
 */
@Data
@ApiModel("其他收入单导出详细报表")
public class OtherIncomeExportDetailedReportDTO {
    @ExcelProperty("客户")
    @ApiModelProperty(value = "客户",example = "2")
    private String customer;

    @ExcelProperty("单据时间")
    @ApiModelProperty(value = "单据时间",example = "2010-05-25 12:30:00")
    private LocalDateTime time;//注意：这里数据库表里面填写的是int类型，但是实际上应该是time的数据类型

    @ExcelProperty("单据编号")
    @ApiModelProperty(value = "单据编号",example = "12345678")
    private String number;

    @ExcelProperty("收入类别")
    //收入类别：在ice_info里面有iet手支类别的列，就用这个
    @ApiModelProperty(value = "收入类别",example = "4")
    private String iet;

    @ExcelProperty("结算金额")
    @ApiModelProperty(value = "结算金额",example = "4.2")//这个是ice_info表里面的
    private BigDecimal money;

    @ExcelProperty("备注信息")
    @ApiModelProperty(value = "备注信息",example = "ice表的备注")
    private String data;

    @ExcelProperty("单据金额")
    @ApiModelProperty(value = "单据金额",example = "1000")
    private BigDecimal total;

    @ExcelProperty("结算账户")
    @ApiModelProperty(value = "结算账户",example = "8909")
    private String account;

    @ExcelProperty("核销金额")
    //is ice_bill表的字段**
    @ApiModelProperty(value = "核销金额",example = "1000")
    private BigDecimal bill_money;//原来数据库表中对应的字段名字是money

    @ExcelProperty("关联人员")
    @ApiModelProperty(value = "关联人员",example = "李四")
    private String people;

    //还有个备注信息，所以是哪个表的备注信息




}
