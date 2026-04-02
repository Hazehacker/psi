package com.zeroone.star.project.dto.j8.finance.otherincome;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 其他收入单详情信息 (单条明细)
 * 涉及数据表: ice_info,ice_bill
 */
@Data
@ApiModel("其他收入单详情信息")
@JsonPropertyOrder({"data","uuid", "iet", "ietDate", "money", "pid" })
public class OtherIncomeInfoDTO {
    @ApiModelProperty(value = "收入单详情信息ID")
    @JsonProperty("uuid")
    private String id;
    @ApiModelProperty(value = "对应收入单的ID")
    private String pid;
    @ApiModelProperty(value = "收支类型")
    private String iet;
    @ApiModelProperty(value = "结算金额")
    private BigDecimal money;
    @ApiModelProperty(value = "备注信息")
    private String data;
    @ApiModelProperty(value = "收支类别")
    IncomeExpendTypeDTO ietData;
}
