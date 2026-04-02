package com.zeroone.star.project.dto.j8.finance.otherexpense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 其他支出单详细信息
 * <p>
 * 涉及数据库表：<b>oce,oce_bill,oce_info,iet</b>
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("其他支出单详细信息")
public class OtherExpenseDetailDTO {

    @ApiModelProperty(value = "支出单ID", example = "test1")
    private String id;

    @ApiModelProperty(value = "所属组织", example = "组织001")
    private String frame;

    @ApiModelProperty(value = "供应商", example = "供应商001")
    private String supplier;

    @ApiModelProperty(value = "单据时间", example = "2025-10-20 15:23:00")
    private LocalDateTime time;

    @ApiModelProperty(value = "单据编号", example = "QTZCD2510201810404")
    private String number;

    @ApiModelProperty(value = "单据金额", example = "12345.67")
    private BigDecimal total;

    @ApiModelProperty(value = "实际金额", example = "12345.67")
    private BigDecimal actual;

    @ApiModelProperty(value = "实付金额", example = "12345.67")
    private BigDecimal money;

    //oce_bill
    @ApiModelProperty(value = "核销金额", example = "12345.33")
    private BigDecimal writeOffAmount;

    @ApiModelProperty(value = "结算账户", example = "CCB001")
    private String account;

    @ApiModelProperty(value = "关联人员", example = "people001")
    private String people;

    @ApiModelProperty(value = "单据附件", example = "[\"/upload/invoice_20251020.pdf\"]")
    private String file;

    @ApiModelProperty(value = "备注信息", example = "备注test")
    private String data;

    @ApiModelProperty(value = "审核状态", example = "1")
    private Integer examine;

    @ApiModelProperty(
            value = "支出详情列表",
            example = "[{\"id\": \"ZCD024X\",  \"ietName\": \"生活\", \"money\": 120.50, \"data\": \"备注\"}]"
    )
    private List<OtherExpenseInfoDTO> info;
}
