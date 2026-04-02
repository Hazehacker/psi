package com.zeroone.star.project.dto.j4.sale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 销售订单导出详情DTO（含商品明细）
 * 用于完整封装销售订单表头信息及关联的商品明细
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("销售订单导出详情DTO,包含订单表头信息和商品明细的完整销售订单数据")
public class SaleOrderExportDetailDTO {

    // ------------------------------ 订单表头信息 ------------------------------
    @ApiModelProperty(value = "所属组织", example = "总部销售部")
    private String organization;

    @ApiModelProperty(value = "客户名称", example = "北京宏业科技有限公司")
    private String customer;

    @ApiModelProperty(value = "单据日期", example = "2025-10-21")
    private LocalDate orderDate;  // 报表中为日期，用LocalDate更合适

    @ApiModelProperty(value = "单据编号", example = "XSDD2510211358447")
    private String orderNo;

    @ApiModelProperty(value = "单据总金额", example = "6772.00")
    private BigDecimal totalOrderAmount;

    @ApiModelProperty(value = "实际支付总金额", example = "5000.00")
    private BigDecimal totalActualAmount;

    @ApiModelProperty(value = "关联人员（销售员）", example = "张三")
    private String relatedPerson;

    @ApiModelProperty(value = "到货日期", example = "2025-10-25")
    private LocalDate arrivalDate;

    @ApiModelProperty(value = "物流信息", example = "顺丰快递 SF1234567890")
    private String logisticsInfo;

    @ApiModelProperty(value = "订单备注信息", example = "加急订单，需优先发货")
    private String orderRemarks;


    // ------------------------------ 商品明细列表 ------------------------------
    @ApiModelProperty(value = "订单包含的商品明细列表")
    private List<SaleOrderExportDetailItemDTO> itemList;
}