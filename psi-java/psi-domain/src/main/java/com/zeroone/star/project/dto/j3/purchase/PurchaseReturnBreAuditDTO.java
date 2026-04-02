package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: 斗气化码
 * @CreateTime: 2025-10-23
 * @Description: 采购退货单审核/反审核DTO
 * @Version: 1.0
 */
@ApiModel("采购退货单审核DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseReturnBreAuditDTO {

    @ApiModelProperty(value = "审核类型[1:审核|0:反审核]", example = "1")
    @NotEmpty(message = "审核类型不能为空")
    private int type;

    @ApiModelProperty(value = "采购退货ID列表", required = true, example = "[\"1\", \"2\", \"3\"]")
    @NotEmpty(message = "采购退货单ID列表不能为空")
    private List<String> ids;
}
