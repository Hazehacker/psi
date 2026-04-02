package com.zeroone.star.project.dto.j2.store;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.dto.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-19 19:04
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@ApiModel("OtherOutListInfoDTO")
@Builder
public class OtherOutListInfoDTO {
    @ExcelProperty("ID")
    @ApiModelProperty(value = "id", example = "1")
    private String id;

    @ExcelProperty("所属出库单ID")
    @ApiModelProperty(value = "所属出库单ID", example = "1")
    private String pid;

    @ExcelProperty("所属商品")
    @ApiModelProperty(value = "所属商品", example = "1")
    private String goods;

    @ExcelProperty("辅助属性")
    @ApiModelProperty(value = "辅助属性", example = "红色,L码")
    private String attr;

    @ExcelProperty("单位")
    @ApiModelProperty(value = "单位", example = "个")
    private String unit;

    @ExcelProperty("仓库")
    @ApiModelProperty(value = "仓库", example = "1")
    private String warehouse;

    @ExcelProperty("批次号")
    @ApiModelProperty(value = "批次号", example = "B20231019001")
    private String batch;

    @ExcelProperty("生产日期")
    @ApiModelProperty(value = "生产日期", example = "2023-10-18T18:18:00")
    private LocalDateTime mfd;

    @ExcelProperty("成本")
    @ApiModelProperty(value = "成本", example = "10.00")
    private BigDecimal price;

    @ExcelProperty("数量")
    @ApiModelProperty(value = "数量", example = "100")
    private BigDecimal nums;

    @ExcelProperty("序列号")
    @ApiModelProperty(value = "序列号", example = "SN001,SN002")
    private String serial;

    @ExcelProperty("总成本")
    @ApiModelProperty(value = "总成本", example = "1000.00")
    private BigDecimal total;

    @ExcelProperty("备注信息")
    @ApiModelProperty(value = "备注信息", example = "备注")
    private String data;
}
