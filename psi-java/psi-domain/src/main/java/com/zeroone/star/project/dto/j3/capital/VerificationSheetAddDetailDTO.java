package com.zeroone.star.project.dto.j3.capital;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("添加核销单详情DTO")
public class VerificationSheetAddDetailDTO {
    /**
     * 核销类型
     */
    @ApiModelProperty(value = "核销类型", required = true, example = "cia")
    @NotNull(message = "核销类型不能为空")
    private String bill;

    /**
     * 单据类型
     */
    @ApiModelProperty(value = "单据类型", required = true, example = "imy")
    @NotNull(message = "单据类型不能为空")
    private String mold;

    /**
     * 单据日期
     */
    @ApiModelProperty(value = "单据日期", required = true, example = "2022-01-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @NotNull(message = "单据日期不能为空")
    private LocalDateTime time;


    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号", required = true, example = "HXD20231201001")
    @NotBlank(message = "单据编号不能为空")
    private String number;

    /**
     * 单据金额---如对应在bre的total字段
     */
    @ApiModelProperty(value = "单据金额", required = true, example = "100.00")
    @NotBlank(message = "单据金额不能为空")
    @DecimalMin(value = "0.0", message = "单据金额不能小于0")
    private BigDecimal total;

    /**
     * 已核销金额---需要根据所属单据查询bre_bill表的money字段
     */
    @ApiModelProperty(value = "已核销金额", required = true, example = "100.00")
    @NotBlank(message = "已核销金额不能为空")
    @DecimalMin(value = "0.0", message = "已核销金额不能小于0")
    private BigDecimal amount;

    /**
     * 未核销金额---单据金额（total）-已核销金额（amount）
     */
    @ApiModelProperty(value = "未核销金额", required = true, example = "100.00")
    @NotBlank(message = "未核销金额不能为空")
    @DecimalMin(value = "0.0", message = "未核销金额不能小于0")
    private BigDecimal anwo;

    /**
     * 核销金额--bill_info的money字段
     */
    @ApiModelProperty(value = "核销金额", required = true, example = "100.00")
    @NotBlank(message = "核销金额不能为空")
    @DecimalMin(value = "0.0", message = "核销金额不能小于0")
    private BigDecimal money;

    /**
     * 关联单据--关联单据的id字段
     */
    @ApiModelProperty(value = "关联单据", required = true, example = "3")
    @NotBlank(message = "关联单据不能为空")
    private String source;
}
