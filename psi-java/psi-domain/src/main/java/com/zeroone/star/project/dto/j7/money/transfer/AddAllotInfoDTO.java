package com.zeroone.star.project.dto.j7.money.transfer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 类名：AddAllotInfoDTO
 * 包名：com.zeroone.star.project.dto.j7.money.transfer
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/31
 * 版本号：V1.0
 */
@Data
@ApiModel("新增转账单详情对象")
public class AddAllotInfoDTO {

    /**
     * 转出账户
     */
    @NotBlank(message = "转出账户不能为空")
    @ApiModelProperty(value = "转出账户", example = "工行-营业部-6222021234567890123",required = true)
    private String account;

    /**
     * 转入账户
     */
    @NotBlank(message = "转入账户不能为空")
    @ApiModelProperty(value = "转入账户", example = "建行-科技支行-6217001234567890123",required = true)
    private String tat;

    /**
     * 结算金额
     */
    @Min(value = 0, message = "金额不能小于0")
    @NotBlank(message = "结算金额不能为0")
    @ApiModelProperty(value = "结算金额", example = "3500.80",required = true)
    private BigDecimal money;

    /**
     * 结算号
     */
    @ApiModelProperty(value = "结算号", example = "SETTLE20251027008")
    private String settle;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", example = "本次结算为跨部门设备调拨尾款，已核对明细")
    private String data;

}
