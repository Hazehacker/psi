package com.zeroone.star.project.dto.j8.finance.otherincome;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 新增其他收入单数据对象
 * 涉及数据表: ice,ice_info
 */
@Data
@ApiModel("新增其他收入单数据对象")
public class AddOtherIncomeDTO {
    @ApiModelProperty(value = "收入单id(修改时使用)", required = false)
    private String id;

    @NotBlank(message = "所属组织不能为空")
    @Size(max = 32, message = "所属组织ID长度不能超过32")
    @ApiModelProperty(value = "所属组织")
    private String frame;

    @ApiModelProperty(value = "客户")
    private String customer;

    @NotNull(message = "单据时间不能为空")
    @PastOrPresent(message = "单据时间不能是未来时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "单据时间", example = "2025-10-22 11:30:00")
    private LocalDateTime time;

    @NotBlank(message = "单据编号不能为空")
    @ApiModelProperty(value = "单据编号")
    private String number;

    @NotNull(message = "单据金额不能为空")
    @DecimalMin(value = "0.0", message = "单据金额不能为负数")
    @Digits(integer = 12, fraction = 4, message = "单据金额格式不正确，整数最多12位，小数最多4位")
    @ApiModelProperty(value = "单据金额", required = true)
    private BigDecimal total;

    @NotNull(message = "实际金额不能为空")
    @DecimalMin(value = "0.0", message = "实际金额不能为负数")
    @Digits(integer = 12, fraction = 4, message = "实际金额格式不正确，整数最多12位，小数最多4位")
    @ApiModelProperty(value = "实际金额")
    private BigDecimal actual;

    @NotNull(message = "实收金额不能为空")
    @DecimalMin(value = "0.0", message = "实收金额不能为负数")
    @Digits(integer = 12, fraction = 4, message = "实收金额格式不正确，整数最多12位，小数最多4位")
    @ApiModelProperty(value = "实付金额/单据收款")
    private BigDecimal money;

    @NotBlank(message = "结算账户不能为空")
    @Size(max = 32, message = "结算账户ID长度不能超过32")
    @ApiModelProperty(value = "结算账户", required = true)
    private String account;

    @Size(max = 32, message = "关联人员ID长度不能超过32")
    @ApiModelProperty(value = "关联人员")
    private String people;

    @ApiModelProperty(value = "单据附件")
    private String file;

    @Size(max = 256, message = "备注信息长度不能超过256")
    @ApiModelProperty(value = "备注信息")
    private String data;

    @ApiModelProperty(value = "扩展信息")
    private String more;

    @NotBlank(message = "制单人不能为空")
    @Size(max = 32, message = "制单人ID长度不能超过32")
    @ApiModelProperty(value = "制单人")
    private String user;

    @Valid
    @NotEmpty(message = "明细信息列表不能为空，至少需要一条明细")
    @ApiModelProperty(value = "收入详情列表", required = true)
    private List<AddOtherIncomeInfoDTO> info;

//    @Valid
//    @ApiModelProperty(value = "收入核销详情")
//    private List<AddOtherIncomeBillDTO> bill;

}
