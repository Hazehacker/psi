package com.zeroone.star.project.dto.j4.sale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 销售订单CSV导入DTO
 * 适配“单据资料”与“商品资料”分离的CSV模板，支持同一订单多行商品
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("销售订单CSV导入数据对象，包含单据资料和关联的商品资料列表")
public class SaleOrderImportDTO {

    // ===================== 单据资料（订单主信息，同一订单仅需填写一次） =====================
    @ApiModelProperty(value = "客户（单据资料）", required = true, example = "零壹星球科技有限公司")
    @NotBlank(message = "单据资料-客户不能为空")
    private String customer;

    @ApiModelProperty(value = "单据日期（单据资料），格式：yyyy-MM-dd", required = true, example = "2024-10-20")
    @NotNull(message = "单据资料-单据日期不能为空")
    private LocalDate orderDate;

    @ApiModelProperty(value = "单据编号（单据资料，系统唯一标识）", required = true, example = "SOR20241020001")
    @NotBlank(message = "单据资料-单据编号不能为空")
    @Pattern(regexp = "^SOR\\d{8}\\d{3}$", message = "单据编号格式应为SOR+8位日期+3位序号（如SOR20241020001）")
    private String orderNumber;

    @ApiModelProperty(value = "单据金额（单据资料，订单总金额未扣折扣前）", required = true, example = "25600.00")
    @NotNull(message = "单据资料-单据金额不能为空")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "实际金额（单据资料，订单总金额扣折扣后）", required = true, example = "24320.00")
    @NotNull(message = "单据资料-实际金额不能为空")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "关联人员（单据资料，如业务员）", example = "张晓明")
    private String relatedPerson;

    @ApiModelProperty(value = "到货日期（单据资料），格式：yyyy-MM-dd", example = "2024-10-25")
    private LocalDate arrivalDate;

    @ApiModelProperty(value = "物流信息（单据资料，如快递公司+单号）", example = "顺丰速运 SF123456789")
    private String logisticsInfo;

    @ApiModelProperty(value = "备注信息（单据资料，订单级备注）", example = "季度采购订单，优先发货")
    private String orderRemark;

    // ===================== 商品资料（同一订单可包含多个商品，对应CSV多行） =====================
    @ApiModelProperty(value = "商品资料列表（同一订单的所有商品）", required = true)
    @NotNull(message = "商品资料列表不能为空")
    private List<SaleOrderItemImportDTO> productList;

}