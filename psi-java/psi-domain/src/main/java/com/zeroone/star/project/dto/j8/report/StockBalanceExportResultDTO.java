package com.zeroone.star.project.dto.j8.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("商品库存余额表导出数据返回对象")
public class StockBalanceExportResultDTO {

    @ApiModelProperty(value = "导出的数据的总条数", example = "100")
    private int totalCount;

    @ApiModelProperty(value = "导出成功的数据条数", example = "95")
    private int successCount;

    @ApiModelProperty(value = "导出失败的数据条数", example = "5")
    private int failCount;

    @ApiModelProperty(value = "导出失败的详细信息", example = "商品编号为1001的库存记录导出失败，原因：数据格式错误")
    private List<String> failDetails;
}