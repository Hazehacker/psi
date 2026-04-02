package com.zeroone.star.project.query.j6.basic_information.fund_account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

/**
 * <p>
 * 描述: 条件查询资金账户
 * </p>
 *
 * @author goldenHour
 * @version 1.0.0
 */
@Data
@ApiModel(description = "条件查询资金账户")
public class AccountQuery {

    @ApiModelProperty("资金账户名称")
    private String name;

    @ApiModelProperty("资金账户编号")
    private String number;

    @ApiModelProperty("备注信息")
    private String data;



}
