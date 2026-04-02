package com.zeroone.star.project.dto.j2.store;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.zeroone.star.project.components.easyexcel.LocalDateConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
@ApiModel("BatchDetailDTO")
public class BatchDetailDTO {

    @ExcelProperty(value = "批次ID", index = 0)
    @ColumnWidth(20)
    @ApiModelProperty(value = "pid所属批次id(查询使用)", example = "1")
    private String pid;

    @ExcelProperty(value = "所属组织", index = 1)
    @ColumnWidth(20)
    @ApiModelProperty(value = "所属组织", example = "默认组织")
    private String frame;

    @ExcelProperty(value = "操作时间", index = 2, converter = LocalDateConverter.class)
    @ColumnWidth(20)
    @ApiModelProperty(value = "操作时间", example = "2025-10-13")
    private String time;

    @ExcelProperty(value = "单据类型", index = 3)
    @ColumnWidth(20)
    @ApiModelProperty(value = "单据类型", example = "其他入库单")
    private String type;

    @ExcelProperty(value = "单据编号", index = 4)
    @ColumnWidth(25)
    @ApiModelProperty(value = "单据编号", example = "QTRKD2510181347194")
    private String number;

    @ExcelProperty(value = "操作类型", index = 5)
    @ColumnWidth(20)
    @ApiModelProperty(value = "操作类型", example = "增加")
    private String info;

    @ExcelProperty(value = "操作数量", index = 6)
    @ColumnWidth(20)
    @ApiModelProperty(value = "操作数量", example = "1.0000")
    private BigDecimal nums;
}