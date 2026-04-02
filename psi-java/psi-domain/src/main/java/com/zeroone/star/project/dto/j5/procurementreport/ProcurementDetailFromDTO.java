package com.zeroone.star.project.dto.j5.procurementreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 采购明细表数据传输对象
 * @author chuming_7
 * @since 2025-10-21
 */
@ApiModel("采购明细表数据传输对象")
@Data
public class ProcurementDetailFromDTO {
    @ApiModelProperty(value = "单据类型",example = "0",allowableValues = "0,1",
            notes = "0:采购单,1:退货单")
    private Integer type;
    @ApiModelProperty(value = "所属组织",example = "默认组织")
    private String frame;
    @ApiModelProperty(value = "供应商",example = "王麻子")
    private String supplier;
    @ApiModelProperty(value = "单据时间",example = "2025-10-20")
    private String time;
    @ApiModelProperty(value = "单据编号",example = "XSD2510191703580")
    private String number;
    @ApiModelProperty(value = "商品名称",example = "iPhone")
    private String name;
    @ApiModelProperty(value = "辅助属性",example = "16")
    private String attr;
    @ApiModelProperty(value = "仓库",example = "一仓")
    private String warehouse;
    @ApiModelProperty(value = "单位",example = "块")
    private String unit;
    @ApiModelProperty(value = "单价",example = "7900.00")
    private BigDecimal price;
    @ApiModelProperty(value = "数量",example = "1")
    private BigDecimal nums;
    @ApiModelProperty(value = "折扣额",example = "0")
    private BigDecimal dsc;
    @ApiModelProperty(value = "金额",example = "7900.00")
    private BigDecimal total;
    @ApiModelProperty(value = "税额",example = "20.00")
    private BigDecimal tat;
    @ApiModelProperty(value = "价税合计",example = "7920.00")
    private BigDecimal tpt;
    @ApiModelProperty(value = "备注信息",example = "这是一个备注信息!")
    private String data;
}
