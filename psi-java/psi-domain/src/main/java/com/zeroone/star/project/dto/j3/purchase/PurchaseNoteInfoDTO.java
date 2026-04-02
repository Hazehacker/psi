package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 采购单主表数据传输对象（对应buy_info表）
 * @author: TWTW
 * @date: 2025/10/24
 */
@Data
@ApiModel(description = "采购单明细数据传输对象")
public class PurchaseNoteInfoDTO {

    //buy_info表
    @ApiModelProperty(value = "关联详情ID", required = true, example = "0")
    @NotNull(message = "关联详情ID不能为空")
    private String source;

    @ApiModelProperty(value = "所属商品ID", required = true, example = "3")
    @NotNull(message = "所属商品ID不能为空")
    private String goods;

    @ApiModelProperty(value = "辅助属性", required = false, example = "衣服纯白色")
    @NotNull(message = "辅助属性不能为空") // 允许空字符串，但不允许null
    private String attr;

    @ApiModelProperty(value = "商品单位", required = true, example = "个")
    @NotBlank(message = "商品单位不能为空") // 不允许null或空白字符串
    private String unit;

    @ApiModelProperty(value = "仓库ID", required = true, example = "1")
    @NotNull(message = "仓库ID不能为空")
    private String warehouse;
    @ApiModelProperty(value = "商品批次号", required = false, example = "1")
    @NotBlank(message = "批次号不能为空") // 允许空字符串，但不允许null
    private String batch;

    @ApiModelProperty(value = "生产日期 由字符串日期转换而来 ", required = true, example = "2025-10-30")
    @NotNull(message = "生产日期时间不能为空")
    private String mfd;

    @ApiModelProperty(value = "商品单价", required = true, example = "2.0000")
    @NotNull(message = "商品单价不能为空")
    @DecimalMin(value = "0.0001", message = "商品单价必须大于0") // 单价不能为0或负数
    private BigDecimal price;

    @ApiModelProperty(value = "商品数量", required = true, example = "1.0000")
    @NotNull(message = "商品数量不能为空")
    @DecimalMin(value = "0.0001", message = "商品数量必须大于0") // 数量不能为0或负数
    private BigDecimal nums;

    @ApiModelProperty(value = "折扣率", required = true, example = "0.00")
    @NotNull(message = "折扣率不能为空")
    @DecimalMin(value = "0", message = "折扣率不能小于0") // 折扣率非负
    private BigDecimal discount;

    @ApiModelProperty(value = "折扣额", required = true, example = "0.0000")
    @NotNull(message = "折扣额不能为空")
    @DecimalMin(value = "0", message = "折扣额不能小于0") // 折扣额非负
    private BigDecimal dsc;

    @ApiModelProperty(value = "商品金额", required = true, example = "2.0000")
    @NotNull(message = "商品金额不能为空")
    @DecimalMin(value = "0", message = "商品金额不能小于0") // 金额非负
    private BigDecimal total;

    @ApiModelProperty(value = "税率", required = true, example = "0.00")
    @NotNull(message = "税率不能为空")
    @DecimalMin(value = "0", message = "税率不能小于0") // 税率非负
    private BigDecimal tax;

    @ApiModelProperty(value = "税额", required = true, example = "0.0000")
    @NotNull(message = "税额不能为空")
    @DecimalMin(value = "0", message = "税额不能小于0") // 税额非负
    private BigDecimal tat;

    @ApiModelProperty(value = "价税合计", required = true, example = "2.0000")
    @NotNull(message = "价税合计不能为空")
    @DecimalMin(value = "0", message = "价税合计不能小于0") // 价税合计非负
    private BigDecimal tpt;

    @ApiModelProperty(value = "备注信息", required = true, example = "备注") //允许null或空白字符串
    private String data;

    @ApiModelProperty(value = "序列号（JSON数组字符串）", required = true, example = "[]")
    @NotBlank(message = "序列号不能为空") // 至少为"[]"等有效格式，不允许空白
    private String serial;

    //buy表
    @ApiModelProperty(value = "供应商", required = true, example = "SUPPLIER_001")
    private String supplier;

    @ApiModelProperty(value = "单据日期", required = true, example = "2025-10-19")
    private String time;

    @ApiModelProperty(value = "单据编号", required = true, example = "BUY_20251019001")
    private String number;

    @ApiModelProperty(value = "单据金额",required = true, example = "84.75")
    private BigDecimal noteTotal;

    @ApiModelProperty(value = "实际金额", required = true, example = "84.75")
    private BigDecimal actual;

    @ApiModelProperty(value = "实付金额", required = true, example = "84.75")
    private BigDecimal money;

    @ApiModelProperty(value = "单据费用", required = true, example = "1.00")
    private BigDecimal cost;

    @ApiModelProperty(value = "结算账户", required = true, example = "四川账户")
    private String account;

    @ApiModelProperty(value = "关联人员", required = true, example = "张三")
    private String people;

    @ApiModelProperty(value = "物流信息", required = true, example = "顺丰")
    private String logistics;

    @ApiModelProperty(value = "备注信息", required = true, example = "无")
    private String data2;

    //goods表
    @ApiModelProperty(value = "商品名称", example = "小刀")
    private String name;

    @ApiModelProperty(value = "商品编号", example = "GOODS_001")
    private String goodsNumber;

    @ApiModelProperty(value = "规格型号", example = "DJ-0001")
    private String spec;

}
