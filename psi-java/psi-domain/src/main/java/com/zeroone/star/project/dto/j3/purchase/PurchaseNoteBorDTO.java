package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
@ApiModel(description = "采购单主表信息（关联buy表）")
public class PurchaseNoteBorDTO {
    @ApiModelProperty(value = "关联上游单据ID", example = "8")
    private String source;
    @ApiModelProperty(value = "供应商ID", example = "1")
    private String supplier;
    @ApiModelProperty(value = "采购单总金额", example = "2")
    private BigDecimal total;
}