package com.zeroone.star.project.dto.j6.basic_information.customer_management;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@ApiModel("客户导入添加对象")
public class CustomerImportAddDTO {

    @ExcelProperty("客户名称")
    @ApiModelProperty(value = "客户名称", required = true)
    @NotBlank(message = "客户名称不能为空")
    @Size(min = 1, max = 100, message = "客户名称长度在1-100个字符之间")
    private String name;

    @ExcelProperty("客户编号")
    @ApiModelProperty(value = "客户编号", required = true)
    @NotBlank(message = "客户编号不能为空")
    @Size(min = 1, max = 50, message = "客户编号长度在1-50个字符之间")
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

    @ExcelProperty("客户类别")
    @ApiModelProperty(value = "客户类别", example = "常规类别")
    @NotBlank(message = "客户类别不能为空")
    @Size(min = 1, max = 50, message = "客户类别长度在1-50个字符之间")
    private String category;

    @ExcelProperty("客户等级")
    @ApiModelProperty(value = "客户等级", example = "会员")
    @NotBlank(message = "客户等级不能为空")
    @Size(min = 1, max = 20, message = "客户等级长度在1-20个字符之间")
    private String grade;

    @ExcelProperty("开户银行")
    @ApiModelProperty(value = "开户银行")
    @Size(max = 100, message = "开户银行长度不能超过100个字符")
    private String bank;

    @ExcelProperty("银行账号")
    @ApiModelProperty(value = "银行账号")
    @Size(max = 50, message = "银行账号长度不能超过50个字符")
    @Pattern(regexp = "^[0-9]*$", message = "银行账号只能包含数字")
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

    @ExcelProperty("应收款余额")
    @ApiModelProperty(value = "应收款余额")
    @DecimalMin(value = "0.0", message = "应收款余额不能小于0")
    @Digits(integer = 10, fraction = 2, message = "应收款余额整数部分最多10位，小数部分最多2位")
    private Double balance;

    @ExcelProperty("扩展信息")
    @ApiModelProperty(value = "扩展信息")
    @Size(max = 1000, message = "扩展信息长度不能超过1000个字符")
    private String more;

    // 错误信息，在导入验证失败时存储错误原因，导出到Excel时展示
    @ExcelProperty("错误信息")
    @ApiModelProperty(value = "错误信息")
    private String errorMessage;
}