package com.zeroone.star.project.dto.j7.money.transfer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel("转账单修改数据对象")
@NoArgsConstructor
@AllArgsConstructor
public class AllotModifyDTO {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty(value = "转账单id",example = "1",required = true)
    private String id;

    @ApiModelProperty(value = "单据时间", example = "2025-10-21",required = true)
    private LocalDate time;

    @NotBlank(message = "单据编号不能为空")
    @ApiModelProperty(value = "单据编号", example = "DJ20xxx",required = true)
    private String number;

    @NotNull(message = "单据金额不能为空")
    @Min(value = 0, message = "金额不能小于0")
    @ApiModelProperty(value = "单据金额", example = "1890.50",required = true)
    private BigDecimal total;

    @ApiModelProperty(value = "关联人员名称", example = "张三")
    private String people;

    @Min(value = 0, message = "审核状态不能小于0")
    @Max(value = 1, message = "审核状态不能大于1")
    @ApiModelProperty(value = "审核状态 0:未审核 1:已审核", example = "0",required = true)
    private Integer examine;

    @NotBlank(message = "制单人不能为空")
    @ApiModelProperty(value = "制单人", example = "李四",required = true)
    private String user;

    @ApiModelProperty(value = "备注信息", example = "信息")
    private String data;

    @ApiModelProperty(value = "表单详询")
    private List<AllotInfoDTO> allotList;
}
