package com.zeroone.star.project.dto.j8.finance.otherincome;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收支类别
 * 涉及数据表: iet
 */
@Data
@ApiModel("收支类别")
@JsonPropertyOrder({"uuid","name","type","sort","data"})
public class IncomeExpendTypeDTO {
    @ApiModelProperty(value = "收支类型id")
    @JsonProperty("uuid")
    private String id;

    @ApiModelProperty(value = "收支类别名称")
    private String name;

    @ApiModelProperty(value = "收支类型")
    private Integer type;

    @ApiModelProperty(value = "类别排序")
    private Integer sort;

    @ApiModelProperty(value = "备注信息")
    private String data;
}
