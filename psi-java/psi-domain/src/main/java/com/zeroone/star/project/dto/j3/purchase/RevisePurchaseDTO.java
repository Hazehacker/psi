package com.zeroone.star.project.dto.j3.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: RevisePurchaseDTO
 * @Description: 修改采购单
 * @Author: 正念
 * @Date: 2025/10/20 22:57
 */
@Data
@ApiModel("修改采购单")
public class RevisePurchaseDTO {
    @ApiModelProperty(value = "采购单ID", example = "1", required = true)
    @NotBlank(message = "ID不能为空")
    @Size(max = 32, message = "ID长度不能超过32")
    private String id;

    @ApiModelProperty(value = "供应商", example = "XXX公司")
    @Size(max = 64, message = "供应商长度不能超过64")
    private String supplier;

    @ApiModelProperty(value = "单据时间", example = "2025-01-05 12:30:00", required = true)
    @NotNull(message = "单据时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;

    @ApiModelProperty(value = "单据编号", example = "CGD2510202227267")
    @Size(max = 32, message = "单据编号长度不能超过32")
    private String number;

    @ApiModelProperty(value = "采购单明细列表", required = true)
    @Valid
    private List<PurchaseBuyInfoDTO> details;

    @ApiModelProperty(value = "实际金额", example = "1999.99")
    @Digits(integer = 16, fraction = 4, message = "实际金额格式不正确")
    @DecimalMin(value = "0", inclusive = true, message = "实际金额不能为负数")
    private BigDecimal actual;

    @ApiModelProperty(value = "实收金额", example = "1999.99")
    @Digits(integer = 16, fraction = 4, message = "实收金额格式不正确")
    @DecimalMin(value = "0", inclusive = true, message = "实收金额不能为负数")
    private BigDecimal money;

    @ApiModelProperty(value = "结算账户", example = "ICBC-xxx")
    @NotNull(message = "结算账户不能为空")
    @Size(max = 64, message = "结算账户长度不能超过64")
    private String account;

    @ApiModelProperty(value = "关联人员", example = "王二")
    @Size(max = 32, message = "关联人员长度不能超过32")
    private String people;

    @ApiModelProperty(value = "物流信息", example = "SF1234567890")
    @Size(max = 256, message = "物流信息长度不能超过256")
    private String logistics;

    @ApiModelProperty(value = "单据附件",example = "http://localhost:8080/file.word")
    @Size(max = 500, message = "附件URL长度不能超过500字符")
    @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$", message = "附件URL格式不正确")
    private String file;

    @ApiModelProperty(value = "备注信息", example = "可以为空")
    @Size(max = 256, message = "备注信息长度不能超过256")
    private String data;

    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]", example = "1")
    private Integer examine;

    @ApiModelProperty(value = "核对状态[0:未核对|1:已核对]", example = "1")
    private Integer checkStatus;

}
