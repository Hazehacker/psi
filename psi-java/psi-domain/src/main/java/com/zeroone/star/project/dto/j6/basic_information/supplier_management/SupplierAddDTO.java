package com.zeroone.star.project.dto.j6.basic_information.supplier_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 趣味生煎
 * @version 1.0
 * 描述：供应商添加对象
 */
@Data
@ApiModel("供应商添加对象")
public class SupplierAddDTO {

    @ApiModelProperty(value = "供应商名称", example = "01星球", required = true)
    @NotBlank(message = "供应商名称不能为空")
    private String name;

    @NotBlank(message = "供应商编号不能为空")
    @ApiModelProperty(value = "供应商编号", example = "1", required = true)
    private String number;

    @ApiModelProperty(value = "所属组织", example = "1")
    private Integer frame;

    @ApiModelProperty(value = "所属用户", example = "1")
    private Integer user;

    @ApiModelProperty(value = "供应商类别", example = "常规类别")
    private String category;

    @NotNull(message = "增值税税率不能为空")
    @ApiModelProperty(value = "增值税税率", example = "0", required = true)
    private Double rate;

    @ApiModelProperty(value = "开户银行", example = "")
    private String bank;

    @ApiModelProperty(value = "银行账号", example = "")
    private String account;

    @ApiModelProperty(value = "纳税号码", example = "")
    private String tax;

    @ApiModelProperty(value = "备注", example = "")
    private String data;

    @ApiModelProperty(value = "联系资料", example = "[]")
    private String contacts;
}
