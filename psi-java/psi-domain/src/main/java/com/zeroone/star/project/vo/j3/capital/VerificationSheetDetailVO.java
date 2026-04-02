package com.zeroone.star.project.vo.j3.capital;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@ApiModel("核销单详情VO")
public class VerificationSheetDetailVO {
    /**
     * 核销类型
     */
    @ApiModelProperty(value = "核销类型", example = "预收")
    @NotBlank(message = "核销类型不能为空")
    private String bill;
    /**
     * 单据类型
     */
    @ApiModelProperty(value = "单据类型", example = "收款单")
    @NotBlank(message = "单据类型不能为空")
    private String mold;

    /**
     * 单据日期--bre的time字段
     */
    @ApiModelProperty(value = "单据日期", required = true, example = "2022-01-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime time;


    /**
     * 单据编号--对应的是BillInfo的source
     */
    @ApiModelProperty(value = "单据编号", example = "HXD20231201001")
    @NotBlank(message = "单据编号不能为空")
    private String number;

    /**
     * 单据金额---如对应在bre的total字段
     */
    @ApiModelProperty(value = "单据金额", example = "100.00")
    @NotBlank(message = "单据金额不能为空")
    @DecimalMin(value = "0.0", inclusive = true, message = "单据金额不能小于0")
    private BigDecimal total;

    /**
     * 已核销金额---需要根据所属单据查询bre_bill表的money字段
     */
    @ApiModelProperty(value = "已核销金额", example = "100.00")
    @NotBlank(message = "已核销金额不能为空")
    @DecimalMin(value = "0.0", inclusive = true, message = "已核销金额不能小于0")
    private BigDecimal amount;

    /**
     * 未核销金额---单据金额（total）-已核销金额（amount）
     */
    @ApiModelProperty(value = "未核销金额", example = "100.00")
    @NotBlank(message = "未核销金额不能为空")
    @DecimalMin(value = "0.0", inclusive = true, message = "未核销金额不能小于0")
    private BigDecimal anwo;

    /**
     * 核销金额--bill_info的money字段
     */
    @ApiModelProperty(value = "核销金额", example = "100.00")
    @NotBlank(message = "核销金额不能为空")
    @DecimalMin(value = "0.0", inclusive = true, message = "核销金额不能小于0")
    private BigDecimal money;

}
