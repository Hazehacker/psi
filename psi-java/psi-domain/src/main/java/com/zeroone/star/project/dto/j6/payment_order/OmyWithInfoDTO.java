package com.zeroone.star.project.dto.j6.payment_order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel("主表和信息关联DTO")
public class OmyWithInfoDTO {
    @ApiModelProperty("主表信息")
    private OmyDTO omy;

    @ApiModelProperty("关联信息")
    private List<OmyInfoDTO> infoList;



}
