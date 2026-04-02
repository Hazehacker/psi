package com.zeroone.star.project.dto.j2.store;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.dto.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-18 10:46
 * @Description: 商品库存详情数据对象
 * @Version: 1.0
 */
@Data
@ApiModel("InventoryDetailDTO")
public class InventoryDetailDTO {

    @ApiModelProperty(value = "单据类型", example = "采购单")
    private String type;

    @ApiModelProperty(value = "操作时间", example = "2025-10-15")
    private LocalDate time;

    @ApiModelProperty(value = "单据ID（内部使用）", example = "1")
    private String id;

    @ApiModelProperty(value = "单据编号", example = "ZCGD2510151056420")
    private String number;

    @ApiModelProperty(value = "组织ID（内部使用）", example = "1")
    private String frameId;

    @ApiModelProperty(value = "所属组织", example = "默认组织")
    private String frameName;

    @ApiModelProperty(
            value = "操作类型[0:减少|1:增加]",
            example = "1")
    private Integer direction;

    @ApiModelProperty(value = "操作数量", example = "10")
    private BigDecimal nums;
}
