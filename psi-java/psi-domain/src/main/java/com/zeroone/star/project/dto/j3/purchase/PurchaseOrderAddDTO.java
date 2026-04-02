package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 描述：新增采购订单对象
 * </p>
 * @author xiaoke
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("新增采购订单对象")
public class PurchaseOrderAddDTO {

    /**
     * 关联单据 | Sor
     */
    @ApiModelProperty(value = "关联单据 | Sor", example = "1983381181546139649")
    private String source;

    /**
     * 供应商
     */
    @NotBlank(message = "供应商不能为空")
    @ApiModelProperty(value = "供应商", example = "1983381181546139684", required = true)
    private String supplier;

    /**
     * 单据时间
     */
    @NotNull(message = "单据时间不能为空")
    @ApiModelProperty(value = "单据时间", example = "2025-10-18", required = true)
    private LocalDate time;

    /**
     * 单据编号
     */
    @NotBlank(message = "单据编号不能为空")
    @ApiModelProperty(value = "单据编号", example = "CGDD2510291218013", required = true)
    private String number;

    /**
     * 单据金额
     */
    @NotNull(message = "单据金额不能为空")
    @Positive(message = "单据金额不能小于0")
    @ApiModelProperty(value = "单据金额", example = "11.0", required = true)
    private BigDecimal total;

    /**
     * 实际金额
     */
    @NotNull(message = "实际金额不能为空")
    @Positive(message = "实际金额不能小于0")
    @ApiModelProperty(value = "实际金额", example = "10.0", required = true)
    private BigDecimal actual;

    /**
     * 关联人员
     */
    @NotBlank(message = "关联人员不能为空")
    @ApiModelProperty(value = "关联人员", example = "1983381181546137851", required = true)
    private String people;

    /**
     * 到货时间
     */
    @NotNull(message = "到货时间不能为空")
    @ApiModelProperty(value = "到货时间", example = "2025-10-18", required = true)
    private LocalDate arrival;

    /**
     * 物流信息
     */
    @ApiModelProperty(value = "物流信息", example = "")
    private String logistics;

    /**
     * 单据附件
     */
    @ApiModelProperty(value = "单据附件", example = "")
    private String file;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", example = "无")
    private String data;

    /**
     * 采购订单详情
     */
    @Valid
    @Size(min = 1, message = "采购订单中至少包含一条数据")
    @ApiModelProperty(value = "采购订单详情")
    private List<PurchaseOrderInfoDTO> purchaseOrderInfoDTOList = new ArrayList<>();

}
