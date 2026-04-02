package com.zeroone.star.project.dto.j8.finance.otherexpense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 其他收入单核销详情
 * 涉及数据表: ice_bill
 */
@Data
@ApiModel("其他收入单核销详情")
public class OtherIncomeBillDTO {
    @ApiModelProperty(value = "收入单核销详情ID", example = "bill01")
    private String id;
    @ApiModelProperty(value = "收入类别", example = "服务费")
    private String name;
    @ApiModelProperty(value = "核销类型", example = "y1")
    private String type;
    @ApiModelProperty(value = "关联单据编号", example = "XSD20251022001")
    private String source;
    @ApiModelProperty(value = "单据时间", example = "2025-10-22 11:40:00")
    private String time;
    @ApiModelProperty(value = "核销金额", example = "8000.00")
    private BigDecimal money;
}
