package com.zeroone.star.project.dto.j5.fundreport;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户对账表导出主表，不含明细表信息
 *
 * @author toexpl
 * @since 2025/10/30
 */

@Data
@ApiModel("客户对账单主表导出DTO")
public class CustomerStatementMainExportDTO {
    @ApiModelProperty(value = "单据类型",example = "销售单")
    private String tableType;

    @ApiModelProperty(value = "所属组织",example = "公司")
    private String frame;

    @ApiModelProperty(value = "单据时间",example = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @ApiModelProperty(value = "单据编号",example = "1")
    private String number;

    @ApiModelProperty(value = "单据金额",example = "123456789012.1234")
    private BigDecimal total;

    @ApiModelProperty(value = "优惠金额",example = "123456789012.1234")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "应收金额",example = "123456789012.1234")
    private BigDecimal actual;

    @ApiModelProperty(value = "实收金额",example = "123456789012.1234")
    private BigDecimal money;

    @ApiModelProperty(value = "应收/付款金额",example = "123456789012.1234")
    private BigDecimal actual2;

    @ApiModelProperty(value = "备注",example = "你好")
    private String data;
}

