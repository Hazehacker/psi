package com.zeroone.star.project.query.j6.basic_information.customer_management;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("查询客户对象")
public class CustomerQuery extends PageQuery {

    private String id;

    private String name;

    @ApiModelProperty(value = "客户编号", required = true)
    private String number;

    @ApiModelProperty(value = "所属组织", example = "1")
    private String frame;

    @ApiModelProperty(value = "所属用户", example = "1")
    private String user;

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

    @ApiModelProperty(value = "联系人")
    private String contactPerson;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty(value = "备注")
    private String data;


}
