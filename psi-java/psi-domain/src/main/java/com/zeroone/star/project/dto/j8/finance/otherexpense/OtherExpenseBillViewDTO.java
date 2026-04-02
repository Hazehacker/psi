package com.zeroone.star.project.dto.j8.finance.otherexpense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 描述：其它支出单核销详情
 *
 * 涉及数据库表：oce_bill
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("其它支出单核销详情数据对象")
public class OtherExpenseBillViewDTO {
    @ApiModelProperty(value = "支出单核销详情id",example = "bill01")
    private String id;
    @ApiModelProperty(value = "所属单据",example = "QTZCD2510211359551")
    private String pid;
    @ApiModelProperty(value = "核销类型",example = "生活")
    private String type;
    @ApiModelProperty(value = "关联单据",example = "test1")
    private String source;
    @ApiModelProperty(value = "单据时间",example = "2023-05-19 00:00:00")
    private LocalDateTime time;
    @ApiModelProperty(value = "核销金额",example = "100")
    private BigDecimal money;
}
