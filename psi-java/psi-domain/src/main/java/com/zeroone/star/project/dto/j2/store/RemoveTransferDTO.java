package com.zeroone.star.project.dto.j2.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("RemoveTransferDTO")
public class RemoveTransferDTO {
    @ApiModelProperty(value = "调拨单ID列表", required = true, example = "[1,2,3]")
    private List<Integer> ids;
}
