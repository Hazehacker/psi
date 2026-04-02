package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName: PurchaseBuyInfoDTO
 * @Description:采购单详情DTO
 * @Author: 正念
 * @Date: 2025/10/30 11:27
 */
@Data
@ApiModel("采购单详情DTO")
public class PurchaseBuyInfoDTO {
    @ApiModelProperty("明细ID（修改时必传）")
    private String id;

    @ApiModelProperty("所属采购单id")
    private String pid;

    @ApiModelProperty(value = "商品ID", required = true)
    private String goods;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty("仓库")
    private String warehouse;

    @ApiModelProperty("单价")
    private BigDecimal price;

    @ApiModelProperty("数量")
    private BigDecimal nums;

    @ApiModelProperty("折扣率")
    private BigDecimal discount;

    @ApiModelProperty("税率")
    private BigDecimal tax;

    @ApiModelProperty("备注信息")
    private String data;

    @ApiModelProperty("退货数量")
    private BigDecimal retreat;

}
