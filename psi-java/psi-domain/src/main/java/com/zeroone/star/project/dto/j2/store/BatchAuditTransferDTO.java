package com.zeroone.star.project.dto.j2.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("BatchAuditTransferDTO")
public class BatchAuditTransferDTO {

    @ApiModelProperty(value = "调拨单ID列表", required = true, example = "[1,2,3]")
    private List<String> ids;

    @ApiModelProperty(value = "操作类型（0-反审核，1-审核）", required = true, example = "1")
    private Integer operation;
}
