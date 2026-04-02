package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;

/**
 * @Author 唐怀瑟
 * @Data 2025/10/19 16:16
 * @PackageName: com.zeroone.star.project.dto.j3.purchase
 * @CLASSNAME: PurchaseOrderListDTO
 * @Description: 获取采购订单列表
 * @Version 1.0
 */

@Data
@ApiModel("采购订单列表数据对象")
public class PurchaseOrderListDTO {
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 32, message = "商品名称长度不能超过32个字符")
    @ApiModelProperty(value = "商品名称", example = "小刀")
    private String name;
    @ApiModelProperty(value = "所属组织", example = "默认组织")
    @NotBlank(message = "所属组织不能为空")
    @Size(max = 32, message = "所属组织长度不能超过32个字符")
    private String frame;
    @ApiModelProperty(value = "单据编号", example = "CGDD2510191455258")
    @NotBlank(message = "单据编号不能为空")
    @Size(max = 32, message = "单据编号长度不能超过32个字符")
    private String number;
    @ApiModelProperty(value = "供应商", example = "郑州东方之花医药股份有限公司")
    @NotBlank(message = "供应商不能为空")
    @Size(max = 32, message = "供应商长度不能超过32个字符")
    private String supplier;
    @ApiModelProperty(value = "关联人员", example = "jack")
    @Size(max = 32, message = "关联人员长度不能超过32个字符")
    private String people;
    @ApiModelProperty(value = "单据时间", example = "2025-10-18")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotBlank(message = "单据时间不能为空")
    private String documentTime;
    @ApiModelProperty(value = "到货日期", example = "2025-10-19")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String arrivalTime;
    @ApiModelProperty(value = "制单人", example = "管理员")
    @NotBlank(message = "制单人不能为空")
    @Size(max = 32, message = "制单人长度不能超过32个字符")
    private String user;
    @ApiModelProperty(value = "审核状态", example = "已审核")
    @NotNull(message = "审核状态不能为空")
    private String examine;
    @ApiModelProperty(value = "入库状态", example = "已入库")
    @NotNull(message = "入库状态不能为空")
    private String state;
    @ApiModelProperty(value = "备注信息", example = "略")
    @Size(max = 256, message = "备注信息长度不能超过256个字符")
    private String data;
    @ApiModelProperty(value = "单据金额", example = "10000")
    @NotBlank(message = "单据金额不能为空")
    @Digits(integer = 12, fraction = 4, message = "单据金额整数部分最多12位，小数部分最多4位")
    private String total;
    @ApiModelProperty(value = "实际金额", example = "10000")
    @NotBlank(message = "实际金额不能为空")
    @Digits(integer = 12, fraction = 4, message = "实际金额整数部分最多12位，小数部分最多4位")
    private String actual;

    @ApiModelProperty(value = "采购订单id", example = "1985230309322948610")
    @NotEmpty(message = "采购订单id不能为空")
    @Size(max = 32, message = "采购订单id长度不能超过32个字符")
    private String id;
}
