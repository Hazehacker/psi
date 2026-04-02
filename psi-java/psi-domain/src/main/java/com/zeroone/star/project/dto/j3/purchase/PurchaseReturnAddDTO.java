package com.zeroone.star.project.dto.j3.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author xiaoliu
 * @Date 2025/10/19 16:48
 * @PackageName:com.zeroone.star.project.dto.j3.purchase
 * @ClassName: PurchaseReturnAddDTO
 * @Description: 新增采购退货单DTO
 * @Version 1.0
 */
@ApiModel("PurchaseReturnAddDTO《新增采购退货单DTO》")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseReturnAddDTO {

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商",example = "小米科技")
    @NotBlank(message = "供应商不能为空")
    @Size(min = 2, max = 100, message = "供应商名称长度应在2-100字符之间")
    private String supplier;

     /**
     * 单据日期
     */
    @ApiModelProperty(value = "单据时间",example = "2025-10-19 00:00:00")
    @NotNull(message = "单据时间不能为空")
    // 将日期字符串转换为 LocalDateTime
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;

    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号",example = "CGTHD2510191431538")
    @NotBlank(message = "单据编号不能为空")
    @Size(min = 5, max = 50, message = "单据编号长度应在5-50字符之间")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "单据编号只能包含大写字母和数字")
    private String number;

    /**
     * 单据金额
     */
    @ApiModelProperty(value = "单据金额",example = "2000.50")
    @NotNull(message = "单据金额不能为空")
    @DecimalMin(value = "0.01", message = "单据金额必须大于0")
    private BigDecimal total;

    /**
     * 实际金额
     */
    @ApiModelProperty(value = "实际金额",example = "1980.00")
    @NotNull(message = "实际金额不能为空")
    @DecimalMin(value = "0.01", message = "实际金额必须大于0")
    private BigDecimal actual;

    /**
     * 实收金额
     */
    @ApiModelProperty(value = "实收金额",example = "1980.00")
    @NotNull(message = "实收金额不能为空")
    @DecimalMin(value = "0.01", message = "实收金额必须大于0")
    private BigDecimal money;

    /**
     * 单据费用
     */
    @ApiModelProperty(value = "单据费用",example = "20.50")
    @NotNull(message = "单据费用不能为空")
    @DecimalMin(value = "0.00", message = "单据费用不能为负数")
    private BigDecimal cost;

    /**
     * 结算账户
     */
    @ApiModelProperty(value = "结算账户",example = "成都建设银行账户")
    @NotBlank(message = "结算账户不能为空")
    @Size(min = 2, max = 50, message = "结算账户名称长度应在2-50字符之间")
    private String account;

    /**
     * 关联人员
     */
    @ApiModelProperty(value = "关联人员",example = "张三")
    @NotBlank(message = "关联人员不能为空")
    @Size(min = 2, max = 20, message = "关联人员姓名长度应在2-20字符之间")
    private String people;

    /**
     * 物流信息
     */
    @ApiModelProperty(value = "物流信息",example = "顺丰快递 SF1234567890")
    @Size(max = 200, message = "物流信息长度不能超过200字符")
    private String logistics;

    /**
     * 单据附件
     */
    @ApiModelProperty(value = "单据附件",example = "http://localhost:8080/upload/return_order_1030.docx")
    @Size(max = 500, message = "附件URL长度不能超过500字符")
    @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$", message = "附件URL格式不正确")
    private String file;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息",example = "采购退货：部分商品包装破损")
    @Size(max = 500, message = "备注信息长度不能超过500字符")
    private String data;

    /**
     * 采购退货商品详情列表：PurchaseReturnDetailDTO
     */
    @NotEmpty(message = "采购退货商品详情不能为空")
    @Valid // 确保嵌套校验
    private List<PurchaseReturnGoodsDetailDTO> details;

}
