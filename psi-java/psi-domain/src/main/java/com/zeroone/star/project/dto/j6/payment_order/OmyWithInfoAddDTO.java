package com.zeroone.star.project.dto.j6.payment_order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel("主表和信息表新增DTO")
public class OmyWithInfoAddDTO {
    @NotBlank(message = "请选择供应商")
    @ApiModelProperty(value = "供应商",example = "中创集团")
    private String supplier;

    @NotNull(message="请选择单据日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "单据日期",example = "2025-10-22")
    private LocalDate time;

    @NotBlank(message="请输入单据编号")
    @ApiModelProperty(value = "单据编号",example = "S111")
    private String number;


    @ApiModelProperty("单据金额自动计算，无需输入")
    //@ApiModelProperty(value = "单据金额",example = "1000")
    private BigDecimal total;

    @ApiModelProperty(value = "关联人员",example = "jack")
    private String people;

    @ApiModelProperty(value = "单据附件",example = "点击上传")
    private String file;

    @ApiModelProperty(value = "备注信息",example = "新增付款单示例")
    private String data;

    @ApiModelProperty(value = "付款单明细信息")
    private List<OmyInfoAddDTO> infoList;

}
