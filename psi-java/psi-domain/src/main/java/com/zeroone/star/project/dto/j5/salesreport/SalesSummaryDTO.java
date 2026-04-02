package com.zeroone.star.project.dto.j5.salesreport;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 描述：销售汇总表，用于封装展示给前端的数据
 */
@Data
@ApiModel("销售汇总返回对象")
public class SalesSummaryDTO {

    @ApiModelProperty(value = "分组依据（按商品分组时为商品名称，按客户分组时为客户名称...）", example = "张三")
    private String groupField;

    @ApiModelProperty(value = "商品名称",example = "小刀")
    private String productName;

    @ApiModelProperty(value = "辅助属性（如颜色、大小）",example = "银色，10cm")
    private String attribute;

    @ApiModelProperty(value = "仓库名称",example = "a仓")
    private String warehouse;

    @ApiModelProperty(value = "单位",example = "个")
    private String unit;

    @ApiModelProperty(value = "销售单详情")
    private SalesItemDTO sales;

    @ApiModelProperty(value = "销售退货单详情")
    private SalesItemDTO returnSales;

    @ApiModelProperty(value = "汇总统计")
    private SummaryDTO summary;

}
