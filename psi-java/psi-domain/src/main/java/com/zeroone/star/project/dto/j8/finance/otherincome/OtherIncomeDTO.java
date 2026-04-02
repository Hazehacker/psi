package com.zeroone.star.project.dto.j8.finance.otherincome;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 其他收入单数据对象 (用于列表显示)
 * 涉及数据表: ice,ice_bill
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("其他收入单数据对象")
public class OtherIncomeDTO {

    @ApiModelProperty(value = "收入单ID")
    private String id;

    @ApiModelProperty(value = "所属组织")
    private String frame;

    @ApiModelProperty(value = "客户")
    private String customer;

    @ApiModelProperty(value = "单据时间")
    private LocalDateTime time;

    @ApiModelProperty(value = "单据编号")
    private String number;

    @ApiModelProperty(value = "单据金额")
    private BigDecimal total;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal actual;

    @ApiModelProperty(value = "实收金额")
    private BigDecimal money;

    //ice_bill
    @ApiModelProperty(value = "核销金额")
    private BigDecimal writeOffAmount;

    @ApiModelProperty(value = "结算账户")
    private String account;

    @ApiModelProperty(value = "关联人员")
    private String people;

    @ApiModelProperty(value = "审核状态")
    private int examine;

    @ApiModelProperty(value = "核销状态")
    private int nucleus;

    @ApiModelProperty(value = "制单人")
    private String user;

    @ApiModelProperty(value = "备注信息")
    private String data;

    @ApiModelProperty(value = "详细信息")
    private OtherIncomeInfoDTO otherIncomeInfoDTO;

    @ApiModelProperty(value = "核销记录")
    private OtherIncomeBillDTO otherIncomeBillDTO;

}
