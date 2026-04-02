package com.zeroone.star.project.dto.j6.payment_order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("主表和账单关联DTO")
public class OmyWithBillDTO {
    @ApiModelProperty("主表信息")
    private OmyDTO omy;

    @ApiModelProperty("关联账单列表")
    private OmyBillDTO bill;
}
