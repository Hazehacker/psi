package com.zeroone.star.project.dto.j5.salesreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 销售明细表数据传输对象
 * @author rainsilent
 * @date 2025/10/19
 */
@ApiModel("销售明细表数据传输对象")
@Data
public class SalesDetailFormDTO {
    @ApiModelProperty(value = "单据类型",example = "0",allowableValues = "0,1",
            notes = "销售退货单(sre)为0,销售单(sor)为1")
    private Integer type;
    @ApiModelProperty(value = "所属组织",example = "默认组织")
    private String frame;
    @ApiModelProperty(value = "客户",example = "张三")
    private String customer;
    @ApiModelProperty(value = "单据时间",example = "2025-10-19")
    private String time;
    @ApiModelProperty(value = "单据编号",example = "XSD2510191703580")
    private String number;
    @ApiModelProperty(value = "商品名称",example = "固态硬盘")
    private String name;
    @ApiModelProperty(value = "辅助属性",example = "2Tb")
    private String attr;
    @ApiModelProperty(value = "仓库",example = "一仓")
    private String warehouse;
    @ApiModelProperty(value = "单位",example = "块")
    private String unit;
    @ApiModelProperty(value = "单价",example = "900.00")
    private BigDecimal price;
    @ApiModelProperty(value = "数量",example = "2")
    private BigDecimal nums;
    @ApiModelProperty(value = "折扣额",example = "0")
    private BigDecimal dsc;
    @ApiModelProperty(value = "金额",example = "1800.00")
    private BigDecimal total;
    @ApiModelProperty(value = "税额",example = "20.00")
    private BigDecimal tat;
    @ApiModelProperty(value = "价税合计",example = "1820.00")
    private BigDecimal tpt;
    @ApiModelProperty(value = "备注信息",example = "这是一个备注信息!")
    private String data;
}
