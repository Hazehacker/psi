package com.zeroone.star.project.dto.j6.basic_information.supplier_management;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@ApiModel("数据导入添加对象")
public class SupplierImportAddDTO {
    @ExcelProperty("供应商名称")
    @ApiModelProperty(value = "供应商名称", required = true)
    @NotBlank(message = "供应商名称不能为空")
    @Size(min = 1, max = 100, message = "供应商名称长度在1-100个字符之间")
    private String name;

    @ExcelProperty("供应商编号")
    @ApiModelProperty(value = "供应商编号", required = true)
    @NotBlank(message = "供应商编号不能为空")
    @Size(min = 1, max = 50, message = "供应商编号长度在1-50个字符之间")
    private String number;

    @ExcelProperty("所属组织")
    @ApiModelProperty(value = "所属组织", example = "1")
    @NotNull(message = "所属组织不能为空")
    @Min(value = 1, message = "所属组织ID必须大于0")
    private Integer frame;

    @ExcelProperty("所属用户")
    @ApiModelProperty(value = "所属用户", example = "1")
    @NotNull(message = "所属用户不能为空")
    @Min(value = 1, message = "所属用户ID必须大于0")
    private Integer user;

    @ExcelProperty("供应商类别")
    @ApiModelProperty(value = "供应商类别", example = "常规类别")
    @NotBlank(message = "供应商类别不能为空")
    @Size(min = 1, max = 50, message = "供应商类别长度在1-50个字符之间")
    private String category;

    @ExcelProperty("增值税税率")
    @ApiModelProperty(value = "增值税税率", required = true)
    @NotNull(message = "增值税税率不能为空")
    @DecimalMin(value = "0.0", message = "增值税税率不能小于0")
    @DecimalMax(value = "1.0", message = "增值税税率不能大于1")
    private Double rate;

    @ExcelProperty("开户银行")
    @ApiModelProperty(value = "开户银行")
    @Size(max = 100, message = "开户银行长度不能超过100个字符")
    private String bank;

    @ExcelProperty("银行账号")
    @ApiModelProperty(value = "银行账号")
    @Size(max = 50, message = "银行账号长度不能超过50个字符")
    private String account;

    @ExcelProperty("纳税号码")
    @ApiModelProperty(value = "纳税号码")
    @Size(max = 50, message = "纳税号码长度不能超过50个字符")
    private String tax;

    @ExcelProperty("备注")
    @ApiModelProperty(value = "备注")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String data;

    @ExcelProperty("联系资料")
    @ApiModelProperty(value = "联系资料")
    @Size(max = 500, message = "联系资料长度不能超过500个字符")
    private String contacts;

    // 错误信息，在导入验证失败时存储错误原因，导出到Excel时展示
    @ApiModelProperty(value = "错误信息")
    private String errorMessage;
}
