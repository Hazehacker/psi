package com.zeroone.star.project.dto.j2.store;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.dto.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-18 10:46
 * @Description: 库存详情数据导出Excel对象
 * @Version: 1.0
 */
@Data
@ApiModel("InventoryDetailExcelDTO")
public class InventoryDetailExcelDTO {

    @ExcelProperty(value = "所属组织", index = 0)
    @ApiModelProperty(value = "所属组织", example = "默认组织")
    private String name;

    @ExcelProperty(value = "操作时间", index = 1)
    @ApiModelProperty(value = "操作时间", example = "2025-10-15")
    private String time;

    @ExcelProperty(value = "单据类型", index = 2)
    @ApiModelProperty(value = "单据类型", example = "采购单")
    private String type;

    @ExcelProperty(value = "单据编号", index = 3)
    @ApiModelProperty(value = "单据编号")
    private String number;

    @ExcelProperty(value = "操作类型", index = 4)
    @ApiModelProperty(
            value = "操作类型[0:减少|1:增加]",
            example = "1")
    private String direction;

    @ExcelProperty(value = "操作数量", index = 5)
    @ApiModelProperty(value = "操作数量", example = "10")
    private BigDecimal nums;
}
