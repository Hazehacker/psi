package com.zeroone.star.project.vo.j6.basic_information.customer_management;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("供应商导出VO")
public class CustomerExportVO {

    @ExcelProperty("供应商ID")
    private Integer id;

    @ExcelProperty("供应商名称")
    private String name;

    @ExcelProperty("供应商编号")
    private String number;

    @ExcelProperty("所属组织")
    private String frameName;

    @ExcelProperty("所属用户")
    private String userName;

    @ExcelProperty("供应商类别")
    private String category;

    @ExcelProperty("增值税税率")
    private Double rate;

    @ExcelProperty("开户银行")
    private String bank;

    @ExcelProperty("银行账号")
    private String account;

    @ExcelProperty("纳税号码")
    private String tax;

    @ExcelProperty("应付金额")
    private Double balance;

    @ExcelProperty("备注")
    private String data;

    @ExcelProperty("联系资料")
    private String contacts;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;
}
