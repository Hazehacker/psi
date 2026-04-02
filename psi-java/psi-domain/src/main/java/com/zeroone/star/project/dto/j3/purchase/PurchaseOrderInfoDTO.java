package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hpsf.Decimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * <p>
 * 描述：采购订单详情输数据对象
 * </p>
 * @author xiaoke
 * @version 1.0.0
 */
@Data
@ApiModel("采购订单详情对象")
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderInfoDTO {

    /**
     * 所属商品
     */
    @NotBlank(message = "所属商品不能为空")
    @ApiModelProperty(value = "所属商品", example = "1983381181546139741", required = true)
    private String goods;

    /**
     * 辅助属性
     */
    @ApiModelProperty(value = "辅助属性")
    private String attr;

    /**
     *  单位
     */
    @NotBlank(message = "单位不能为空")
    @ApiModelProperty(value = "单位", example = "kg", required = true)
    private String unit;

    /**
     * 仓库
     */
    @NotBlank(message = "仓库不能为空")
    @ApiModelProperty(value = "仓库", example = "1983381181546139963", required = true)
    private String warehouse;

    /**
     * 单价
     */
    @NotNull(message = "单价不能为空")
    @ApiModelProperty(value = "单价", example = "10", required = true)
    private BigDecimal price;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    @Positive(message = "订单中货物数量必须大于0")
    @ApiModelProperty(value = "数量", example = "1", required = true)
    private BigDecimal nums;

    /**
     * 折扣率
     */
    @ApiModelProperty(value = "折扣率", example = "0")
    @DecimalMin(value = "0", message = "折扣率不能小于0")
    private BigDecimal discount;

    /**
     * 折扣额
     */
    @ApiModelProperty(value = "折扣额", example = "0")
    @DecimalMin(value = "0", message = "折扣额不能小于0")
    private BigDecimal dsc;

    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    @Positive(message = "货物金额必须大于0")
    @ApiModelProperty(value = "金额", example = "10", required = true)
    private BigDecimal total;

    /**
     * 税率
     */
    @ApiModelProperty(value = "税率", example = "10")
    @DecimalMin(value = "0", message = "税率不能小于0")
    private BigDecimal tax;

    /**
     * 税额
     */
    @ApiModelProperty(value = "税额", example = "1")
    @DecimalMin(value = "0", message = "税额不能小于0")
    private BigDecimal tat;

    /**
     * 价税合计
     */
    @NotNull(message = "价税合计不能为空")
    @Positive(message = "税价合计必须大于0")
    @ApiModelProperty(value = "价税合计", example = "11", required = true)
    private BigDecimal tpt;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", example = "无")
    private String data;
}
