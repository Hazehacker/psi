package com.zeroone.star.project.dto.j4.fund;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 审核/反审核收款单DTO（支持批量选择单据）
 */
@Data
public class ReceiptAuditDTO {
    @ApiModelProperty(value = "审核单号",example = "1")
    private List<String> ids;
}
