package com.zeroone.star.project.dto.j6.basic_information.customer_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author cmanPro
 * @version 1.0
 * 描述：客户添加对象
 */
@Data
@ApiModel("客户添加对象")
public class CustomerAddDTO {


    @ApiModelProperty(value = "客户名称", required = true)
    @NotBlank(message = "客户名称不能为空")
    private String name;

    @ApiModelProperty(value = "拼音", required = true)
    @NotBlank(message = "拼音不能为空")
    private String py;

    @NotNull(message = "客户编号不能为空")
    @ApiModelProperty(value = "客户编号", required = true)
    private String number;

    @ApiModelProperty(value = "所属组织", example = "1")
    @NotBlank(message = "所属组织不能为空")
    private String frame;

    @ApiModelProperty(value = "所属用户", example = "1")
    @NotBlank(message = "所属用户不能为空")
    private String user;

    @ApiModelProperty(value = "客户类别", example = "常规类别")
    @NotBlank(message = "客户类别不能为空")
    private String category;

    @ApiModelProperty(value = "客户等级", example = "会员")
    @NotBlank(message = "客户等级不能为空")
    private String grade;

    @ApiModelProperty(value = "开户银行")
    private String bank;

    @ApiModelProperty(value = "银行账号")
    private String account;

    @ApiModelProperty(value = "纳税号码")
    private String tax;

    @ApiModelProperty(value = "备注")
    private String data;

    @ApiModelProperty(value = "联系资料")
    private String contacts;

    @ApiModelProperty(value = "应收款余额")
    private Double balance ;

    @ApiModelProperty(value = "扩展信息")
    private String more;
}
