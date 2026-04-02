package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel("核对/反核对(支持批量)")
public class PurchaseCheckDTO {
    @ApiModelProperty(value = "采购单ID", required = true)
    private List<String> ids;

    @ApiModelProperty(value = "是否核对：0=核对 / 1=反核对", required = true, example = "0")
    private int type;
}
