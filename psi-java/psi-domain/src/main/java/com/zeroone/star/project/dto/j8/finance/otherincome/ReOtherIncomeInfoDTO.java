package com.zeroone.star.project.dto.j8.finance.otherincome;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 修改其他收入单详情信息 (单条明细)
 */
@Data
@ApiModel("修改其他收入单详情信息")
public class ReOtherIncomeInfoDTO {
    @ApiModelProperty(value = "所属项目", example = "咨询服务费", required = true)
    private String source;
    @ApiModelProperty(value = "收支类型", example = "INC001", required = true)
    private String iet;
    @ApiModelProperty(value = "结算金额", example = "8888.88", required = true)
    private BigDecimal money;
    @ApiModelProperty(value = "备注信息", example = "项目款")
    private String data;
}