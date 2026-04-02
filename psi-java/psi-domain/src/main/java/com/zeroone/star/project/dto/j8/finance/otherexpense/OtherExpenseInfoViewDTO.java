package com.zeroone.star.project.dto.j8.finance.otherexpense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 描述：其他支出单详情数据对象
 *
 * 涉及数据库表：oce_info
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("其他支出单详情数据对象")
public class OtherExpenseInfoViewDTO {
    @ApiModelProperty(value = "其它支出单详情id",example = "info1")
    private String id;
    @ApiModelProperty(value = "所属ID",example = "QTZCD2510211359551")
    private String pid;
    @ApiModelProperty(value = "所属费用",example = "生活")
    private String source;
    @ApiModelProperty(value = "收支类型",example = "学习")
    private String iet;
    @ApiModelProperty(value = "结算金额",example = "100")
    private BigDecimal money;
    @ApiModelProperty(value = "备注信息",example = "备注01")
    private String data;
}
