package com.zeroone.star.project.dto.j3.purchase;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author jingxu
 * @version 1.0.0
 * @since 2025/10/31
 */
@Data
@ApiModel(value = "核对和反核对DTO")
public class CheckOutReturnGoodsDTO {
    @NotBlank
    @ApiModelProperty(value = "核对或是反核对,对一批订单执行核对是0 对一批订单执行反核对是1")
    int type;
    @NotBlank
    @ApiModelProperty(value = "反核对的订单id列表")
    String[] pids;
}
