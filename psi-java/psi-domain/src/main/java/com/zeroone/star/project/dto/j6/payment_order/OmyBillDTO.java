package com.zeroone.star.project.dto.j6.payment_order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@ApiModel("账单表DTO")
public class OmyBillDTO {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty(value = "核销金额",example = "0")
    private BigDecimal money;

    @NotBlank(message="所属单据不能为空")
    @ApiModelProperty("所属单据")
    private String pid;

    /*@NotBlank(message="核销类型不能为空")
    @ApiModelProperty(value = "核销类型",example = "预收")
    private Integer type;

    @NotBlank(message="关联单据不能为空")
    @ApiModelProperty("关联单据")
    private Integer source;

    @NotBlank(message="关联时间不能为空")
    @ApiModelProperty(value = "单据时间",example = "2025-04-27")
    private Integer time;*/


}
