package com.zeroone.star.project.dto.j8.finance.otherincome;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 其他收入单核销记录
 * 涉及数据表: ice,ice_bill
 */
@Data
@ApiModel("其他收入单核销记录")
@JsonPropertyOrder({"uuid","type", "money", "source", "money", "time","pid" })
public class OtherIncomeBillDTO {
    @ApiModelProperty(value = "核销记录ID")
    @JsonProperty("uuid")
    private String id;
    @ApiModelProperty(value = "所属单据")
    private String pid;
    @ApiModelProperty(value = "核销类型")
    private String type;
    @ApiModelProperty(value = "关联单据")
    private String source;
    @ApiModelProperty(value = "核销金额")
    private BigDecimal money;
    @ApiModelProperty(value = "单据时间")
    private LocalDateTime time;

}
