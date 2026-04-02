package com.zeroone.star.project.dto.j6.payment_order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@ApiModel("信息表DTO")
public class OmyInfoDTO {

    @NotBlank(message="id不能为空")
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty(value = "结算账户",example = "中创资金")
    private String account;

    @NotBlank(message="结算金额不能为空")
    @ApiModelProperty(value = "结算金额",example = "1000")
    private BigDecimal money;

    @NotBlank(message="结算号不能为空")
    @ApiModelProperty(value = "结算号",example = "S111")
    private String settle;

    @NotBlank(message="备注信息不能为空")
    @ApiModelProperty(value = "备注信息",example = "新增付款单示例")
    private String data1;

    /*@NotBlank(message="所属组织不能为空")
    @ApiModelProperty(value = "所属组织",example = "默认组织")
    private Integer pid;*/

}
