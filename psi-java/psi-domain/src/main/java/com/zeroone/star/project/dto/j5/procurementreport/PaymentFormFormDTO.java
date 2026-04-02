package com.zeroone.star.project.dto.j5.procurementreport;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel(value = "采购付款表单数据对象")
public class PaymentFormFormDTO {
    @ApiModelProperty(value = "单据类型", example = "采购单", allowableValues = "采购单,采购退货单")
    private String type;
    @ApiModelProperty(value = "所属组织", example = "123456")
    private String frame;
    @ApiModelProperty(value = "供应商", example = "工厂")
    private String supplier;
    @ApiModelProperty(value = "单据时间", example = "2025-10-18")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate time;
    @ApiModelProperty(value = "单据编号", example = "100001")
    private String number;
    @ApiModelProperty(value = "单据金额", example = "10000.0000")
    private BigDecimal total;
    @ApiModelProperty(value = "实际金额", example = "10000.0000")
    private BigDecimal actual;
    // 单据付款
    @ApiModelProperty(value = "单据付款", example = "10000.0000")
    private BigDecimal payment;
    @ApiModelProperty(value = "应付款余额", example = "10000.0000")
    private BigDecimal balance;
    // 付款率
    @ApiModelProperty(value = "付款率", example = "100%")
    private String rate;
    @ApiModelProperty(value = "核销状态", example = "0", allowableValues = "0,1,2")
    private Integer nucleus;
    @ApiModelProperty(value = "备注信息", example = "备注信息")
    private String data;
}
