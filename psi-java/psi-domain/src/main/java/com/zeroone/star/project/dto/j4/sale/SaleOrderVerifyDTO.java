package com.zeroone.star.project.dto.j4.sale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("销售订单审核请求对象")
public class SaleOrderVerifyDTO {

    @ApiModelProperty(value = "订单ID列表", example = "[\"SO1001\", \"SO1002\"]", required = true)
    private List<String> ids;

    @ApiModelProperty(value = "状态数字（1表示审核，0表示反审核）", example = "1", required = true)
    private Integer num;

    // Getter & Setter
    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}