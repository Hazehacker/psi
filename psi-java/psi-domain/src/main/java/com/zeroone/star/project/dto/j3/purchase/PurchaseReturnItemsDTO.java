package com.zeroone.star.project.dto.j3.purchase;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author jingxu
 * @version 1.0.0
 * @since 2025/10/31
 */
@Data
public class PurchaseReturnItemsDTO {
    @NotBlank
    @ApiModelProperty(value = "所属订单的 Id")
    private String pid;

    @ApiModelProperty(value = "商品名称")
    private String goods;

    @ApiModelProperty(value = "辅助属性")
    private String attr;

    @ApiModelProperty(value = "单位")
    private String unit;

    @NotBlank
    @ApiModelProperty(value = "仓库")
    private String warehouse;

    @ApiModelProperty(value = "批次号")
    private String batch;

    @Past
    @ApiModelProperty(value = "生产日期")
    private String mfd;

    @PositiveOrZero
    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @PositiveOrZero
    @ApiModelProperty(value = "数量")
    private BigDecimal nums;

    @PositiveOrZero
    @ApiModelProperty(value = "金额")
    private BigDecimal total;

    @Size(max = 256)
    @ApiModelProperty(value = "备注信息")
    private String data;
}
