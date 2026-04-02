package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author TWTW
 * @date 2025/10/24
 * @description 采购单数据传输对象，用于采购单相关的参数传递与响应
 * Version: 2.0
 */
@Data
@ApiModel(description = "采购单数据传输对象，用于采购单相关的参数传递与响应")
public class PurchaseNoteAddDTO {

    @ApiModelProperty(value = "采购单主表信息", required = true)
    @NotNull(message = "采购单主表信息不能为空")
    @Valid
    private PurchaseNoteBuyDTO purchaseNoteBuyDTO;

    @ApiModelProperty(value = "采购单明细列表（至少包含一条明细）", required = true)
    @NotNull(message = "采购单明细列表不能为空")
    @Size(min = 1, message = "采购单明细列表至少包含一条明细") // 确保列表至少有1条数据
    @Valid
    private List<PurchaseNoteInfoDTO> purchaseNoteInfoDTOS;
}