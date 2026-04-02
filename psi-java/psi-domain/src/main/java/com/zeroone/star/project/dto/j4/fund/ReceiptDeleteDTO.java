package com.zeroone.star.project.dto.j4.fund;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 删除收款单DTO（支持批量删除单据）
 */
@Data
public class ReceiptDeleteDTO {
    @ApiModelProperty(value = "收款单id列表",example = "1")
    private List<String> receiptIds;
}
