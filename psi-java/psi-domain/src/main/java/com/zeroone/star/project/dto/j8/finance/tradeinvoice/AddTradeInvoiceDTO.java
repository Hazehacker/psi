package com.zeroone.star.project.dto.j8.finance.tradeinvoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "新增发票参数", description = "批量为业务单据（buy/bre/sell/sre）开具发票")
public class AddTradeInvoiceDTO implements Serializable {

    @Data
    @ApiModel(value = "待开票单据明细", description = "一条发票可对应多张业务单据，逐条填写金额")
    public static class Bill implements Serializable {
        @ApiModelProperty(value = "单据类型", required = true, example = "sell", allowableValues = "buy,bre,sell,sre")
        @NotBlank(message = "单据类型不能为空")
        private String type;

        @ApiModelProperty(value = "单据ID", required = true, example = "SEL0002")
        @NotBlank(message = "单据ID不能为空")
        private String id;

        @ApiModelProperty(value = "本张发票开给该单据的金额", required = true, example = "300.00")
        @NotNull(message = "开票金额不能为空")
        @DecimalMin(value = "0.01", message = "开票金额必须大于0")
        @Digits(integer = 16, fraction = 4, message = "金额最多16位整数,4位小数")
        private BigDecimal amount;
    }

    @ApiModelProperty(value = "待开票单据集合", required = true,
            example = "[{\"type\":\"sell\",\"id\":\"SEL0002\",\"amount\":300.00}]")
    @NotEmpty(message = "至少选择一条待开票单据")
    @Valid
    private List<Bill> infos;

    @ApiModelProperty(value = "开票时间", required = true, example = "2025-10-20 15:23:00")
    @NotNull(message = "开票时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;

    @ApiModelProperty(value = "发票号码", required = true, example = "FP-251020-0001")
    @NotBlank(message = "发票号码不能为空")
    @Size(max = 64, message = "发票号码长度不能超过64")
    private String number;

    @ApiModelProperty(value = "发票抬头", required = true, example = "星光科技有限公司")
    @NotBlank(message = "发票抬头不能为空")
    @Size(max = 64, message = "发票抬头长度不能超过64")
    private String title;

    @ApiModelProperty(value = "发票附件（可为URL或JSON）", example = "{\"urls\":[\"https://.../a.pdf\"]}")
    private String file;

    @ApiModelProperty(value = "备注信息", example = "本次开票为部分开票")
    @Size(max = 256, message = "备注长度不能超过256")
    private String data;
}
