package com.zeroone.star.project.vo.j6.basic_information.supplier_management;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("供应商导入模板")
public class SupplierImportTemplateVO {

    @ExcelProperty(value = "供应商名称*", index = 0)
    private String name;

    @ExcelProperty(value = "供应商编号*", index = 1)
    private String number;

    @ExcelProperty(value = "所属组织", index = 2)
    private String frame;

    @ExcelProperty(value = "所属用户", index = 3)
    private String user;

    @ExcelProperty(value = "供应商类别", index = 4)
    private String category;

    @ExcelProperty(value = "增值税税率*", index = 5)
    private Double rate;

    @ExcelProperty(value = "开户银行", index = 6)
    private String bank;

    @ExcelProperty(value = "银行账号", index = 7)
    private String account;

    @ExcelProperty(value = "纳税号码", index = 8)
    private String tax;

    @ExcelProperty(value = "备注", index = 9)
    private String data;

    @ExcelProperty(value = "联系资料", index = 10)
    private String contacts;
}
