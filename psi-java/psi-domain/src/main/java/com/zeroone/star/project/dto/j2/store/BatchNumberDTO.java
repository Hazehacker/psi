package com.zeroone.star.project.dto.j2.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("BatchNumberDTO")
public class BatchNumberDTO {

    @ApiModelProperty(value = "批次号")
    private String batchNumber;

    @ApiModelProperty(value = "库存数量")
    private BigDecimal totalStock;

    @ApiModelProperty(value = "是否预警")
    @JsonIgnore
    private Boolean isWarning;

    @ApiModelProperty(value = "批次详情列表")
    private List<BatchDocumentDTO> batchDocuments;

}
