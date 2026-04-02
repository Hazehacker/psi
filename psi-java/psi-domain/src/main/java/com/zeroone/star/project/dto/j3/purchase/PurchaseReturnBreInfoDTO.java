package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: 斗气化码
 * @CreateTime: 2025-10-20
 * @Description: 采购退货单详情参数
 * @Version: 1.0
 */
@Data
@ApiModel("采购退货单详情参数")
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseReturnBreInfoDTO {
    @ApiModelProperty(value = "主键", example = "A000001")
    private String id;
    @ApiModelProperty(value = "所属ID", example = "G000001")
    private String pid;
    @ApiModelProperty(value = "关联详情|BUY")
    private String source;
    @ApiModelProperty(value = "商品名称", example = "蓝牙耳机")
    private String goods;
    @ApiModelProperty(value = "辅助属性", example = "降噪")
    private String attr;
    @ApiModelProperty(value = "单位", example = "对")
    private String unit;
    @ApiModelProperty(value = "仓库", example = "一号仓")
    private String warehouse;
    @ApiModelProperty(value = "批次号", example = "XM2025")
    private String batch;
    @ApiModelProperty(value = "生产日期", example = "2024-10-01")
    private LocalDateTime mfd;
    @ApiModelProperty(value = "单价", example = "3999.00")
    private double price;
    @ApiModelProperty(value = "数量", example = "10")
    private double nums;
    @ApiModelProperty(value = "序列号", example = "A1")
    private String serial;
    @ApiModelProperty(value = "折扣率（%）", example = "20")
    private double discount;
    @ApiModelProperty(value = "折扣额", example = "800.00")
    private double dsc;
    @ApiModelProperty(value = "金额", example = "39990.00")
    private double total;
    @ApiModelProperty(value = "税率（%）", example = "2")
    private double tax;
    @ApiModelProperty(value = "税额", example = "799.80")
    private double tat;
    @ApiModelProperty(value = "价税合计", example = "12")
    private double tpt;
    @ApiModelProperty(value = "备注信息", example = "单据导入")
    private String data;
}
