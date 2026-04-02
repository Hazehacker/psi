package com.zeroone.star.project.dto.j4.sale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售订单简洁导出DTO
 * 用于报表导出时的简洁订单信息封装
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("销售订单简洁导出DTO,销售订单报表导出的简洁信息")
public class SaleOrderExportSimpleDTO {

    @ApiModelProperty(value = "所属组织", example = "北京分公司")
    private String organization;

    @ApiModelProperty(value = "客户", example = "北京宏业科技有限公司")
    private String customer;

    @ApiModelProperty(value = "单据时间", example = "2023-10-21 09:30:00")
    private LocalDateTime orderTime;

    @ApiModelProperty(value = "单据编号", example = "SO20231021001")
    private String orderNo;

    @ApiModelProperty(value = "单据金额", example = "25600.00")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "实际金额", example = "24800.00")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "到货日期", example = "2023-10-26")
    private LocalDateTime arrivalDate;

    @ApiModelProperty(value = "关联人员", example = "张三（销售员）")
    private String relatedPerson;

    @ApiModelProperty(value = "审核状态", example = "已审核")
    private String auditStatus;

    @ApiModelProperty(value = "出库状态", example = "全部出库")
    private String deliveryStatus;

    @ApiModelProperty(value = "制单人", example = "李四")
    private String creator;

    @ApiModelProperty(value = "备注信息", example = "加急订单，优先处理")
    private String remarks;
}