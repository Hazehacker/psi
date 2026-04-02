package com.zeroone.star.project.dto.j3.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @Author xiaoliu
 * @Date 2025/10/19 16:15
 * @PackageName:com.zeroone.star.project.dto.j3.purchase
 * @ClassName: PurchaseReturnDetailDTO
 * @Description: 采购退货商品详情DTO
 * @Version 1.0
 */
@ApiModel("PurchaseReturnGoodsDetailDTO《采购退货商品详情DTO》")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseReturnGoodsDetailDTO {

    /**
     * 采购退货单详情id
     */
    @ApiModelProperty(value = "采购退货单详情id",example = "CGTHD2510191431534")
    @Size(max = 50, message = "详情ID长度不能超过50字符")
    private String id;

    /**
     * 所属id
     */
    @ApiModelProperty(value="所属id",example = "212122")
    @Size(max = 50, message = "所属ID长度不能超过50字符")
    private String pid;

    /**
     * 关联详情 | BUY
     */
    @ApiModelProperty(value = "关联详情",example = "CGD2510191431534")
    @Size(max = 50, message = "关联详情长度不能超过50字符")
    private String source;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称",example = "A4打印纸")
    @NotBlank(message = "商品名称不能为空")
    @Size(min = 2, max = 100, message = "商品名称长度应在2-100字符之间")
    private String goods;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号",example = "P001")
    @NotBlank(message = "商品编号不能为空")
    @Size(min = 2, max = 50, message = "商品编号长度应在2-50字符之间")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "商品编号只能包含字母和数字")
    private String number;

    /**
     * 规格型号
     */
    @ApiModelProperty(value = "规格型号",example = "80g")
    @Size(max = 50, message = "规格型号长度不能超过50字符")
    private String spec;

    /**
     * 辅助属性
     */
    @ApiModelProperty(value = "辅助属性",example = "白色")
    @Size(max = 50, message = "辅助属性长度不能超过50字符")
    private String attr;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位",example = "包")
    @NotBlank(message = "单位不能为空")
    @Size(max = 20, message = "单位长度不能超过20字符")
    private String unit;

    /**
     * 仓库
     */
    @ApiModelProperty(value = "仓库",example = "一号仓库")
    @NotBlank(message = "仓库不能为空")
    @Size(max = 50, message = "仓库名称长度不能超过50字符")
    private String warehouse;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号",example = "B20231019")
    @Size(max = 50, message = "批次号名称长度不能超过50字符")
    private String batch;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期", example = "2025-10-30")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate mfd;

    /**
     * 单价
     */
    @ApiModelProperty(value = "单价",example = "20.50")
    @NotNull(message = "单价不能为空")
    @DecimalMin(value = "0.01", message = "单价必须大于0")
    private BigDecimal price;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量",example = "50")
    @NotNull(message = "数量不能为空")
    @DecimalMin(value = "0.01", message = "数量必须大于0")
    private BigDecimal nums;

    /**
     * 序列号
     */
    @ApiModelProperty(value = "序列号",example = "S001")
    @Size(max = 50, message = "序列号长度不能超过50字符")
    private String serial;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额",example = "1025.00")
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    private BigDecimal total;

    /**
     * 税率
     */
    @ApiModelProperty(value = "税率",example = "0.00")
    @DecimalMin(value = "0.00", message = "税率不能为负数")
    @DecimalMax(value = "100.00", message = "税率不能超过100%")
    private BigDecimal tax;

    /**
     * 税额
     */
    @ApiModelProperty(value = "税额",example = "0.00")
    @DecimalMin(value = "0.00", message = "税额不能为负数")
    private BigDecimal tat;

    /**
     * 价税合计
     */
    @ApiModelProperty(value = "价税合计",example = "955.00")
    @DecimalMin(value = "0.01", message = "价税合计必须大于0")
    private BigDecimal tpt;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息",example = "全部退货")
    @Size(max = 200, message = "备注信息长度不能超过200字符")
    private String data;

}

