package com.zeroone.star.project.dto.j6.basic_information.customer_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author cmanPro
 * @version 1.0
 * 描述：客户
 */

@Data
@ApiModel("客户")
public class CustomerDTO {


    @ApiModelProperty(value = "id", required = true)
    private String id;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String name;

    /**
     * 拼音信息
     */
    @ApiModelProperty(value = "拼音信息")
    private String py;

    /**
     * 客户编号
     */
    @ApiModelProperty(value = "客户编号")
    private String number;

    /**
     * 所属组织
     */
    @ApiModelProperty(value = "所属组织")
    private String frame;

    /**
     * 所属用户
     */
    @ApiModelProperty(value = "所属用户")
    private String user;

    /**
     * 客户类别
     */
    @ApiModelProperty(value = "客户类别")
    private String category;

    /**
     * 客户等级
     */
    @ApiModelProperty(value = "客户等级")
    private String grade;

    /**
     * 开户银行
     */
    @ApiModelProperty(value = "开户银行")
    private String bank;

    /**
     * 银行账号
     */
    @ApiModelProperty(value = "银行账号")
    private String account;

    /**
     * 纳税号码
     */
    @ApiModelProperty(value = "纳税号码")
    private String tax;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String data;

    /**
     * 联系资料
     */
    @ApiModelProperty(value = "联系资料")
    private String contacts;

    /**
     * 应付款余额
     */
    @ApiModelProperty(value = "应付款余额")
    private Double balance;

    /**
     * 扩展信息
     */
    @ApiModelProperty(value = "扩展信息")
    private String more;


}
