package com.zeroone.star.project.dto.j8.finance.otherincome;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 其他收入单详细信息
 * 涉及数据表: ice,ice_info,ice_bill
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("其他收入单详细信息")
public class OtherIncomeDetailDTO {

    @ApiModelProperty(value = "收入单ID", example = "income_test1")
    private String id;

    @ApiModelProperty(value = "所属组织", example = "组织001")
    private String frame;

    @ApiModelProperty(value = "客户", example = "客户001")
    private String customer;

    @ApiModelProperty(value = "单据时间", example = "2025-10-22 11:30:00")
    private LocalDateTime time;

    @ApiModelProperty(value = "单据编号", example = "QTSRD251022113030")
    private String number;

    @ApiModelProperty(value = "单据金额", example = "8888.88")
    private BigDecimal total;

    @ApiModelProperty(value = "应收金额", example = "8888.88")
    private BigDecimal actual;

    @ApiModelProperty(value = "实收金额", example = "8000.00")
    private BigDecimal money;

    @ApiModelProperty(value = "结算账户", example = "ICBC001")
    private String account;

    @ApiModelProperty(value = "关联人员", example = "people002")
    private String people;

    @ApiModelProperty(value = "单据附件", example = "[\"/upload/income_receipt_20251022.jpg\"]")
    private String file;

    @ApiModelProperty(value = "备注信息", example = "这是一笔其它收入")
    private String data;

    @ApiModelProperty(value = "扩展信息", example = "xyz")
    private String more;

    @ApiModelProperty(value = "审核状态", example = "1")
    private int examine;

    @ApiModelProperty(value = "核销状态", example = "0")
    private int nucleus;

    @ApiModelProperty(value = "制单人", example = "制单人002")
    private String user;

    @ApiModelProperty(value = "收入详情列表")
    private List<OtherIncomeInfoDTO> info;

    @ApiModelProperty(value = "收入核销列表")
    private List<OtherIncomeBillDTO> bill;
}
