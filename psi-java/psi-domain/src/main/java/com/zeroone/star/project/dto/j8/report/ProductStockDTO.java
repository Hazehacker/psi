package com.zeroone.star.project.dto.j8.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品库存余额dto
 * - 获取报表（支持条件查询 + 分页）
 * - 导出数据（Excel / CSV）
 * autor:冯烨
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("商品库存余额表")
public class ProductStockDTO {
    /**
     * 商品库存数据传输对象
     */

    @ApiModelProperty(value = "余额表id", example="1")
    private String id;    // 商品名称

    @ApiModelProperty(value = "商品名称", example="小刀")
        private String productName;    // 商品名称

    @ApiModelProperty(value ="商品编号",example="01")
        private String productCode;    // 商品编号
    @ApiModelProperty(value ="规格型号",example="DJ-0001")
        private String specification;  // 规格型号
    @ApiModelProperty(value ="单位",example="个/件")
        private String unit;           // 单位
                           // 仓库列表（包含1号、2号等多个仓库）
    @ApiModelProperty(value = "仓库列表",example="1号仓库")
       private List<Warehouse> warehouses;


    // 汇总相关字段
    @ApiModelProperty(value ="总成本",example="30")
    private BigDecimal totalCost;   // 总成本
    @ApiModelProperty(value ="总数量",example="500")
    private Integer totalQuantity;  // 总数量
    @ApiModelProperty(value ="总总成本",example="7000")
    private BigDecimal overallTotalCost; // 总总成本



    // 仓库内部类
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel("仓库")
    public static class Warehouse {
        @ApiModelProperty(value = "仓库编号", example = "1")
        private Integer warehouseNo; // 1:1号仓库 2:2号仓库

        @ApiModelProperty(value = "仓库成本", example = "10")
        private BigDecimal cost;

        @ApiModelProperty(value = "仓库数量", example = "300")
        private Integer quantity;

        @ApiModelProperty(value = "仓库总成本", example = "3000")
        private BigDecimal totalCost;

    }







       /* // 2号仓库相关字段
        @ApiModelProperty(value ="2号仓库成本",example="20")
        private BigDecimal warehouse2Cost;   // 2号仓库成本
    @ApiModelProperty(value ="2号仓库数量",example="200")
        private Integer warehouse2Quantity;  // 2号仓库数量
    @ApiModelProperty(value ="2号仓库总成本",example="4000")
        private BigDecimal warehouse2TotalCost; // 2号仓库总成本

        // 1号仓库相关字段
        @ApiModelProperty(value ="1号仓库成本",example="10")
        private BigDecimal warehouse1Cost;   // 1号仓库成本
    @ApiModelProperty(value ="1号仓库数量",example="300")
        private Integer warehouse1Quantity;  // 1号仓库数量
    @ApiModelProperty(value ="1号仓库总成本",example="3000")
        private BigDecimal warehouse1TotalCost; // 1号仓库总成本*/



}
