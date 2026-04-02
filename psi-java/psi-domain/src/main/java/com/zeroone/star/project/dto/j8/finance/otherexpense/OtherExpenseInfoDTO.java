package com.zeroone.star.project.dto.j8.finance.otherexpense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 其他支出单详情信息
 * <p>
 * 涉及数据库表：<b>oce_info,iet</b>
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("其他支出单详情信息")
public class OtherExpenseInfoDTO {

    @ApiModelProperty(value = "支出单详情信息ID", example = "info01")
    private String id;

    @ApiModelProperty(value = "支出类别", example = "生活")
    private String ietName;

    @ApiModelProperty(value = "结算金额", example = "1234.56")
    private BigDecimal money;

    @ApiModelProperty(value = "备注信息", example = "备注test")
    private String data;
}
