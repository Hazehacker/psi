package com.zeroone.star.project.dto.j4.sale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("退货单审核核对对象")
public class SaleReturnIdsDTO {
    @ApiModelProperty(value = "退货单id集合",required = true)
    private List<Integer> ids;
    @ApiModelProperty(value = "数字状态，1表示核对或审核,0表示反核对或反审核",required = true)
    private Integer status;
}
