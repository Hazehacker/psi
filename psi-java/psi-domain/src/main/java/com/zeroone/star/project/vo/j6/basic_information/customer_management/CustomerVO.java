package com.zeroone.star.project.vo.j6.basic_information.customer_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author cmanPro
 * 返回信息对象
 */
@Data
@ApiModel("返回前端")
public class CustomerVO {
    @ApiModelProperty(value = "客户名称", required = true)
    @NotBlank(message = "客户名称不能为空")
    private String name;

    @NotNull(message = "客户编号不能为空")
    @ApiModelProperty(value = "客户编号", required = true)
    private String number;

    @ApiModelProperty(value = "所属组织", example = "1")
    private Integer frame;

    @ApiModelProperty(value = "所属用户", example = "1")
    private Integer user;

    @ApiModelProperty(value = "客户类别", example = "常规类别")
    private String category;

    @ApiModelProperty(value = "客户等级", example = "会员")
    private String grade;

    @ApiModelProperty(value = "开户银行")
    private String bank;

    @ApiModelProperty(value = "银行账号")
    private String account;

    @ApiModelProperty(value = "纳税号码")
    private String tax;

    @ApiModelProperty(value = "备注")
    private String data;

    @ApiModelProperty(value = "应收款余额")
    private Double balance ;


}
