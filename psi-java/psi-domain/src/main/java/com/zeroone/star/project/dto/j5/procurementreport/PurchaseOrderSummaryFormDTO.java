package com.zeroone.star.project.dto.j5.procurementreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelProperty;


@Data
@ApiModel("采购汇总表数据对象")
public class PurchaseOrderSummaryFormDTO {
    // ========== 隐藏字段 ==========
    @ApiModelProperty("供应商")
    @ExcelProperty("供应商")
    private String supplier;

    @ApiModelProperty("用户")
    @ExcelProperty("用户")
    private String user;

    @ApiModelProperty("关联人员")
    @ExcelProperty("关联人员")
    private String people;

    @ApiModelProperty(value = "商品名称", example = "衬衫")
    @ExcelProperty("商品名称")
    private String goods;

    @ApiModelProperty(value = "辅助属性", example = "XL")
    @ExcelProperty("辅助属性")
    private String attr;

    @ApiModelProperty(value = "仓库", example = "1号仓库")
    @ExcelProperty("仓库")
    private String warehouse;

    @ApiModelProperty(value = "单位", example = "件")
    @ExcelProperty("单位")
    private String unit;

    // ========== 采购单 & 退货单 & 汇总 ==========
    @ApiModelProperty("采购单信息")
    private OrderInfo buy;

    @ApiModelProperty("采购退货单信息")
    private OrderInfo bor;

    @ApiModelProperty("汇总信息")
    private Summary summary;

    // ========== 内部类定义 ==========
    @Data
    @ApiModel("订单信息（采购单/退货单通用）")
    public static class OrderInfo {
        @ApiModelProperty("单价")
        @ExcelProperty("单价")
        private BigDecimal price;

        @ApiModelProperty("数量")
        @ExcelProperty("数量")
        private BigDecimal nums;

        @ApiModelProperty("金额")
        @ExcelProperty("金额")
        private BigDecimal money;
    }

    @Data
    @ApiModel("汇总信息")
    public static class Summary {
        @ApiModelProperty("数量")
        @ExcelProperty({"汇总", "数量"})
        private BigDecimal nums;

        @ApiModelProperty("金额")
        @ExcelProperty({"汇总", "金额"})
        private BigDecimal money;
    }
}

