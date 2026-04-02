package com.zeroone.star.project.dto.j8.finance.otherincome;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@ApiModel("新增其他收入单核销详情")
public class AddOtherIncomeBillDTO {
    @NotBlank(message = "核销类型不能为空")
    @ApiModelProperty(value = "核销类型")
    private String type;

    @NotBlank(message = "关联单据不能为空")
    @ApiModelProperty(value = "关联单据")
    private String source;

    @NotNull(message = "单据时间不能为空")
    @PastOrPresent(message = "单据时间不能是未来时间")
    @ApiModelProperty(value = "单据时间")
    private LocalDateTime time;

    @NotNull(message = "核销金额不能为空")
    @DecimalMin(value = "0.0", message = "核销金额不能为负数")
    @Digits(integer = 12, fraction = 4, message = "核销金额格式不正确")
    @ApiModelProperty(value ="核销金额" )
    private BigDecimal money;

}
