package com.zeroone.star.project.dto.j6.basic_information.fund_account;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 描述: 资金账户列表
 * </p>
 *
 * @author goldenHour
 * @version 1.0.0
 */

@ApiModel(description = "资金账户列表")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountListDTO {

    @ApiModelProperty(value = "资金账户ID",required = true)
    @NotEmpty(message = "资金账户ID不能为空")
    private String id;

    @ApiModelProperty("资金账户名称")
    private String name;

    @ApiModelProperty(value = "资金账户编号",example = "T0004")
    private String number;

    @ApiModelProperty("所属组织")
    private String frame;

    @NotNull(message = "时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "余额日期",example = "2023-01-01",required = true)
    private Date time;

    @ApiModelProperty("账户余额")
    private BigDecimal balance;

    @ApiModelProperty("备注信息")
    private String data;
}
