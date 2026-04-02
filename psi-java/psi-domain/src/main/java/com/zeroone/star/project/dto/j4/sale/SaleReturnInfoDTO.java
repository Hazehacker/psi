package com.zeroone.star.project.dto.j4.sale;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Api(value = "销售退货单详情")
public class SaleReturnInfoDTO {

    @ApiModelProperty(value = "主键ID", example = "1")
    private String id;
    @ApiModelProperty(value = "销售退货单主表ID", example = "1")
    private String pid;
    @ApiModelProperty(value = "商品名称", example = "xx")
    private String goods;
    @ApiModelProperty(value = "辅助属性", example = "xx")
    private String attr;
    @ApiModelProperty(value = "单位", example = "个")
    private String unit;
    @ApiModelProperty(value = "仓库名称", example = "xx")
    private Long warehouse;
    @ApiModelProperty(value = "批次号", example = "xxx")
    private String batch;
    @ApiModelProperty(value = "生产日期", example = "2020-01-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate mfd;
    @ApiModelProperty(value = "单价", example = "1")
    private BigDecimal price;
    @ApiModelProperty(value = "数量", example = "5")
    private BigDecimal nums;
    @ApiModelProperty(value = "序列号", example = "xxx")
    private String serial;
    @ApiModelProperty(value = "折扣率", example = "20.0")
    private BigDecimal discount;
    @ApiModelProperty(value = "折扣额", example = "20.0")
    private BigDecimal dsc;
    @ApiModelProperty(value = "金额", example = "50.0")
    private BigDecimal total;
    @ApiModelProperty(value = "税率", example = "0.2")
    private BigDecimal tax;
    @ApiModelProperty(value = "税额", example = "50.2")
    private BigDecimal tat;
    @ApiModelProperty(value = "价税合计", example = "50.0")
    private BigDecimal tpt;
    @ApiModelProperty(value = "备注信息", example = "xxx")
    private String data;

}
