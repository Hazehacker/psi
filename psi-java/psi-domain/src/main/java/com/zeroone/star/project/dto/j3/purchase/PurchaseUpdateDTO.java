package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: 景旭
 * @CreateTime: 2025-10-18
 * @Description: 采购退货单 DTO对象
 * @Version: 1.0
 */
@ApiModel("更新退货订单-参数列表")
@Data
public class PurchaseUpdateDTO {
    @NotBlank
    @ApiModelProperty(value = "所属订单的 Id")
    private String pid;

    @NotBlank
    @ApiModelProperty(value = "供应商")
    private String supplier;

    @ApiModelProperty(value = "单据日期")
    private String time;

    @ApiModelProperty(value = "单据编号")
    private String number;

    @PositiveOrZero
    @ApiModelProperty(value = "单据金额")
    /*对应数据库bre表的 total */
    private BigDecimal total_money;

    @NotNull
    @PositiveOrZero
    @ApiModelProperty(value = "实际金额")
    private BigDecimal actual;

    @NotNull
    @PositiveOrZero
    @ApiModelProperty(value = "实收金额")
    private BigDecimal money;

    @PositiveOrZero
    @ApiModelProperty(value = "单据费用")
    private BigDecimal cost;

    @NotBlank
    @ApiModelProperty(value = "结算账户")
    private String account;

    @ApiModelProperty(value = "关联人员")
    private String people;

    @ApiModelProperty(value = "物流信息")
    private String logistics;

    @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$", message = "附件URL格式不正确")
    @ApiModelProperty(value = "单据附件")
    private String file;

    @Size(max = 256)
    @ApiModelProperty(value = "备注信息")
    private String data;

    @ApiModelProperty(value = "退货商品列表")
    List<PurchaseReturnItemsDTO> returnItems;
}
