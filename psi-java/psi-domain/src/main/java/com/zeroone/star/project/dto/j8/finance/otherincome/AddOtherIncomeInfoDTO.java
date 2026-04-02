package com.zeroone.star.project.dto.j8.finance.otherincome;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 新增其他收入单详情信息 (单条明细)
 * 涉及数据表: ice_info
 */
@Data
@ApiModel("新增其他收入单详情信息")
public class AddOtherIncomeInfoDTO {
    @NotBlank(message = "明细中的收支类型不能为空")
    @ApiModelProperty(value = "收支类型", example = "INC001", required = true)
    private String iet;


    @NotNull(message = "明细中的金额不能为空")
    @DecimalMin(value = "0.0", message = "明细金额不能为负数")
    @Digits(integer = 12, fraction = 4, message = "明细金额格式不正确")
    @ApiModelProperty(value = "结算金额", example = "8888.88", required = true)
    private BigDecimal money;

    @ApiModelProperty(value = "备注信息", example = "项目款")
    private String data;
}
