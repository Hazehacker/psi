package com.zeroone.star.project.dto.j3.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: WriteOffDetailDTO
 * @Description: 核销单DTO
 * @Author: 平生
 * @Date: 2025/10/25 23:41
 */
@Data
@ApiModel("采购单核销详情")
public class WriteOffDetailDTO {
    @ApiModelProperty(value = "采购单核销ID", example = "1", required = true)
    @NotBlank(message = "采购单核销ID不能为空")
    @Size(max = 32, message = "ID长度不能超过32")
    private String id;

    @ApiModelProperty(value = "所属单据ID", example = "PID123456", required = true)
    @NotBlank(message = "所属单据不能为空")
    @Size(max = 32, message = "所属单据长度不能超过32")
    private String pid;

    @ApiModelProperty(value = "核销类型", example = "预付款核销/费用核销", required = true)
    @NotBlank(message = "核销类型不能为空")
    @Size(max = 32, message = "核销类型长度不能超过32")
    private String type;

    @ApiModelProperty(value = "关联单据", example = "PO20251025001")
    @Size(max = 32, message = "关联单据长度不能超过32")
    private String source;

    @ApiModelProperty(value = "单据时间", example = "2025-10-25 10:00:00", required = true)
    @NotNull(message = "单据时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;

    @ApiModelProperty(value = "核销金额", example = "1999.99", required = true)
    @NotNull(message = "核销金额不能为空")
    @Digits(integer = 16, fraction = 4, message = "核销金额格式不正确")
    @DecimalMin(value = "0", inclusive = true, message = "核销金额不能为负数")
    private BigDecimal money;

}
