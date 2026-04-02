package com.zeroone.star.project.dto.j8.finance.otherincome;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 其他收入单导出简单报表：
 * 涉及到is ice_bill表的字段**
 *所以涉及两个表
 */
@Data
@ApiModel("其他收入单导出简单报表")
public class OtherIncomeExportSimpleReportDTO {

    @ExcelProperty("所属组织")
    @ApiModelProperty(value = "所属组织",example = "1")
    private String frame;

    @ExcelProperty("客户")
    @ApiModelProperty(value = "客户",example = "2")
    private String customer;

    @ExcelProperty("单据时间")
    @ApiModelProperty(value = "单据时间",example = "2010-05-25 12:30:00")
    private LocalDateTime time;//注意：这里数据库表里面填写的是int类型，但是实际上应该是time的数据类型

    @ExcelProperty("单据编号")
    @ApiModelProperty(value = "单据编号",example = "12345678")
    private String number;

    @ExcelProperty("单据金额")
    @ApiModelProperty(value = "单据金额",example = "1000")
    private BigDecimal total;

    @ExcelProperty("实际金额")
    @ApiModelProperty(value = "实际金额",example = "1000")
    private BigDecimal actual;

    @ExcelProperty("单据收款")
    //这个是is ice里面的money
    @ApiModelProperty(value = "单据收款",example = "1000")//对应数据库表中的实收金额
    private BigDecimal money;


    //将下main的修改为billMoney,原先为bill_money
    @ExcelProperty("核销金额")
    //is ice_bill表的字段**
    @ApiModelProperty(value = "核销金额",example = "1000")
    private BigDecimal billMoney;//原来数据库表中对应的字段名字是money


    @ExcelProperty("结算账户")
    @ApiModelProperty(value = "结算账户",example = "8909")
    private String account;

    @ExcelProperty("关联人员")
    //
    @ApiModelProperty(value = "关联人员",example = "张三")
    private String people;

    //修改成boolean形
    @ExcelProperty("审核状态")
    @ApiModelProperty(value = "审核状态",example = "1")
    private Boolean examine;

    //修改成Boolean形
    //再次修改核销状态，[0:未核销|1:部分核销|2:已核销]
    @ExcelProperty("核销状态")
    @ApiModelProperty(value = "核销状态",example = "已核销")
    private String nucleus;

    @ExcelProperty("制单人")
    @ApiModelProperty(value = "制单人",example = "李四")
    private String user;

    @ExcelProperty("备注信息")
    @ApiModelProperty(value = "备注信息",example = "简单的表单")
    private String data;


}
