package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @Author 唐怀瑟
 * @Data 2025/10/31 9:24
 * @PackageName: com.zeroone.star.project.dto.j3.purchase
 * @CLASSNAME: PurchaseNoteOtherGenerateDataDTO
 * @Description: 获取生成采购单数据
 * @Version 1.0
 */
@Data
@ApiModel("生成采购单数据部分对象")
public class PurchaseNoteOtherGenerateDataDTO {
    //下方展示
    @ApiModelProperty(value = "单据金额", example = "0.0000")
    @NotBlank(message = "单据金额不能为空")
    @Digits(integer = 16, fraction = 4, message = "单据金额整数部分最多16位，小数部分最多4位")
    private String total;
    @ApiModelProperty(value = "实际金额", example = "2.0000")
    @NotBlank(message = "实际金额不能为空")
    @Digits(integer = 16, fraction = 4, message = "实际金额整数部分最多16位，小数部分最多4位")
    private String actual;
    @ApiModelProperty(value = "实付金额", example = "2.0000")
    @NotBlank(message = "实付金额不能为空")
    @Digits(integer = 16, fraction = 4, message = "实付金额整数部分最多16位，小数部分最多4位")
    private String money;
    @ApiModelProperty(value = "单据费用", example = "0.0000")
    @NotBlank(message = "单据费用不能为空")
    @Digits(integer = 16, fraction = 4, message = "单据费用整数部分最多16位，小数部分最多4位")
    private String cost;
    @ApiModelProperty(value = "结算账户", example = "1234567890")
    @Size(max = 32, message = "结算账户长度不能超过32个字符")
    private String account;
    @ApiModelProperty(value = "关联人员", example = "")
    @Size(max = 32, message = "关联人员长度不能超过32个字符")
    private String people;
    @ApiModelProperty(value = "物流信息", example = "请输入物流单号")
    private String logistics;
    @ApiModelProperty(value = "单据附件", example = "略")
    private String file;
    @ApiModelProperty(value = "备注信息", example = "请输入备注信息")
    @Size(max = 256, message = "备注信息长度不能超过256个字符")
    private String data;

    //上方展示
    @ApiModelProperty(value = "供应商", example = "起床有限公司")
    @Size(max = 32, message = "供应商长度不超过32个字符")
    @NotBlank(message = "供应商不能为空")
    private String supplier;
    @ApiModelProperty(value = "单据日期", example = "2025-10-30")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotBlank(message = "单据日期不能为空")
    private String time;
    @ApiModelProperty(value = "单据编号", example = "CGDD2510301931579")
    @Size(max = 32, message = "单据编号长度不超过32个字符")
    @NotBlank(message = "单据编号不能为空")
    private String number;
}

