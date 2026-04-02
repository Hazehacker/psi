package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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
public class PurchaseNoteGenerateAllDTO {
    @ApiModelProperty(value = "生成采购单列表部分展示信息", example = "略")
    private List<PurchaseNoteGenerateDataDTO> purchaseNoteGenerateDataDTO;
    @ApiModelProperty(value = "生成采购单其他部分展示信息", example = "略")
    private PurchaseNoteOtherGenerateDataDTO purchaseNoteOtherGenerateDataDTO;

    @ApiModelProperty(value = "响应状态", example = "success")
    @NotBlank(message = "响应状态不能为空")
    private String state;

    @ApiModelProperty(value = "源单主键", example = "220")
    @NotBlank(message = "源单主键不能为空")
    @Size(max = 32, message = "源单主键长度不能超过32个字符")
    private String source;

    @ApiModelProperty(value = "供应商主键", example = "9")
    @NotBlank(message = "供应商主键不能为空")
    @Size(max = 32, message = "供应商主键长度不能超过32个字符")
    private String supplier;

    @ApiModelProperty(value = "整单金额(元)", example = "123")
    @NotBlank(message = "整单金额不能为空")
    @Digits(integer = 16, fraction = 4, message = "整单金额整数最多16位，小数最多4位")
    private String total;

    @ApiModelProperty(value = "行主键", example = "559")
    @NotBlank(message = "行主键不能为空")
    @Size(max = 32, message = "行主键长度不能超过32个字符")
    private String id;

    @ApiModelProperty(value = "源单主键(bor.id)", example = "220")
    @NotBlank(message = "源单主键不能为空")
    @Size(max = 32, message = "源单主键长度不能超过32个字符")
    private String pid;

    @ApiModelProperty(value = "商品主键", example = "92")
    @NotBlank(message = "商品主键不能为空")
    @Size(max = 32, message = "商品主键长度不能超过32个字符")
    private String goods;

    @ApiModelProperty(value = "仓库主键", example = "18")
    private String warehouse;

    @ApiModelProperty(value = "折扣率(%)", example = "0")
    @NotBlank(message = "折扣率不能为空")
    @Digits(integer = 5, fraction = 2, message = "折扣率整数最多5位，小数最多2位")
    private String discount;

    @ApiModelProperty(value = "折扣额", example = "0")
    @NotBlank(message = "折扣额不能为空")
    @Digits(integer = 12, fraction = 4, message = "折扣额整数最多12位，小数最多4位")
    private String dsc;

    @ApiModelProperty(value = "已入库数量", example = "0")
    @NotBlank(message = "已入库数量不能为空")
    @Digits(integer = 12, fraction = 4, message = "入库数量整数最多12位，小数最多4位")
    private String handle;

    @ApiModelProperty(value = "退货数量", example = "0")
    @NotBlank(message = "退货数量不能为空")
    @Digits(integer = 12, fraction = 4, message = "退货数量整数最多12位，小数最多4位")
    private String retreat;

    @ApiModelProperty(value = "序列号数组", example = "[]")
    private List<String> serial;

    @ApiModelProperty(value = "生产日期", example = "")
    private String mfd;

    // 嵌套快照对象
    @ApiModelProperty(value = "商品快照")
    private GoodsSnapshotVO goodsData;

    @ApiModelProperty(value = "仓库快照")
    private WarehouseSnapshotVO warehouseData;


    @Data
    public static class GoodsSnapshotVO {
        @ApiModelProperty(value = "商品主键", example = "92")
        @NotBlank(message = "商品主键不能为空")
        @Size(max = 32, message = "商品主键长度不能超过32个字符")
        private String id;

        @ApiModelProperty(value = "商品名称", example = "ssd213")
        @NotBlank(message = "商品名称不能为空")
        @Size(max = 32, message = "商品名称长度不能超过32个字符")
        private String name;

        @ApiModelProperty(value = "拼音", example = "ssd213")
        private String py;

        @ApiModelProperty(value = "商品编号", example = "112222")
        @NotBlank(message = "商品编号不能为空")
        @Size(max = 32, message = "商品编号长度不能超过32个字符")
        private String number;

        @ApiModelProperty(value = "规格型号", example = "")
        private String spec;

        @ApiModelProperty(value = "类别主键", example = "0")
        @NotBlank(message = "类别主键不能为空")
        @Size(max = 32, message = "类别主键长度不能超过32个字符")
        private String category;

        @ApiModelProperty(value = "品牌", example = "")
        private String brand;

        @ApiModelProperty(value = "单位", example = "kg")
        @NotBlank(message = "单位不能为空")
        @Size(max = 32, message = "单位长度不能超过32个字符")
        private String unit;

        @ApiModelProperty(value = "采购价", example = "99999999.9999")
        @NotBlank(message = "采购价不能为空")
        @Digits(integer = 12, fraction = 4, message = "采购价整数最多12位，小数最多4位")
        private String buy;

        @ApiModelProperty(value = "销售价", example = "123123")
        @NotBlank(message = "销售价不能为空")
        @Digits(integer = 12, fraction = 4, message = "销售价整数最多12位，小数最多4位")
        private String sell;

        @ApiModelProperty(value = "条码", example = "")
        private String code;

        @ApiModelProperty(value = "货位", example = "")
        private String location;

        @ApiModelProperty(value = "库存阈值", example = "30")
        @NotBlank(message = "库存阈值不能为空")
        @Digits(integer = 12, fraction = 4, message = "库存阈值整数最多12位，小数最多4位")
        private String stock;

        @ApiModelProperty(value = "产品类型 0常规 1服务", example = "0")
        @NotBlank(message = "产品类型不能为空")
        @Size(max = 1, message = "产品类型长度必须为1")
        private String type;

        @ApiModelProperty(value = "备注", example = "")
        private String data;

        @ApiModelProperty(value = "是否序列产品", example = "true")
        @NotNull(message = "序列产品标识不能为空")
        private Boolean serial;

        @ApiModelProperty(value = "是否批次产品", example = "true")
        @NotNull(message = "批次产品标识不能为空")
        private Boolean batch;

        @ApiModelProperty(value = "是否有效期", example = "false")
        @NotNull(message = "有效期标识不能为空")
        private Boolean validity;

        @ApiModelProperty(value = "保质期(天)", example = "0")
        @NotBlank(message = "保质期不能为空")
        @Digits(integer = 5, fraction = 0, message = "保质期最多5位整数")
        private String protect;

        @ApiModelProperty(value = "预警阈值", example = "0")
        @NotBlank(message = "预警阈值不能为空")
        @Digits(integer = 5, fraction = 0, message = "预警阈值最多5位整数")
        private String threshold;
    }

    @Data
    public static class WarehouseSnapshotVO {
        @ApiModelProperty(value = "仓库主键", example = "18")
        @NotBlank(message = "仓库主键不能为空")
        @Size(max = 32, message = "仓库主键长度不能超过32个字符")
        private String id;

        @ApiModelProperty(value = "仓库名称", example = "一仓")
        @NotBlank(message = "仓库名称不能为空")
        @Size(max = 32, message = "仓库名称长度不能超过32个字符")
        private String name;

        @ApiModelProperty(value = "仓库编号", example = "02")
        @NotBlank(message = "仓库编号不能为空")
        @Size(max = 32, message = "仓库编号长度不能超过32个字符")
        private String number;

        @ApiModelProperty(value = "所属组织", example = "15")
        @NotBlank(message = "所属组织不能为空")
        @Size(max = 32, message = "所属组织长度不能超过32个字符")
        private String frame;

        @ApiModelProperty(value = "联系人", example = "")
        private String contacts;

        @ApiModelProperty(value = "联系电话", example = "")
        private String tel;

        @ApiModelProperty(value = "地址", example = "")
        private String add;

        @ApiModelProperty(value = "备注", example = "")
        private String data;
    }
}
