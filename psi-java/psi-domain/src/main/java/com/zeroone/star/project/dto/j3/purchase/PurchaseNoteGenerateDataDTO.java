package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @Author 唐怀瑟
 * @Data 2025/10/19 20:22
 * @PackageName: com.zeroone.star.project.dto.j3.purchase
 * @CLASSNAME: GeneratePurchaseDataDTO
 * @Description: 获取生成采购单数据
 * @Version 1.0
 */

@Data
@ApiModel("生成采购单数据对象")
public class PurchaseNoteGenerateDataDTO {
    @ApiModelProperty(value = "商品编号", example = "123456")
    @NotBlank(message = "商品编号不能为空")
    @Size(max = 32, message = "商品编号长度不能超过32个字符")
    private String goods;
    @ApiModelProperty(value = "商品名称", example = "小刀")
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 32, message = "商品名称长度不能超过32个字符")
    private String name;
    @ApiModelProperty(value = "规格型号", example = "qqq")
    @Size(max = 32, message = "规格型号长度不能超过32个字符")
    private String spec;
    @ApiModelProperty(value = "辅助属性", example = "红色")
    @Size(max = 64, message = "辅助属性长度不能超过64个字符")
    private String attr;
    @ApiModelProperty(value = "单位", example = "kg")
    @NotBlank(message = "单位不能为空")
    @Size(max = 32, message = "单位长度不能超过32个字符")
    private String unit;
    @ApiModelProperty(value = "仓库", example = "一仓")
    @Size(max = 32, message = "仓库长度不能超过32个字符")
    private String warehouse;
    @ApiModelProperty(value = "批次号",example = "66")
    @Size(max = 32, message = "批次号长度不超过32个字符")
    private String batch;
    @ApiModelProperty(value = "单价", example = "13.5")
    @NotBlank(message = "单价不能为空")
    @Digits(integer = 12, fraction = 4, message = "单价整数部分最多12位，小数部分最多4位")
    private String price;
    @ApiModelProperty(value = "数量", example = "1")
    @NotBlank(message = "数量不能为空")
    @Digits(integer = 12, fraction = 4, message = "数量整数部分最多12位，小数部分最多4位")
    private String nums;
    @ApiModelProperty(value = "金额", example = "13.5")
    @NotBlank(message = "金额不能为空")
    @Digits(integer = 12, fraction = 4, message = "金额整数部分最多12位，小数部分最多4位")
    private String amount;
    @ApiModelProperty(value = "税率", example = "0")
    @NotBlank(message = "税率不能为空")
    @Digits(integer = 5, fraction = 2, message = "税率整数部分最多5位，小数部分最多2位")
    private String tax;
    @ApiModelProperty(value = "税额", example = "0")
    @NotBlank(message = "税额不能为空")
    @Digits(integer = 12, fraction = 4, message = "税额整数部分最多12位，小数部分最多4位")
    private String tat;
    @ApiModelProperty(value = "价税合计", example = "13.5")
    @NotBlank(message = "价税合计不能为空")
    @Digits(integer = 12, fraction = 4, message = "价税合计整数部分最多12位，小数部分最多4位")
    private String tpt;
    @ApiModelProperty(value = "备注信息", example = "六百六十六")
    @Size(max = 256, message = "备注信息长度不能超过256个字符")
    private String data;
    @ApiModelProperty(value = "生成采购单其他部分展示信息", example = "略")
    private PurchaseNoteOtherGenerateDataDTO purchaseNoteOtherGenerateDataDTO;


}
