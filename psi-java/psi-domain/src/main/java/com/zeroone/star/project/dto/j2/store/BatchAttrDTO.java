package com.zeroone.star.project.dto.j2.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("BatchAttrDTO")
public class BatchAttrDTO {
    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @ApiModelProperty(value = "库存数量")
    private BigDecimal attrStock;

    @ApiModelProperty(value = "批次列表")
    private List<BatchNumberDTO> batches;

    @ApiModelProperty(value = "商品ID", hidden = true)
    @JsonIgnore
    private String goodsId;
}
