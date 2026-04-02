package com.zeroone.star.project.dto.j4.fund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("收款单列表数据对象")
public class FundReceiptListDTO {
    @ApiModelProperty(value = "收款单id", example = "1")
    private String id;
    @ApiModelProperty(value = "所属组织名称", example = "01星球")
    private String frame;
    @ApiModelProperty(value = "客户名称", example = "阿伟")
    private String customer;
    @ApiModelProperty(value = "单据时间",example = "2020-01-01 22:00:00")
    private LocalDateTime time;
    @ApiModelProperty(value = "单据编号", example = "001")
    private String number;
    @ApiModelProperty(value = "单据金额", example = "100")
    private BigDecimal total;
    @ApiModelProperty(value = "核销金额", example = "100")
    private BigDecimal money;
    @ApiModelProperty(value = "关联人员名称", example = "阿伟")
    private String people;
    @ApiModelProperty(value = "审核状态", example = "0")
    private Integer examine;
    @ApiModelProperty(value = "核销状态", example = "0")
    private Integer nucleus;
    @ApiModelProperty(value = "制单人名称", example = "阿伟")
    private String user;
    @ApiModelProperty(value = "备注信息", example = "无")
    private String data;
}
