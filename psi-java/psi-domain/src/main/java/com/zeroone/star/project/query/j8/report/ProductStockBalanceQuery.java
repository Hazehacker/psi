package com.zeroone.star.project.query.j8.report;


import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * 商品库存余额查询dto
 * - 获取报表（支持条件查询 + 分页）
 * - 导出数据（Excel / CSV）
 * autor:冯烨
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("商品库存余额查询")
public class ProductStockBalanceQuery extends PageQuery {
    // 商品名称
    @ApiModelProperty(value = "商品名称", example = "小刀")
    private String productName;
    // 仓库信息
    @ApiModelProperty(value = "仓库信息", example = "仓库id")
    private List<String> warehouseId;
    // 查询日期
    @ApiModelProperty(value = "查询日期", example = "2025-10-17")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate queryDate;
}