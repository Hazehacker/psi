package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel("审核/反审核（支持批量）")
public class PurchaseAuditDTO {
    @ApiModelProperty(value = "采购单ID", required = true)
    private List<String> ids;

    @ApiModelProperty("是否审核：1=审核 / 0=反审核")
    private int type;

}
