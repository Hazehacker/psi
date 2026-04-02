package com.zeroone.star.homepage.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户
 * </p>
 *
 * @author Duuuuuu
 * @since 2025-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("customer")
@ApiModel(value="Customer对象", description="客户")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "客户名称")
    private String name;

    @ApiModelProperty(value = "拼音信息")
    private String py;

    @ApiModelProperty(value = "客户编号")
    private String number;

    @ApiModelProperty(value = "所属组织")
    private Integer frame;

    @ApiModelProperty(value = "所属用户")
    private Integer user;

    @ApiModelProperty(value = "客户类别")
    private String category;

    @ApiModelProperty(value = "客户等级")
    private String grade;

    @ApiModelProperty(value = "开户银行")
    private String bank;

    @ApiModelProperty(value = "银行账号")
    private String account;

    @ApiModelProperty(value = "纳税号码")
    private String tax;

    @ApiModelProperty(value = "备注信息")
    private String data;

    @ApiModelProperty(value = "联系资料")
    private String contacts;

    @ApiModelProperty(value = "应收款余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "扩展信息")
    private String more;


}
