package com.zeroone.star.sale.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("销售订单CSV导入结果")
public class SaleOrderImportResult {
    @ApiModelProperty("成功导入的订单编号列表")
    private List<String> successOrderNumbers;
    @ApiModelProperty("失败的订单编号列表")
    private List<String> failOrderNumbers;
    @ApiModelProperty("成功数量")
    private int successCount;
    @ApiModelProperty("失败数量")
    private int failCount;
    @ApiModelProperty("失败原因")
    private String failReason;
}